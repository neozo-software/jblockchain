package de.neozo.jblockchain.common.domain;


import javax.validation.constraints.NotNull;
import java.net.URL;

public class Node {

    /**
     * HTTP-Address including port on which the addressed node listens for incoming connections
     */
    @NotNull
    private URL address;

    public Node() {
    }

    public Node(URL address) {
        this.address = address;
    }

    public URL getAddress() {
        return address;
    }

    public void setAddress(URL address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return address != null ? address.equals(node.address) : node.address == null;
    }

    @Override
    public int hashCode() {
        return address != null ? address.hashCode() : 0;
    }
}
