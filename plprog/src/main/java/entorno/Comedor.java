/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entorno;

import modelo.Humano;

/**
 *
 * @author Rodri
 */
public class Comedor {
    private int comidaDisponible = 0;

    public synchronized void depositarComida(int cantidad) {
        comidaDisponible += cantidad;
        notifyAll();
    }

    public synchronized void comer(Humano h) throws InterruptedException {
        while (comidaDisponible < 1) {
            wait();
        }
        comidaDisponible--;
        Thread.sleep((int)(Math.random() * 2000) + 3000);
        SistemaDeLog.get().log(h.getIdHumano() + " ha comido en el comedor.");
    }
}