package poo.plprog;

import java.util.ArrayList;
import java.util.Random;

public class zriesgo {
    private int id;
    private ArrayList<String> zactuales;
    public ArrayList<String> hactuales;
    private Random random;

    public zriesgo(int id) {
        this.id = id;
        this.zactuales = new ArrayList<>();
        this.hactuales = new ArrayList<>();
        this.random = new Random();
    }

    public void merodear(zombis z) throws InterruptedException {
        if (!hactuales.isEmpty()) {
            System.out.println("Zombi " + z.getid() + " merodea en la zona " + id + "...");
            z.atacar(hactuales);
        } else {
            System.out.println("Zombi " + z.getid() + " merodea por la zona " + id + " buscando víctimas...");
        }

        int tiempoEsperaMerodeo = 2000 + random.nextInt(1000);  // 2000ms a 3000ms
        Thread.sleep(tiempoEsperaMerodeo);
    }

    public void mostrarZombis() {
        System.out.println("Zombis en zona " + id + ":");
        for (String z : zactuales) {
            System.out.println("- " + z);
        }
    }
}
