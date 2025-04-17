/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entorno;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import modelo.Humano;

public class Comedor {
    private int comidaDisponible = 0;
    private final Queue<Humano> humanos = new ConcurrentLinkedQueue<>();

    public synchronized void depositarComida(int cantidad) {
        comidaDisponible += cantidad;
        notifyAll();
    }

    public synchronized void comer(Humano h) throws InterruptedException {
        while (comidaDisponible < 1) {
            wait();
        }
        comidaDisponible--;
        humanos.add(h);
        Thread.sleep((int)(Math.random() * 2000) + 3000);
        SistemaDeLog.get().log(h.getIdHumano() + " ha comido en el comedor.");
        humanos.remove(h);
    }

    public synchronized int getCantidadDisponible() {
        return comidaDisponible;
    }

    public List<String> getHumanosPresentes() {
        return humanos.stream().map(Humano::getIdHumano).collect(Collectors.toList());
    }
}
