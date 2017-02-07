package de.neozo.jblockchain.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public abstract class SignatureUtils {

    private final static Logger LOG = LoggerFactory.getLogger(SignatureUtils.class);

    /**
     * The keyFactory defines which algorithms are used to generate the private/public keys.
     */
    private static KeyFactory keyFactory = null;

    static {
        try {
            keyFactory = KeyFactory.getInstance("DSA", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            LOG.error("Failed initializing keyFactory", e);
        }
    }

    /**
     * Generate a random key pair.
     * @return KeyPair containg private and public key
     */
    public static KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        return keyGen.generateKeyPair();
    }

    /**
     * Verify if the given signature is valid regarding the data and publicKey.
     * @param data raw data which was signed
     * @param signature to proof the validity of the sender
     * @param publicKey key to verify the data was signed by owner of corresponding private key
     * @return true if the signature verification succeeds.
     */
    public static boolean verify(byte[] data, byte[] signature, byte[] publicKey) throws InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // construct a public key from raw bytes
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        PublicKey publicKeyObj = keyFactory.generatePublic(keySpec);

        // do the verification
        Signature sig = getSignatureObj();
        sig.initVerify(publicKeyObj);
        sig.update(data);
        return sig.verify(signature);
    }

    /**
     * Sign given data with a private key
     * @param data raw data to sign
     * @param privateKey to use for the signage process
     * @return signature of data which can be verified with corresponding public key
     */
    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        // construct a PrivateKey-object from raw bytes
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey privateKeyObj = keyFactory.generatePrivate(keySpec);

        // do the signage
        Signature sig = getSignatureObj();
        sig.initSign(privateKeyObj);
        sig.update(data);
        return sig.sign();
    }

    private static Signature getSignatureObj() throws NoSuchProviderException, NoSuchAlgorithmException {
        return Signature.getInstance("SHA1withDSA", "SUN");
    }

}
