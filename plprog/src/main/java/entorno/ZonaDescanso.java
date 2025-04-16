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
public class ZonaDescanso {
    public synchronized void entrar(Humano h) throws InterruptedException {
        SistemaDeLog.get().log(h.getIdHumano() + " entra en la zona de descanso.");
        Thread.sleep((int)(Math.random() * 2000) + 2000);
    }

    public synchronized void recuperarse(Humano h) throws InterruptedException {
        SistemaDeLog.get().log(h.getIdHumano() + " se recupera de sus heridas en la zona de descanso.");
        Thread.sleep((int)(Math.random() * 2000) + 3000);
    }
}
