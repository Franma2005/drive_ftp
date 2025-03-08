package org.example.command;

import org.example.FTPService;

public class UploadFile implements Command{

    private FTPService ftpService;

    public UploadFile() {
        ftpService = FTPService.getInstance();
    }

    @Override
    public void execute(String fileName) {
        System.out.println("Upload");
        ftpService.uploadFiles(fileName, false);
    }
}
