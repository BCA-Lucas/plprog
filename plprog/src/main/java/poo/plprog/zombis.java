/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.plprog;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucasbe
 */
public class zombis extends Thread{
    private String id;
    private int Muertos;
    private zriesgo zr1;
    private zriesgo zr2;
    private zriesgo zr3;
    private zriesgo zr4;
    
    public zombis(String id,zriesgo zr1,zriesgo zr2,zriesgo zr3,zriesgo zr4){
        this.id=id;
        this.Muertos=0;
        this.zr1=zr1;
        this.zr2=zr2;
        this.zr3=zr3;
        this.zr4=zr4;
    }
    
    public String getid(){
        return id;
    }
    
    public void atacar(ArrayList<String> posiblesVictimas) {
        if (posiblesVictimas == null || posiblesVictimas.isEmpty()) {
            System.out.println("Zombi " + id + ": No hay víctimas disponibles.");
            return;
        }

        // Atacar una víctima aleatoria
        Random random = new Random();
        int indice = random.nextInt(posiblesVictimas.size());
        String victimaId = posiblesVictimas.get(indice);

        System.out.println("Zombi " + id + " ataca a " + victimaId + " 💀");

        // Eliminar la víctima de la lista
        posiblesVictimas.remove(indice);

        // Esperar entre 0.5 y 1.5 segundos
        try {
            int tiempoEspera = 500 + random.nextInt(1000); // 500ms a 1500ms
            Thread.sleep(tiempoEspera);
        } catch (InterruptedException e) {
            System.out.println("Zombi " + id + " fue interrumpido.");
        }

        // Anunciar muerte del zombi actual
        System.out.println("Zombi " + id + " ha muerto 🪦");

        // Transformar víctima en zombi
        String nuevoIdZombi = victimaId.replaceFirst("H", "Z");
        zombis nuevoZombi = new zombis(nuevoIdZombi,zr1,zr2,zr3,zr4);
        nuevoZombi.start();
    }
    
    public void run(){
        System.out.println("Zombi ha nacido:"+id);
        Random random = new Random();        
        while(true){
            int accion = random.nextInt(5); // genera 0, 1, 2 o 3

            switch (accion) {
                case 1 -> {
                    try {
                        zr1.merodear();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(zombis.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                case 2 -> {
                    try {
                        zr2.merodear();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(zombis.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 3 -> {
                    try {
                        zr3.merodear();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(zombis.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 4 -> {
                    try {
                        zr4.merodear();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(zombis.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }
    
    
}
