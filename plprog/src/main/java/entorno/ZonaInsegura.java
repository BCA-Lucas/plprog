/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entorno;

/**
 *
 * @author Rodri
 */
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import modelo.Humano;
import modelo.Zombi;

public class ZonaInsegura {
    private static final ZonaInsegura[] zonas = new ZonaInsegura[4];

    static {
        for (int i = 0; i < 4; i++) zonas[i] = new ZonaInsegura(i);
    }
    public static ZonaInsegura get(int id) { 
        return zonas[id]; 
    }
    public static ZonaInsegura getDistintaAleatoria(int actual) {
        List<ZonaInsegura> otras = new ArrayList<>();
        for (ZonaInsegura z : zonas) {
            if (z.id != actual) otras.add(z);
        }
        return otras.get(new Random().nextInt(otras.size()));
    }
    

    private final int id;
    private final Queue<Humano> humanos = new ConcurrentLinkedQueue<>();

    private ZonaInsegura(int id) { 
        this.id = id; 
    }
    
    public int getId(){
        return id;
    }

    public void entrarHumano(Humano h) throws InterruptedException {
        humanos.add(h);
        Thread.sleep((int)(Math.random() * 2000) + 3000);
        if (!h.estaMarcado()) {
            h.setComidaRecolectada(2);
            SistemaDeLog.get().log(h.getIdHumano() + " ha recolectado 2 unidades de comida.");
        }
        humanos.remove(h);
    }

    public void entrarZombi(Zombi z) throws InterruptedException {
        Humano[] disponibles = humanos.toArray(new Humano[0]);
        boolean ataco = false;

        if (disponibles.length > 0) {
            Humano victima = disponibles[new Random().nextInt(disponibles.length)];
            Thread.sleep((int)(Math.random() * 1000) + 500);
            ataco = true;

            if (Math.random() < 2.0 / 3) {
                victima.marcar();
                SistemaDeLog.get().log("El zombi " + z.getIdZombi() + " ha atacado pero la víctima " + victima.getIdHumano() + " ha sobrevivido. Muertes: " + z.getMuertes());
            } else {
                victima.morir();
                z.registrarMuerte();
                SistemaDeLog.get().log("El zombi " + z.getIdZombi() + " ha matado a " + victima.getIdHumano() + " Muertes: " + z.getMuertes());
                new Zombi(Integer.parseInt(victima.getIdHumano().substring(1))).start();
            }
        }

        if (!ataco) {
            Thread.sleep((int)(Math.random() * 1000) + 2000); // entre 2 y 3 segundos
        }
    }
}