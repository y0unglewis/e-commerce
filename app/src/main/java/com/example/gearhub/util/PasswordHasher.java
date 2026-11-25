package com.example.gearhub.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {
    public static final String SHA256 = "PBKDF2WithHmacSHA256";
    public static final String SHA1 = "PBKDF2WithHmacSHA1";
    public static final int SHA256_ITERATIONS = 600000;
    public static final int SHA1_ITERATIONS = 1400000;

    private PasswordHasher() { }

    public static byte[] newSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // Không lưu mật khẩu gốc. Salt khác nhau cho mỗi tài khoản.
    public static byte[] hash(String password, byte[] salt,
                              String algorithm, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
        try {
            return SecretKeyFactory.getInstance(algorithm).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Không thể xử lý mật khẩu", e);
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean matches(String password, byte[] salt, byte[] expected,
                                  String algorithm, int iterations) {
        if (salt == null || expected == null) return false;
        return MessageDigest.isEqual(expected, hash(password, salt, algorithm, iterations));
    }
}
