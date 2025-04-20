package poo.plprog;

import gestor.GestorZombis;
import gestor.GestorHumanos;
import entorno.Refugio;
import control.ControlGlobal;
import entorno.SistemaDeLog;
import interfaz.ventanaPrincipal;

public class Plprog {

    public static void main(String[] args) {
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
