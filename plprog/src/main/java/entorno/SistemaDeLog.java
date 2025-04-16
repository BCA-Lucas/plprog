/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entorno;

/**
 *
 * @author Rodri
 */
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SistemaDeLog {
    private static final SistemaDeLog instancia = new SistemaDeLog();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private File logFile;

    private SistemaDeLog() {}

    public static SistemaDeLog get() { return instancia; }

    public void init() {
        for (int i = 0; i < 1000; i++) {
            File f = new File(String.format("apocalipsis%03d.txt", i));
            if (!f.exists()) {
                logFile = f;
                break;
            }
        }
    }

    public synchronized void log(String mensaje) {
        if (logFile == null) return;
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            out.printf("[%s] %s%n", sdf.format(new Date()), mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
