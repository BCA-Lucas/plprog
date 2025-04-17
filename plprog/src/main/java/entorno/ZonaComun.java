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

public class ZonaComun {
    private final Queue<Humano> humanos = new ConcurrentLinkedQueue<>();

    public synchronized void entrar(Humano h) throws InterruptedException {
        humanos.add(h);
        SistemaDeLog.get().log(h.getIdHumano() + " entra en la zona común.");
        Thread.sleep((int)(Math.random() * 1000) + 1000);
        humanos.remove(h);
    }

    public List<String> getHumanosPresentes() {
        return humanos.stream().map(Humano::getIdHumano).collect(Collectors.toList());
    }
}