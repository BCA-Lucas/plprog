/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestor;

import entorno.Refugio;
import java.util.concurrent.atomic.AtomicInteger;
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
                Humano h = new Humano(contador.incrementAndGet(), refugio);
                h.start();
                try {
                    Thread.sleep((int)(Math.random() * 1500) + 500);
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }
}
