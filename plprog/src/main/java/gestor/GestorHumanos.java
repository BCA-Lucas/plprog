/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestor;

import control.ControlGlobal;
import entorno.Refugio;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Humano;

/**
 *
 * @author Rodri
 */
public class GestorHumanos {
    private final Refugio refugio;
    private final AtomicInteger contador = new AtomicInteger(0);

    public GestorHumanos(Refugio refugio) {
        this.refugio = refugio;
    }

    public void iniciarGeneracionHumanos() {
        new Thread(() -> {
            while (contador.get() < 10000) {
                try {
                    ControlGlobal.esperarSiPausado();
                    Humano h = new Humano(contador.incrementAndGet(), refugio);
                    h.start();
                    try {
                        Thread.sleep((int)(Math.random() * 1500) + 500);
                    } catch (InterruptedException ignored) {}
                } catch (InterruptedException ex) {
                    Logger.getLogger(GestorHumanos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
}
