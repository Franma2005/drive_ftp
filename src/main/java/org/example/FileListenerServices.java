package org.example;

import org.example.entities.Config;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.nio.file.WatchEvent.Kind;

public class FileListenerServices extends Thread{

    // Nos permite observar los cambios que se producen en un directorio
    private WatchService watcher;
    // Guarda los eventos que envía el sistema operativo en un objeto.
    private WatchKey key;
    // Archivo de configuración del programa
    private Config config;

    public FileListenerServices() {
        initializeService();
    }

    // Se inicializan las clases
    public void initializeService() {
        config = Config.getInstance();
        try {
            watcher = FileSystems.getDefault().newWatchService();
            config.getFILENAME().
                    register(
                            watcher,
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_DELETE,
                            StandardWatchEventKinds.ENTRY_MODIFY
                    );
        } catch(IOException exception) {
            System.out.println("Error, no se puede observar el directorio");
        }
    }

    public void observateEvent() {
        while(true) {
            try {
                key = watcher.take();

                List<WatchEvent<?>> events = key.pollEvents();

                for (WatchEvent<?> event : events) {
                    // Obtenemos el tipo de evento
                    Kind<?> eventType = event.kind();

                    // Obtenemos el nombre del archivo (Devuelve un objeto pero lo casteamos a tipo Path)
                    Path fileName = (Path) event.context();

                    if(StandardWatchEventKinds.OVERFLOW == eventType)
                        continue;
                    else
                        new Eventos(eventType, fileName).start();
                }

                boolean valid = key.reset();

            } catch (InterruptedException exception) {
                System.out.println("Se a interrumpido la monitorización del directorio");
            }
        }
    }

}
