package de.neozo.blockchain.service;


import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class SignatureSevice {

    private KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");

    public SignatureSevice() throws NoSuchProviderException, NoSuchAlgorithmException {
    }

    public boolean verify(byte[] data, byte[] signature, byte[] publicKey) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        PublicKey publicKeyObj = keyFactory.generatePublic(keySpec);
        Signature sig = getSignatureObj();
        sig.initVerify(publicKeyObj);
        sig.update(data);
        return sig.verify(signature);
    }

    public KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        return keyGen.generateKeyPair();
    }

    public byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey privateKeyObj = keyFactory.generatePrivate(keySpec);
        Signature sig = getSignatureObj();
        sig.initSign(privateKeyObj);
        sig.update(data);
        return sig.sign();
    }

    private Signature getSignatureObj() throws NoSuchProviderException, NoSuchAlgorithmException {
        return Signature.getInstance("SHA1withDSA", "SUN");
    }

}
