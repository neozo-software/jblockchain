package de.neozo.blockchain;


import de.neozo.blockchain.domain.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class Config {

    public final static Logger LOG = LoggerFactory.getLogger(Config.class);

    public static Node MASTER_NODE;


    static {
        try {
            MASTER_NODE = new Node().setAddress(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            LOG.error("can't init master node", e);
        }
    }

}
