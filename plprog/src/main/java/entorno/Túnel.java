package entorno;

import interfaz.ventanaPrincipal;
import java.util.*;
import java.util.concurrent.*;
import modelo.Humano;

/**
 *
 * @author Rodri
 */
public class Túnel {
    private final int id;
    private final Semaphore sem = new Semaphore(1, true);
    private final CyclicBarrier barrera;

    private final Queue<Humano> esperaInterior = new ConcurrentLinkedQueue<>();
    private final Queue<Humano> esperaExterior = new ConcurrentLinkedQueue<>();

    public Túnel(int id) {
        this.id = id;
        this.barrera = new CyclicBarrier(3);
    }

    public int getId() { return id; }

    public void entrarGrupo(Humano h) throws InterruptedException {
        esperaInterior.add(h);
        actualizarUI();
        SistemaDeLog.get().log(h.getIdHumano() + " espera para formar grupo en el túnel " + id);
        try {
            barrera.await();
        } catch (BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
        esperaInterior.remove(h);
        actualizarUI();
        SistemaDeLog.get().log(h.getIdHumano() + " forma parte de un grupo en el túnel " + id);
    }

    public void atravesar(Humano h) throws InterruptedException {
        sem.acquire();
        ventanaPrincipal.mostrarHumanoEnTunel(id, h.getIdHumano());
        SistemaDeLog.get().log(h.getIdHumano() + " cruza el túnel " + id);
        Thread.sleep(1000);
        ventanaPrincipal.limpiarTunel(id);
        sem.release();
    }

    public void entrarDesdeExterior(Humano h) throws InterruptedException {
        esperaExterior.add(h);
        actualizarUI();
        sem.acquire();
        esperaExterior.remove(h);
        actualizarUI();
        ventanaPrincipal.mostrarHumanoEnTunel(id, h.getIdHumano());
        SistemaDeLog.get().log(h.getIdHumano() + " vuelve por el túnel " + id);
        Thread.sleep(1000);
        ventanaPrincipal.limpiarTunel(id);
        sem.release();
    }

    private void actualizarUI() {
        ventanaPrincipal.actualizarTunelIzquierda(id, formatearCola(esperaExterior));
        ventanaPrincipal.actualizarTunelDerecha(id, formatearCola(esperaInterior));
    }

    private String formatearCola(Queue<Humano> cola) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Humano h : cola) {
            sb.append(h.getIdHumano());
            if (++i % 4 == 0) sb.append("\n");
            else sb.append(", ");
        }
        return sb.toString().trim();
    }
}