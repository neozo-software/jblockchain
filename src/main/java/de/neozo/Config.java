package de.neozo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class Config {

    public final static Logger LOG = LoggerFactory.getLogger(Config.class);

    public static InetAddress MASTER_NODE;


    static {
        try {
            MASTER_NODE = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOG.error("can't init master node", e);
        }
    }

}
