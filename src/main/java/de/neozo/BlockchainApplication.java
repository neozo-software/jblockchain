package de.neozo;

import de.neozo.entities.Block;
import de.neozo.entities.Blockchain;
import de.neozo.entities.Node;
import de.neozo.entities.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@SpringBootApplication
public class BlockchainApplication {

	private Blockchain blockchain;
	private Set<Transaction> transactionPool;
	private Set<Node> knownNodes;

	@RequestMapping("/get-blockchain")
	Blockchain getBlockchain() {
		return blockchain;
	}

	@RequestMapping("/get-transaction-pool")
	Set<Transaction> getTransactionPool() {
		return transactionPool;
	}

	@RequestMapping("/get-known-nodes")
	Set<Node> getKnownNodes() {
		return knownNodes;
	}

	@RequestMapping("/add-node")
	void addNode(Node node) {
		knownNodes.add(node);
	}

	@RequestMapping("/remove-node")
	void removeNode(Node node) {
		knownNodes.remove(node);
	}


	@RequestMapping("/add-transaction")
	void addTransaction(Transaction transaction) {
		transactionPool.add(transaction);
	}

	@RequestMapping("/publish-transaction")
	void publishTransaction(Transaction transaction) {
		// broadcast transaction to all nodes
	}


	@RequestMapping("/add-block")
	void addBlock(Block block) {
		blockchain.append(block);
	}

	@RequestMapping("/publish-block")
	void publishBlock(Block block) {
		// broadcast block to all nodes
	}


	public static void main(String[] args) {
		SpringApplication.run(BlockchainApplication.class, args);
	}
}
