package de.neozo.jblockchain.node.service;


import de.neozo.jblockchain.common.domain.Block;
import de.neozo.jblockchain.common.domain.Transaction;
import de.neozo.jblockchain.node.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class MiningService implements Runnable {

    private final static Logger LOG = LoggerFactory.getLogger(MiningService.class);

    private final TransactionService transactionService;
    private final NodeService nodeService;
    private final BlockService blockService;

    private AtomicBoolean runMiner = new AtomicBoolean(false);


    @Autowired
    public MiningService(TransactionService transactionService, NodeService nodeService, BlockService blockService) {
        this.transactionService = transactionService;
        this.nodeService = nodeService;
        this.blockService = blockService;
    }

    public void startMiner() {
        if (runMiner.compareAndSet(false, true)) {
            LOG.info("Starting miner");
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    public void stopMiner() {
        LOG.info("Stopping miner");
        runMiner.set(false);
    }

    @Override
    public void run() {
        while (runMiner.get()) {
            Block block = mineBlock();
            if (block != null) {
                LOG.info("Mined block with " + block.getTransactions().size() + " transactions and nonce " + block.getNonce());
                blockService.append(block);
                nodeService.broadcastPut("block", block);
            }
        }
        LOG.info("Miner stopped");
    }

    private Block mineBlock() {
        long nonce = 0;

        // get previous hash and transactions
        byte[] previousBlockHash = blockService.getLastBlock() != null ? blockService.getLastBlock().getHash() : null;
        List<Transaction> transactions = transactionService.getTransactionPool()
                .stream().limit(Config.MAX_TRANSACTIONS_PER_BLOCK).collect(Collectors.toList());

        if (transactions.isEmpty()) {
            LOG.info("No transactions available, pausing");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                LOG.error("Thread interrupted", e);
            }
            return null;
        }

        // try new block until difficulty is sufficient
        while (runMiner.get()) {
            Block block = new Block(previousBlockHash, transactions, nonce);
            if (block.getLeadingZerosCount() >= Config.DIFFICULTY) {
                return block;
            }
            nonce++;
        }
        return null;
    }

}
