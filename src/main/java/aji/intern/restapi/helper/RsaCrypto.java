package aji.intern.restapi.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaCrypto {

    private static final Logger log = LogManager.getLogger(RsaCrypto.class);

    private static final OAEPParameterSpec OAEP_SHA256 =
            new OAEPParameterSpec(
                    "SHA-256",
                    "MGF1",
                    MGF1ParameterSpec.SHA256,
                    PSource.PSpecified.DEFAULT);

    public static String encrypt(String pin, String key) {
        try {
            PublicKey publicKey = getPublicKeyFromString(key);

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey, OAEP_SHA256);

            byte[] encryptedBytes = cipher.doFinal(pin.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("Failed to encrypt content, reason : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static PublicKey getPublicKeyFromString(String publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedKey = decodeKeyFromString(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    private static byte[] decodeKeyFromString(String key) {
        return Base64.getDecoder().decode(key);
    }

}
