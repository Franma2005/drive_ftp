package org.example;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        FileListenerServices fileListenerServices = new FileListenerServices();
        Download download = new Download();
        FTPService ftpService = FTPService.getInstance();

        ftpService.connect();
        download.start();
        fileListenerServices.observateEvent();
        ftpService.disconnect();
    }
}