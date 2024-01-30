package com.passManager.pass;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import io.vertx.core.json.JsonObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.spec.KeySpec;
import java.util.HashMap;

public class EncryptAndStore {
    public static void store(HashMap<Integer, Password> hashMap, String password) {
        try {
            // Generate a secret key from the provided password
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 65536, 256);
            SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            // Create a cipher instance
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Encrypt the HashMap
            byte[] encryptedData = cipher.doFinal(hashMap.toString().getBytes());

            // Create a file output stream
            FileOutputStream fileOutputStream = new FileOutputStream("data.txt");

            // Create an object output stream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write the encrypted data to the file
            objectOutputStream.writeObject(encryptedData);

            // Close the streams
            objectOutputStream.close();
            fileOutputStream.close();

            System.out.println("HashMap encrypted and stored in data.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, Password> decrypt(String password) {
        HashMap<Integer, Password> decryptedHashMap = new HashMap<>();

        try {
            // Read the encrypted data from the file
            FileInputStream fileInputStream = new FileInputStream("data.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            byte[] encryptedData = (byte[]) objectInputStream.readObject();

            // Generate the secret key from the provided password
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 65536, 256);
            SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            // Create a cipher instance
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decrypt the data
            byte[] decryptedData = cipher.doFinal(encryptedData);

            // Convert the decrypted data back to HashMap
            String decryptedString = new String(decryptedData);
            String[] decryptedArray = decryptedString.split(", ");
            decryptedHashMap = new HashMap<>();
            for (int i = 0; i < decryptedArray.length; i += 2) {
                int key = Integer.parseInt(decryptedArray[i]);
                Password value = new Password(new JsonObject(decryptedArray[i + 1]));
                decryptedHashMap.put(key, value);
            }
            // Close the streams
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedHashMap;
    }

}
