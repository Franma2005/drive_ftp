package org.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.example.entities.Config;

import java.io.*;
import java.nio.file.Path;

public class FTPService {

    private static FTPService ftpService;
    private FTPClient ftpClient;
    private AESManager aesManager;
    private Config config;

    private FTPService() {
        ftpClient = new FTPClient();
        config = Config.getInstance();
        aesManager = new AESManager();
    }

    public static FTPService getInstance() {
        if(ftpService == null)
            ftpService = new FTPService();
        return ftpService;
    }

    public void connect() {
        try {
            ftpClient.connect(config.getSERVER(), config.getPORT());
            int response = ftpClient.getReplyCode();

            if(!FTPReply.isPositiveCompletion(response)) {
                ftpClient.disconnect();
                throw new IOException("Error al conectar con el servidor FTP");
            }

            boolean credentials = ftpClient.login(config.getUSER(), config.getPASSWORD());

            if (!credentials) {
                ftpClient.disconnect();
                throw new IOException("Error al conectar con el servidor FTP credenciales incorrectas");
            }

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch(IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void disconnect() {
        try {
            ftpClient.disconnect();
        } catch (IOException exception) {
            System.out.println("Fallo al desconectar el cliente");
        }
    }

    public boolean uploadFiles(String fileName, boolean modified) {
        try {
            Path filePath = config.getFILENAME().resolve(fileName);
            File fileLocal = filePath.toFile();

            if(modified) {
                File temporary = File.createTempFile("temp_downloaded_", null);
                OutputStream outputStream = new FileOutputStream(temporary);
                ftpClient.retrieveFile(fileLocal.getName(), outputStream);
                outputStream.close(); // Faltaba cerrar este stream

                InputStream inputStream2 = new FileInputStream(temporary);

                ftpClient.changeWorkingDirectory("history");

                boolean renamed = ftpClient.storeFile("history_" + fileLocal.getName(), inputStream2);
                inputStream2.close();
                ftpClient.changeToParentDirectory();

                temporary.delete();
            }

            InputStream inputStream1 = new FileInputStream(aesManager.encryptation(fileLocal));
            boolean send = ftpClient.storeFile(fileLocal.getName(), inputStream1);


            inputStream1.close();
            return send;
        } catch (IOException exception) {
            System.out.println("Error al subir el archivo " + exception);
        }
        return false;
    }

    public boolean downloadFiles(String fileName) {
        try {
            // Definir un archivo temporal para almacenar el archivo encriptado descargado
            File tempEncryptedFile = File.createTempFile("temp_downloaded_", null);

            // Descargar el archivo encriptado del servidor FTP
            try (OutputStream outputStream = new FileOutputStream(tempEncryptedFile)) {
                boolean receive = ftpClient.retrieveFile(fileName, outputStream);

                if (!receive) {
                    System.out.println("Error al descargar el archivo del servidor FTP");
                    return false;
                }
            }

            // Desencriptar el archivo
            File decryptedFile = aesManager.desencryptation(
                    tempEncryptedFile,
                    config.getFILENAME().resolve(fileName).toString()
            );

            // Eliminar el archivo temporal después de desencriptar
            tempEncryptedFile.delete();

            return decryptedFile != null;
        } catch (FileNotFoundException exception) {
            System.out.println("Fichero no encontrado");
        } catch (IOException exception) {
            System.out.println("Error al recuperar el archivo");
        }
        return false;
    }

    public boolean downloadFilesFromHistory(String fileName) {
        try {
            // Definir un archivo temporal para almacenar el archivo encriptado descargado
            File tempEncryptedFile = File.createTempFile("temp_history_", null);

            // Cambiar al directorio history
            boolean changeDir = ftpClient.changeWorkingDirectory("history");
            if (!changeDir) {
                System.out.println("No se pudo acceder al directorio history");
                return false;
            }

            // Descargar el archivo encriptado del directorio history
            try (OutputStream outputStream = new FileOutputStream(tempEncryptedFile)) {
                boolean receive = ftpClient.retrieveFile(fileName, outputStream);

                if (!receive) {
                    System.out.println("Error al descargar el archivo del directorio history");
                    ftpClient.changeToParentDirectory(); // Volver al directorio principal
                    return false;
                }
            }

            // Volver al directorio principal
            ftpClient.changeToParentDirectory();

            // Crear un nombre específico para el archivo de history (para evitar sobrescribir)
            String historyFileName = "history_" + fileName;

            // Desencriptar el archivo
            File decryptedFile = aesManager.desencryptation(
                    tempEncryptedFile,
                    config.getFILENAME().resolve(historyFileName).toString()
            );

            // Eliminar el archivo temporal después de desencriptar
            tempEncryptedFile.delete();

            return decryptedFile != null;
        } catch (FileNotFoundException exception) {
            System.out.println("Fichero no encontrado en history");
        } catch (IOException exception) {
            System.out.println("Error al recuperar el archivo de history");
            try {
                // Asegurarnos de volver al directorio principal si ocurre un error
                ftpClient.changeToParentDirectory();
            } catch (IOException e) {
                // Ignorar error secundario
            }
        }
        return false;
    }

    public boolean deleteFiles(String fileName) {
        try {

            File temporary = File.createTempFile("temp_downloaded_", null);
            OutputStream outputStream = new FileOutputStream(temporary);
            ftpClient.retrieveFile(fileName, outputStream);
            outputStream.close(); // Faltaba cerrar este stream

            InputStream inputStream2 = new FileInputStream(temporary);

            ftpClient.changeWorkingDirectory("history");

            boolean renamed = ftpClient.storeFile("history_" + fileName, inputStream2);
            inputStream2.close();
            ftpClient.changeToParentDirectory();

            temporary.delete();

            System.out.println("Ruta " + "/localhost" + "/" + fileName);
            boolean deleted = ftpClient.deleteFile("/" +fileName);

            if (deleted)
                System.out.println("El archivo " + fileName + " se elimino correctamente");
            else
                System.out.println("El archivo " + fileName + " no fue encontrado");

            return deleted;
        } catch (IOException exception) {
            System.out.println("Error al eliminar el archivo");
        }
        return false;
    }
}
