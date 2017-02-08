package de.neozo.jblockchain.node.rest;


import de.neozo.jblockchain.common.domain.Transaction;
import de.neozo.jblockchain.node.service.NodeService;
import de.neozo.jblockchain.node.service.TransactionService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;


@RestController()
@RequestMapping("transaction")
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final NodeService nodeService;

    @Autowired
    public TransactionController(TransactionService transactionService, NodeService nodeService) {
        this.transactionService = transactionService;
        this.nodeService = nodeService;
    }

    /**
     * Retrieve all Transactions, which aren't in a block yet
     * @return JSON list of all Transactions
     */
    @RequestMapping
    Set<Transaction> getTransactionPool() {
        return transactionService.getTransactionPool();
    }


    /**
     * Add a new Transaction to the pool.
     * It is expected that the transaction has a valid signature and the correct hash.
     *
     * @param transaction the Transaction to add
     * @param publish if true, this Node is going to inform all other Nodes about the new Address
     * @param response Status Code 202 if Transaction accepted, 406 if verification fails
     */
    @RequestMapping(method = RequestMethod.PUT)
    void addTransaction(@RequestBody Transaction transaction, @RequestParam(required = false) Boolean publish, HttpServletResponse response) {
        LOG.info("Add transaction " + Base64.encodeBase64String(transaction.getHash()));
        boolean success = transactionService.add(transaction);

        if (success) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);

            if (publish != null && publish) {
                nodeService.broadcastPut("transaction", transaction);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }

}
