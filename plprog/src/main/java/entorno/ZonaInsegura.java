/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entorno;

/**
 *
 * @author Rodri
 */
import interfaz.VentanaPrincipal;
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
        if (!Vivos.humanosVivos.containsKey(h.getIdHumano())) return;
        humanos.add(h);
        actualizarUI();
        Thread.sleep((int)(Math.random() * 2000) + 3000);

        while (h.estaSiendoAtacado()) Thread.sleep(100); // espera si está en ataque

        if (Vivos.humanosVivos.containsKey(h.getIdHumano()) && !h.estaMarcado()) {
            h.setComidaRecolectada(2);
            SistemaDeLog.get().log(h.getIdHumano() + " ha recolectado 2 unidades de comida.");
        }
        humanos.remove(h);
        actualizarUI();
    }

    public void entrarZombi(Zombi z) throws InterruptedException {
        zombis.add(z);
        actualizarUI();

        boolean ataco = false;
        List<Humano> candidatos;

        synchronized (this) {
            candidatos = new ArrayList<>(humanos);
        }

        for (Humano h : candidatos) {
            synchronized (h) {
                if (Vivos.humanosVivos.containsKey(h.getIdHumano())) {
                    humanos.remove(h); // evitar ataques múltiples
                    h.recibirAtaque(z);
                    ataco = true;
                    break;
                }
            }
        }

        if (!ataco) Thread.sleep((int)(Math.random() * 1000) + 2000);

        zombis.remove(z);
        actualizarUI();
    }
    
    private void actualizarUI() {
        VentanaPrincipal.actualizarZonaInseguraHumanos(id, formatearIds(humanos));
        VentanaPrincipal.actualizarZonaInseguraZombis(id, formatearIds(zombis));
    }

    private <T extends Thread> String formatearIds(Collection<T> lista) {
        // Convierte una lista de humanos o zombis a un string de IDs con saltos de línea cada 4 elementos
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
    
    public int getHumanos(){
        return humanos.size();
    }
    
    public int getZombis(){
        return zombis.size();
    }
    
    public static List<Zombi> getZombisLista() {
        List<Zombi> zombisLista = new ArrayList<>();

        for (ZonaInsegura zona : zonas) {
            zombisLista.addAll(zona.zombis);
        }
        
        return zombisLista;
    }
}