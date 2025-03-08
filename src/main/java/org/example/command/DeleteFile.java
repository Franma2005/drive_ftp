package org.example.command;

import org.example.FTPService;

public class DeleteFile implements Command {

    private FTPService ftpService;

    public DeleteFile() {
        ftpService = FTPService.getInstance();
    }

    @Override
    public void execute(String fileName) {
        ftpService.deleteFiles(fileName);
    }
}
