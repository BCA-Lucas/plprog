/*
 * Clase que implementa un sistema de log básico para registrar eventos en un archivo de texto.
 * Se asegura de que solo exista una instancia (patrón Singleton) y permite agregar mensajes con marcas de tiempo.
 */
package entorno;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SistemaDeLog {
    // Instancia única de la clase (Singleton)
    private static final SistemaDeLog instancia = new SistemaDeLog();

    // Formato para las fechas y horas que se escriben en el log
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Archivo de log donde se escribirán los mensajes
    private File logFile;

    /**
     * Constructor privado para evitar la creación de múltiples instancias.
     * Se utiliza solo internamente dentro de la clase.
     */
    private SistemaDeLog() {
    }

    /**
     * Método para obtener la instancia única del sistema de log.
     * 
     * @return Instancia compartida de SistemaDeLog.
     */
    public static SistemaDeLog get() {
        return instancia;
    }

    /**
     * Inicializa el archivo de log buscando un nombre disponible entre 1000 posibles archivos.
     * Se usará el primer archivo que no exista en el sistema.
     */
    public void init() {
        for (int i = 0; i < 1000; i++) {
            File f = new File(String.format("apocalipsis%03d.txt", i)); // Ejemplo: apocalipsis001.txt
            if (!f.exists()) {
                logFile = f;
                break;
            }
        }
    }

    /**
     * Método sincronizado para registrar un mensaje en el archivo de log.
     * Si el archivo no ha sido inicializado, no se hace nada.
     * 
     * @param mensaje Texto que se desea registrar, acompañado de la hora actual.
     */
    public synchronized void log(String mensaje) {
        if (logFile == null) return; // Si no hay archivo, se ignora el mensaje

        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            out.printf("[%s] %s%n", sdf.format(new Date()), mensaje); // Se escribe el mensaje con marca de tiempo
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error en consola en caso de fallo
        }
    }
}
