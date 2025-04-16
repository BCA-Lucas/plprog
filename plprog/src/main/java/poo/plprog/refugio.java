/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.plprog;

/**
 *
 * @author Lucasbe
 */
public class refugio {
    public zrefugio descanso;
    public zrefugio comedor;
    public zrefugio zonaComun;

    public refugio() {
        descanso = new ZonaRefugio("Zona de Descanso", 3);
        comedor = new ZonaRefugio("Comedor", 2);
        zonaComun = new ZonaRefugio("Zona Común", 4);
    }
}
