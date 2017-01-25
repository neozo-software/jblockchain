package de.neozo.blockchain.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public abstract class SignatureUtils {

    private final static Logger LOG = LoggerFactory.getLogger(SignatureUtils.class);

    private static KeyFactory keyFactory = null;

    static {
        try {
            keyFactory = KeyFactory.getInstance("DSA", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            LOG.error("Failed initializing keyFactory", e);
        }
    }

    public static boolean verify(byte[] data, byte[] signature, byte[] publicKey) throws InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        PublicKey publicKeyObj = keyFactory.generatePublic(keySpec);
        Signature sig = getSignatureObj();
        sig.initVerify(publicKeyObj);
        sig.update(data);
        return sig.verify(signature);
    }

    public static KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        return keyGen.generateKeyPair();
    }

    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey privateKeyObj = keyFactory.generatePrivate(keySpec);
        Signature sig = getSignatureObj();
        sig.initSign(privateKeyObj);
        sig.update(data);
        return sig.sign();
    }

    private static Signature getSignatureObj() throws NoSuchProviderException, NoSuchAlgorithmException {
        return Signature.getInstance("SHA1withDSA", "SUN");
    }

}
