package org.example.entities;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    private static Config config;
    private final String SERVER = "localhost";
    private final int PORT = 21;
    private final String USER = "paco";
    private final String PASSWORD = "paco";

    private final Path DIRECTORY = Paths.get("/Users/fmandia/FTP");
    private final Path FILE = Paths.get("directorio_paco");
    private final Path BACKUP = Paths.get("history");

    private Config() {

    }

    public static Config getInstance() {
        if(config == null)
            config = new Config();
        return config;
    }

    public Path getFILENAME() {
        return FILE;
    }

    public Path getDIRECTORY() {
        return DIRECTORY;
    }

    public String getSERVER() {
        return SERVER;
    }

    public int getPORT() {
        return PORT;
    }

    public String getUSER() {
        return USER;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }
}
