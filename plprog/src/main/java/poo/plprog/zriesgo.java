/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.plprog;

/**
 *
 * @author Lucasbe
 */
import java.util.ArrayList;
import java.util.Random;

public class zriesgo {
    private int id;
    private ArrayList<String> zactuales;
    private ArrayList<String> hactuales;

    public zriesgo(int id) {
        this.id = id;
        this.zactuales = new ArrayList<>();
    }

    public void merodear(zombis z) {
        zactuales.add(z.getid());
        if(!hactuales.isEmpty()){
            Random rand = new Random();
            int index = rand.nextInt(zactuales.size());
            //zombificar humano
        }
    }

    public void eliminarZombiPorId(String id) {
        if (zactuales.remove(id)) {
            System.out.println("Zombi con ID " + id + " eliminado.");
        } else {
            System.out.println("Zombi con ID " + id + " no encontrado.");
        }
    }

    public void mostrarZombis() {
        System.out.println("Zombis en zona " + id + ":");
        for (String z : zactuales) {
            System.out.println("- " + z);
        }
    }
}
