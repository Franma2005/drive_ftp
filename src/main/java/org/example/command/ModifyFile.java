package org.example.command;

import org.example.FTPService;

public class ModifyFile implements Command {

    private FTPService ftpService;

    public ModifyFile() {
        ftpService = FTPService.getInstance();
    }


    @Override
    public void execute(String fileName) {
        System.out.println("Hola");
        ftpService.uploadFiles(fileName, true);
    }
}
