package poo.plprog;

import gestor.GestorZombis;
import gestor.GestorHumanos;
import entorno.Refugio;
import control.ControlGlobal;
import entorno.SistemaDeLog;
import interfaz.ventanaPrincipal;
import rmi.servidor.ServidorRMI;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;

public class Plprog {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // Levanta el servidor de nombres RMI
            ServidorRMI servidor = new ServidorRMI();
            Naming.rebind("ServidorApocalipsis", servidor);
            System.out.println("[ServidorRMI] Servidor registrado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SistemaDeLog.get().init();
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            ventanaPrincipal ventana = new ventanaPrincipal();
            ventana.setVisible(true);
        });

        Refugio refugio = new Refugio();
        GestorHumanos gestorHumanos = new GestorHumanos(refugio);
        GestorZombis gestorZombis = new GestorZombis();

        ControlGlobal.iniciar();

        gestorZombis.iniciarZombiInicial();
        gestorHumanos.iniciarGeneracionHumanos();
    }
}
