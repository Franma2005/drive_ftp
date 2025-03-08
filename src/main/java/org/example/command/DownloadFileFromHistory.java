package org.example.command;

import org.example.FTPService;

public class DownloadFileFromHistory implements Command{
    private FTPService ftpService;

    public DownloadFileFromHistory() {
        ftpService = FTPService.getInstance();
    }

    @Override
    public void execute(String fileName) {
        ftpService.downloadFilesFromHistory(fileName);
    }
}
