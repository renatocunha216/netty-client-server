package br.com.rbcti.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class Sha1 {

    private MessageDigest md = null;
    private boolean reseted;
    private byte[] hash;

    public Sha1() {
        reseted = true;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void update(byte[] input) {
        md.update(input);
        reseted = false;
    }

    public byte[] digest() {
        if(!reseted) {
            hash = md.digest();
            reseted = true;
        }
        return hash;
    }

    public void reset() {
        md.reset();
        reseted = true;
    }

    public String toString() {
        return toHex(digest());
    }

    private String toHex(byte[] data) {
        StringBuilder buffer = new StringBuilder();
        for(byte b: data) {
            String aux = Integer.toHexString(0xFF & b);
            if ((aux.length() == 1)) {
                buffer.append("0");
            }
            buffer.append(aux);
        }
        return buffer.toString();
    }

    public String toBase64() {
        return Base64.encodeBase64String(digest());
    }

}