# Herramienta de Sincronización FTP

## Descripción general
Aplicación Java que implementa un servicio de sincronización de archivos con un servidor FTP, con monitorización de directorio y encriptación AES.

## Estructura del proyecto

### Patrones de diseño implementados
- **Patrón Command**: Utilizado para encapsular las operaciones de archivo (subir, descargar, modificar, eliminar)
- **Patrón Singleton**: Implementado para recursos compartidos como configuración, servicio FTP y entrada de teclado
- **Patrón Observer**: Para la monitorización del directorio mediante Java WatchService

### Componentes principales
1. **Eventos.java**: Maneja los eventos de cambio en archivos utilizando el patrón Command
2. **FileListenerServices.java**: Monitoriza el directorio local utilizando WatchService
3. **FTPService.java**: Gestiona todas las operaciones FTP y aplica el patrón Singleton
4. **AESManager.java**: Proporciona encriptación y desencriptación AES para transferencias seguras
5. **Config.java**: Almacena parámetros de configuración utilizando el patrón Singleton
6. **Command (interfaz)**: Define el comportamiento básico para todos los comandos
  - **UploadFile.java**: Implementa la subida de archivos nuevos
  - **ModifyFile.java**: Implementa la subida de archivos modificados
  - **DeleteFile.java**: Implementa el borrado de archivos
  - **DownloadFile.java**: Implementa la descarga de archivos
  - **DownloadFileFromHistory.java**: Implementa la descarga desde el historial

### Tecnologías utilizadas
- Java 8+
- Apache Commons Net para funcionalidad FTP
- Java NIO para monitoreo de directorios (WatchService)
- Encriptación AES con SHA-1 para generación de claves

### Flujo de la aplicación
1. La aplicación se inicia en Main.java
2. Establece conexión con el servidor FTP
3. Comienza a monitorizar el directorio especificado
4. Responde automáticamente a eventos del sistema de archivos
5. Permite descargar archivos bajo demanda
