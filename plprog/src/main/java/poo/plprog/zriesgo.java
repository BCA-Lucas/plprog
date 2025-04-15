package poo.plprog;

import java.util.ArrayList;
import java.util.Random;

public class zriesgo {
    private int id;
    private ArrayList<String> zactuales;  // Lista de zombis en la zona
    public ArrayList<String> hactuales;    // Lista de humanos en la zona
    private Random random;

    public zriesgo(int id) {
        this.id = id;
        this.zactuales = new ArrayList<>();
        this.hactuales = new ArrayList<>();
        this.random = new Random();
        
    }

    // El zombi merodea por la zona
    public void merodear() throws InterruptedException {
        if (!hactuales.isEmpty()) {
            // Si hay humanos, el zombi puede atacar
            System.out.println("Zombi merodea en la zona " + id + "...");
            atacar();
        } else {
            // Si no hay humanos, el zombi hace algo diferente
            System.out.println("Zombi merodea por la zona " + id + " buscando víctimas...");
        }
        
        // Esperar un tiempo aleatorio entre 2 y 3 segundos antes de la próxima acción
        int tiempoEsperaMerodeo = 2000 + random.nextInt(1000);  // 2000ms a 3000ms
        Thread.sleep(tiempoEsperaMerodeo);
    }

    // El zombi ataca a una víctima aleatoria si hay humanos en la zona
    public void atacar() {
        if (hactuales.isEmpty()) {
            System.out.println("No hay víctimas disponibles en la zona " + id + ".");
            return;
        }

        // Elegir una víctima aleatoria
        int indice = random.nextInt(hactuales.size());
        String victimaId = hactuales.get(indice);
        
        System.out.println("Zombi ataca a " + victimaId + " en la zona " + id + " 💀");

        // Eliminar la víctima de la lista de humanos
        hactuales.remove(indice);
        
        // Esperar entre 0.5 y 1.5 segundos antes de anunciar la muerte del zombi
        try {
            int tiempoEspera = 500 + random.nextInt(1000);  // 500ms a 1500ms
            Thread.sleep(tiempoEspera);
        } catch (InterruptedException e) {
            System.out.println("Zombi fue interrumpido durante el ataque.");
        }

        // Anunciar la muerte de la víctima y la creación de un nuevo zombi
        System.out.println("Zombi " + victimaId + " ha muerto 🪦 y ahora se convierte en un nuevo zombi.");

        // Crear un nuevo zombi con el ID de la víctima
        String nuevoIdZombi = victimaId.replaceFirst("H", "Z");
        zombis nuevoZombi = new zombis(nuevoIdZombi, this, null, null, null);  // Creando el nuevo zombi
        nuevoZombi.start();  // El nuevo zombi empieza su ciclo
    }

    // Mostrar los zombis actuales en la zona
    public void mostrarZombis() {
        System.out.println("Zombis en zona " + id + ":");
        for (String z : zactuales) {
            System.out.println("- " + z);
        }
    }
}
