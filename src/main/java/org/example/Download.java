package org.example;

import org.example.command.DownloadFile;
import org.example.command.DownloadFileFromHistory;

import java.util.Scanner;

public class Download extends Thread{

    private Keyboard keyboard;
    private DownloadFile downloadFile;
    private DownloadFileFromHistory downloadFileFromHistory;

    public Download() {
        keyboard = Keyboard.getInstance();
        downloadFile = new DownloadFile();
        downloadFileFromHistory = new DownloadFileFromHistory();
    }

    public void download() {
        while (true) {
            System.out.println("De que directorio quieres descargar:" +
                    "   1) Directorio Principal" +
                    "   2) Directorio history");
            switch (keyboard.getKeyboard().nextInt()) {
                case 1:
                    downloadFile.execute(keyboard.getKeyboard().next());
                    break;
                case 2:
                    downloadFileFromHistory.execute(keyboard.getKeyboard().next());
                    break;
            }
        }
    }

    @Override
    public void run() {
        download();
    }
}
