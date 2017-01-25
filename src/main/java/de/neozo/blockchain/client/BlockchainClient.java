package de.neozo.blockchain.client;


import de.neozo.blockchain.common.SignatureUtils;
import de.neozo.blockchain.common.domain.Address;
import de.neozo.blockchain.common.domain.Transaction;
import org.apache.commons.cli.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;

// TODO: separate module

public class BlockchainClient {

    public static void main(String args[]) throws Exception {
        CommandLineParser parser = new DefaultParser();
        Options options = getOptions();
        try {
            CommandLine line = parser.parse(options, args);
            executeCommand(line);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("BlockchainClient", options , true);
        }
    }

    private static void executeCommand(CommandLine line) throws Exception {
        if (line.hasOption("keypair")) {
            generateKeyPair();
        } else if (line.hasOption("address")) {
            String node = line.getOptionValue("node");
            String name = line.getOptionValue("name");
            String publickey = line.getOptionValue("publickey");
            if (node == null || name == null || publickey == null) {
                throw new ParseException("node, name and publickey is required");
            }
            publishAddress(new URL(node), Paths.get(publickey), name);

        } else if (line.hasOption("transaction")) {
            String node = line.getOptionValue("node");
            String message = line.getOptionValue("message");
            String sender = line.getOptionValue("sender");
            String privatekey = line.getOptionValue("privatekey");
            if (node == null || message == null || sender == null || privatekey == null) {
                throw new ParseException("node, message, sender and privatekey is required");
            }
            publishTransaction(new URL(node), Paths.get(privatekey), message, Base64.getDecoder().decode(sender));
        }
    }

    private static Options getOptions() {
        OptionGroup actions = new OptionGroup();
        actions.addOption(new Option("k", "keypair", false, "generate private/public key pair"));
        actions.addOption(new Option("a", "address", false, "publish new address"));
        actions.addOption(new Option("t", "transaction", false, "publish new transaction"));
        actions.setRequired(true);

        Options options = new Options();
        options.addOptionGroup(actions);
        options.addOption(Option.builder("o")
                .longOpt("node")
                .hasArg()
                .argName("Node URL")
                .desc("needed for address and transaction publishing")
                .build());
        options.addOption(Option.builder("n")
                .longOpt("name")
                .hasArg()
                .argName("name for new address")
                .desc("needed for address publishing")
                .build());
        options.addOption(Option.builder("p")
                .longOpt("publickey")
                .hasArg()
                .argName("path to key file")
                .desc("needed for address publishing")
                .build());
        options.addOption(Option.builder("v")
                .longOpt("privatekey")
                .hasArg()
                .argName("path to key file")
                .desc("needed for transaction publishing")
                .build());
        options.addOption(Option.builder("m")
                .longOpt("message")
                .hasArg()
                .argName("message to post")
                .desc("needed for transaction publishing")
                .build());
        options.addOption(Option.builder("s")
                .longOpt("sender")
                .hasArg()
                .argName("address hash (Base64)")
                .desc("needed for transaction publishing")
                .build());

        return options;
    }

    private static void generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        KeyPair keyPair = SignatureUtils.generateKeyPair();
        Files.write(Paths.get("key.priv"), keyPair.getPrivate().getEncoded());
        Files.write(Paths.get("key.pub"), keyPair.getPublic().getEncoded());
    }

    private static void publishAddress(URL node, Path publicKey, String name) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(node.toString() + "/address?publish=true", new Address(name, Files.readAllBytes(publicKey)));
    }

    private static void publishTransaction(URL node, Path privateKey, String text, byte[] senderHash) throws Exception {
        byte[] signature = SignatureUtils.sign(text.getBytes(), Files.readAllBytes(privateKey));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(node.toString() + "/transaction?publish=true", new Transaction(text, senderHash, signature));
    }
}
