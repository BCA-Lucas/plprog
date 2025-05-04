package poo.plprog;

import gestor.GestorZombis;
import gestor.GestorHumanos;
import entorno.Refugio;
import control.ControlGlobal;
import entorno.SistemaDeLog;
import interfaz.VentanaPrincipal;
import rmi.servidor.ServidorRMI;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;

/**
 * Clase principal que inicia toda la aplicación.
 * Configura el servidor RMI, lanza la interfaz gráfica,
 * inicializa los sistemas principales del entorno, y da comienzo a la simulación.
 */
public class Plprog {

    public static void main(String[] args) {
        try {
            // Inicia el registro RMI en el puerto 1099
            LocateRegistry.createRegistry(1099);

            // Crea e instancia el servidor RMI
            ServidorRMI servidor = new ServidorRMI();
            Naming.rebind("ServidorApocalipsis", servidor); // Registra el servidor con nombre lógico

            System.out.println("[ServidorRMI] Servidor registrado correctamente");

        } catch (Exception e) {
            e.printStackTrace(); // Si ocurre un error al iniciar RMI, lo muestra
        }

        // Inicializa el sistema de log, que generará un archivo de texto con los eventos
        SistemaDeLog.get().init();

        // Lanza la interfaz gráfica (Swing) en el hilo correspondiente
        javax.swing.SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });

        // Crea las instancias principales del sistema
        Refugio refugio = new Refugio();
        GestorHumanos gestorHumanos = new GestorHumanos(refugio);
        GestorZombis gestorZombis = new GestorZombis();

        // Inicia el sistema de control global (sin pausas)
        ControlGlobal.iniciar();

        // Inicia el primer zombi (paciente cero)
        gestorZombis.iniciarZombiInicial();

        // Comienza la generación automática de humanos
        gestorHumanos.iniciarGeneracionHumanos();
    }
}
