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
public class ZonaComun {
    public synchronized void entrar(Humano h) throws InterruptedException {
        SistemaDeLog.get().log(h.getIdHumano() + " entra en la zona común.");
        Thread.sleep((int)(Math.random() * 1000) + 1000);
    }
}
