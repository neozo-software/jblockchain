package de.neozo.jblockchain.node;


public abstract class Config {

    /**
     * Address of a Node to use for initialization
     */
    public static final String MASTER_NODE_ADDRESS = "http://localhost:8080";

    /**
     * Minimum number of leading zeros every block hash has to fulfill
     */
    public static final int DIFFICULTY = 3;

    /**
     * Maximum numver of Transactions a Block can hold
     */
    public static final int MAX_TRANSACTIONS_PER_BLOCK = 5;


}
