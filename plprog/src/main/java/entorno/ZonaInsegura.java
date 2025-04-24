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
import java.util.concurrent.ConcurrentLinkedQueue;
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
    private final Queue<Humano> humanos = new ConcurrentLinkedQueue<>();
    private final Queue<Zombi> zombis = new ConcurrentLinkedQueue<>();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                

    private ZonaInsegura(int id) { this.id = id; }

    public int getId() { return id; }

    public void entrarHumano(Humano h) throws InterruptedException {
        humanos.add(h);
        actualizarUI();
        Thread.sleep((int)(Math.random() * 2000) + 3000);
        if (!h.estaMarcado() && !Muertos.humanosMuertos.containsKey(h.getIdHumano())) {
            h.setComidaRecolectada(2);
            SistemaDeLog.get().log(h.getIdHumano() + " ha recolectado 2 unidades de comida.");
        }
        humanos.remove(h);
        actualizarUI();
    }

    public void entrarZombi(Zombi z) throws InterruptedException {
        zombis.add(z);
        actualizarUI();

        Humano[] disponibles = humanos.toArray(new Humano[0]);
        boolean ataco = false;

        for (Humano h : disponibles) {
            if (!Muertos.humanosMuertos.containsKey(h.getIdHumano())) {
                ataco = true;
                Thread.sleep((int)(Math.random() * 1000) + 500);
                if (Math.random() < 2.0 / 3) {
                    h.marcar();
                    SistemaDeLog.get().log("El zombi " + z.getIdZombi() + " ha atacado pero la víctima " + h.getIdHumano() + " ha sobrevivido.");
                } else {
                    Muertos.humanosMuertos.put(h.getIdHumano(), true);
                    humanos.remove(h);
                    h.morir();
                    SistemaDeLog.get().log("El zombi " + z.getIdZombi() + " ha matado a " + h.getIdHumano() + " Muertes: " + (z.getMuertes() + 1));
                    z.registrarMuerte();
                    new Zombi(Integer.parseInt(h.getIdHumano().substring(1))).start();
                }
                break;
            }
        }

        if (!ataco) Thread.sleep((int)(Math.random() * 1000) + 2000);

        zombis.remove(z);
        actualizarUI();
    }

    private void actualizarUI() {
        ventanaPrincipal.actualizarZonaInseguraHumanos(id, formatearIds(humanos));
        ventanaPrincipal.actualizarZonaInseguraZombis(id, formatearIds(zombis));
    }

    private <T extends Thread> String formatearIds(Collection<T> lista) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (T t : lista) {
            String id = (t instanceof Humano) ? ((Humano)t).getIdHumano() : ((Zombi)t).getIdZombi();
            sb.append(id);
            if (++i % 4 == 0) sb.append("\n");
            else sb.append(", ");
        }
        return sb.toString().trim();
    }
}