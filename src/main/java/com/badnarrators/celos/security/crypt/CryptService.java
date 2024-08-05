package com.badnarrators.celos.security.crypt;

import org.springframework.stereotype.Service;

@Service
public class CryptService {

    // Simple crypt service for now, it's not a professional environment
    public String encrypt(String plainText) {
        int shift = 5;

        plainText = caesarEncrypt(plainText, shift);

        plainText = randomChars(5) + plainText + randomChars(3);

        return plainText;
    }

    public String decrypt(String cipherText) {
        int shift = 5;

        cipherText = cipherText.substring(5, cipherText.length() - 3);

        cipherText = caesarDecrypt(cipherText, shift);

        return cipherText;
    }

    // Generate random characters
    public String randomChars(int length) {
        StringBuilder chars = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (Math.random() * 26 + 'a');
            chars.append(c);
        }
        return chars.toString();
    }

    // Simple Caesar's cipher
    public String caesarEncrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                if (Character.isUpperCase(c)) {
                    c = (char) ('A' + (c - 'A' + shift) % 26);
                } else {
                    c = (char) ('a' + (c - 'a' + shift) % 26);
                }
            }
            result.append(c);
        }
        return result.toString();
    }
    public String caesarDecrypt(String text, int shift) {
        return caesarEncrypt(text, 26 - shift);
    }
}
