package org.example.command;

import org.example.FTPService;

public class DownloadFile implements Command {

    private FTPService ftpService;

    public DownloadFile() {
        ftpService = FTPService.getInstance();
    }

    @Override
    public void execute(String fileName) {
        ftpService.downloadFiles(fileName);
    }
}
