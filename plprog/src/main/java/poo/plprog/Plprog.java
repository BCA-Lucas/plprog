package poo.plprog;

import gestor.GestorZombis;
import gestor.GestorHumanos;
import entorno.Refugio;
import control.ControlGlobal;
import entorno.SistemaDeLog;
import fase1.VentanaPrincipal;
import javax.swing.SwingUtilities;


public class Plprog {

    public static void main(String[] args) {
        SistemaDeLog.get().init();

        Refugio refugio = new Refugio();
        GestorHumanos gestorHumanos = new GestorHumanos(refugio);
        GestorZombis gestorZombis = new GestorZombis();

        ControlGlobal.iniciar();

        gestorZombis.iniciarZombiInicial();
        gestorHumanos.iniciarGeneracionHumanos();
        
        new VentanaPrincipal(refugio).setVisible(true);
    }
}
