package org.example;

import org.example.command.Command;
import org.example.command.DeleteFile;
import org.example.command.ModifyFile;
import org.example.command.UploadFile;

import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent.Kind;
import java.util.HashMap;
import java.util.Map;

public class Eventos extends Thread {

    private Kind<?> eventType;
    private Path fileName;
    private Path backup;
    // Necesario para el patrón command
    private Map<Kind<Path>, Command> operations;

    public Eventos(Kind<?> eventType, Path fileName) {
        this.eventType = eventType;
        this.fileName = fileName;
        operations = new HashMap<>(){{
            put(StandardWatchEventKinds.ENTRY_CREATE, new UploadFile());
            put(StandardWatchEventKinds.ENTRY_MODIFY, new ModifyFile());
            put(StandardWatchEventKinds.ENTRY_DELETE, new DeleteFile());
        }};
    }

    @Override
    public void run() {
        operations.get(eventType).execute(fileName.getFileName().toString());
    }
}
