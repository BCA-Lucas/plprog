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

public class ZonaDescanso {
    private final Queue<Humano> humanos = new ConcurrentLinkedQueue<>();

    public synchronized void entrar(Humano h) throws InterruptedException {
        humanos.add(h);
        SistemaDeLog.get().log(h.getIdHumano() + " entra en la zona de descanso.");
        Thread.sleep((int)(Math.random() * 2000) + 2000);
        humanos.remove(h);
    }

    public synchronized void recuperarse(Humano h) throws InterruptedException {
        humanos.add(h);
        SistemaDeLog.get().log(h.getIdHumano() + " se recupera de sus heridas en la zona de descanso.");
        Thread.sleep((int)(Math.random() * 2000) + 3000);
        humanos.remove(h);
    }

    public List<String> getHumanosPresentes() {
        return humanos.stream().map(Humano::getIdHumano).collect(Collectors.toList());
    }
}