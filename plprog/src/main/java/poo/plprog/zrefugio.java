/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.plprog;

import java.util.concurrent.Semaphore;

public class zrefugio {
    private String nombre;
    private Semaphore capacidad;

    public zrefugio(String nombre, int cupo) {
        this.nombre = nombre;
        this.capacidad = new Semaphore(cupo);
    }

    public void entrar(String humanoId) {
        try {
            System.out.println(humanoId + " quiere entrar a " + nombre + "...");
            capacidad.acquire();  // Espera si no hay lugar
            System.out.println(humanoId + " entra a " + nombre + " ✅");
        } catch (InterruptedException e) {
            System.out.println(humanoId + " fue interrumpido al intentar entrar a " + nombre);
        }
    }

    public void salir(String humanoId) {
        capacidad.release();  // Libera un lugar
        System.out.println(humanoId + " sale de " + nombre + " 🏃");
    }

    public String getNombre() {
        return nombre;
    }
}