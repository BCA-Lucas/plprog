/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entorno;

/**
 *
 * @author Rodri
 */
import interfaz.ventanaPrincipal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import modelo.Humano;
import modelo.Zombi;

public class ZonaInsegura {
    private static final ZonaInsegura[] zonas = new ZonaInsegura[4];

    static {
        for (int i = 0; i < 4; i++) zonas[i] = new ZonaInsegura(i);
    }

    public static ZonaInsegura get(int id) { return zonas[id]; }

    public static ZonaInsegura getDistintaAleatoria(int actual) {
        List<ZonaInsegura> otras = new ArrayList<>();
        for (ZonaInsegura z : zonas) {
            if (z.id != actual) otras.add(z);
        }
        return otras.get(new Random().nextInt(otras.size()));
    }

    private final int id;
    private final Set<String> humanosPresentes = ConcurrentHashMap.newKeySet();
    private final Set<String> zombisPresentes = ConcurrentHashMap.newKeySet();
    private final Semaphore accesoZona = new Semaphore(1);

    private ZonaInsegura(int id) { this.id = id; }

    public int getId() { return id; }

    public void entrarHumano(Humano h) throws InterruptedException {
        if (!Vivos.humanosVivos.containsKey(h.getIdHumano())) return;
        humanosPresentes.add(h.getIdHumano());
        actualizarUI();
        Thread.sleep((int)(Math.random() * 2000) + 3000);
        if (Vivos.humanosVivos.containsKey(h.getIdHumano()) && !h.estaMarcado()) {
            h.setComidaRecolectada(2);
            SistemaDeLog.get().log(h.getIdHumano() + " ha recolectado 2 unidades de comida.");
        }
        humanosPresentes.remove(h.getIdHumano());
        actualizarUI();
    }

    public void entrarZombi(Zombi z) throws InterruptedException {
        zombisPresentes.add(z.getIdZombi());
        actualizarUI();

        boolean ataco = false;
        accesoZona.acquire();
        try {
            for (Humano h : Vivos.humanosVivos.values()) {
                if (humanosPresentes.contains(h.getIdHumano())) {
                    synchronized (h) {
                        if (!Vivos.humanosVivos.containsKey(h.getIdHumano())) continue;

                        Thread.sleep((int)(Math.random() * 1000) + 500);
                        if (Math.random() < 2.0 / 3) {
                            h.marcar();
                            SistemaDeLog.get().log("El zombi " + z.getIdZombi() + " ha atacado pero la víctima " + h.getIdHumano() + " ha sobrevivido.");
                        } else {
                            Vivos.humanosVivos.remove(h.getIdHumano());
                            humanosPresentes.remove(h.getIdHumano());
                            h.morir();
                            SistemaDeLog.get().log("El zombi " + z.getIdZombi() + " ha matado a " + h.getIdHumano() + " Muertes: " + (z.getMuertes() + 1));
                            z.registrarMuerte();
                            new Zombi(Integer.parseInt(h.getIdHumano().substring(1))).start();
                        }
                        ataco = true;
                        break;
                    }
                }
            }
        } finally {
            accesoZona.release();
        }

        if (!ataco) Thread.sleep((int)(Math.random() * 1000) + 2000);

        zombisPresentes.remove(z.getIdZombi());
        actualizarUI();
    }

    private void actualizarUI() {
        ventanaPrincipal.actualizarZonaInseguraHumanos(id, formatearIds(humanosPresentes));
        ventanaPrincipal.actualizarZonaInseguraZombis(id, formatearIds(zombisPresentes));
    }

    private String formatearIds(Collection<String> lista) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String id : lista) {
            sb.append(id);
            if (++i % 4 == 0) sb.append("\n");
            else sb.append(", ");
        }
        return sb.toString().trim();
    }
}
