package org.example;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AESManager {

    private final String key = "Clave Server FTP";
    private SecretKeySpec encryptionKey;

    // Generar clave de encriptación y desencriptación
    private void createKey() {

        try {
            byte[] sentence = key.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            sentence = md.digest(sentence);
            sentence = Arrays.copyOf(sentence, 16);
            encryptionKey = new SecretKeySpec(sentence, "AES");
        } catch (UnsupportedEncodingException exception) {
            System.out.println("Error al codificar la clave de encriptación");
        // Esta excepción se lanza cuando se solicita un algoritmo criptográfico particular pero no esta
        // disponible en el entorno
        } catch (NoSuchAlgorithmException exception) {
            System.out.println("El algoritmo no es compatible");
        }
    }

    // Metodo de encriptación de un archivo claude
    public File encryptation(File encrypt) {
            try {
                createKey();
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);

                // Leer el archivo original
                byte[] fileInBytes = Files.readAllBytes(encrypt.toPath());

                // Encriptar los datos
                byte[] encrypted = cipher.doFinal(fileInBytes);

                // Crear un archivo temporal para almacenar los datos encriptados
                File tempFile = File.createTempFile("temp_enc_", null);

                // Escribir los datos encriptados al archivo temporal
                Files.write(tempFile.toPath(), encrypted);

                return tempFile;
        // Esta excepcion se lanza por fallos en la clave. Como por ejemplo no haberle asignado un tamaño de 16 bytes
        } catch(InvalidKeyException exception) {
            System.out.println("La clave SHA-1 es invalida");
        // Esta excepción se lanza cuando se solicita un algoritmo criptográfico particular pero no esta
        // disponible en el entorno
        } catch (NoSuchAlgorithmException exception) {
            System.out.println("El algoritmo no es compatible");
        // Le has puesto un padding que es el mecanismo que se usa para que la cadena tenga 16 bytes inventado
        } catch (NoSuchPaddingException exception) {
            System.out.println("No existe el mecanismo de padding indicado");
        } catch (IOException exception) {
            System.out.println("Error al leer los bytes del fichero");
        } catch (IllegalBlockSizeException exception) {
            System.out.println("Error en el tamaño de los datos");
        } catch (BadPaddingException exception) {
            System.out.println("El padding no coincide con el formato esperado");
        }
        return null;
    }

    // Metodo de desencriptacion
    public File desencryptation(File encryptedFile, String outputPath) {
        try {
            createKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, encryptionKey);

            // Leer el archivo encriptado
            byte[] encryptedBytes = Files.readAllBytes(encryptedFile.toPath());

            // Desencriptar los datos
            byte[] desencrypted = cipher.doFinal(encryptedBytes);

            // Guardar el resultado en un archivo
            File outputFile = new File(outputPath);
            Files.write(outputFile.toPath(), desencrypted);
            return outputFile;
        } catch(InvalidKeyException exception) {
            System.out.println("La clave SHA-1 es invalida");
        } catch (NoSuchAlgorithmException exception) {
            System.out.println("El algoritmo no es compatible");
        } catch (NoSuchPaddingException exception) {
            System.out.println("No existe el mecanismo de padding indicado");
        } catch (IllegalBlockSizeException exception) {
            System.out.println("Error en el tamaño de los datos");
        } catch (BadPaddingException exception) {
            System.out.println("El padding no coincide con el formato esperado");
        } catch (IOException exception) {
            System.out.println("Error al escribir los bytes al fichero");
        }
        return null;
    }
}
