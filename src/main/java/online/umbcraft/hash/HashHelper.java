package online.umbcraft.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashHelper {

    private static MessageDigest sha256digest;

    static {
        try {
            sha256digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // gets the base 64 encoded hash of a string of text - only works with UTF-8 characters
    public static String getHash(String plaintext) {

        byte[] byteHash = sha256digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));
        String base64hash = Base64.getEncoder().encodeToString(byteHash);
        return base64hash;

    }
}
