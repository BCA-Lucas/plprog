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
    private final Semaphore sem = new Semaphore(1);
    private final CyclicBarrier barrera;
    private final List<Humano> esperaInterior = Collections.synchronizedList(new ArrayList<>());
    private final List<Humano> esperaExterior = Collections.synchronizedList(new ArrayList<>());

    private enum Direccion { ENTRADA, SALIDA, NINGUNA }
    private Direccion direccionActual = Direccion.NINGUNA;
    private final Object lockDireccion = new Object();

    public Túnel(int id, int grupo) {
        this.id = id;
        this.barrera = new CyclicBarrier(grupo);
    }

    public void entrarGrupo(Humano h) throws InterruptedException {
        esperaInterior.add(h);
        actualizarUI();
        SistemaDeLog.get().log(h.getIdHumano() + " espera para formar grupo en el túnel " + id);
        try {
            barrera.await();
        } catch (BrokenBarrierException e) {
            barrera.reset();
            Thread.currentThread().interrupt();
        }
        esperaInterior.remove(h);
        actualizarUI();
        SistemaDeLog.get().log(h.getIdHumano() + " forma parte de un grupo en el túnel " + id);
    }

    public void atravesar(Humano h) throws InterruptedException {
        // Espera mientras haya humanos intentando volver desde el exterior
        synchronized (lockDireccion) {
            while (direccionActual != Direccion.NINGUNA || !esperaExterior.isEmpty()) {
                lockDireccion.wait();
            }
            direccionActual = Direccion.SALIDA;
        }

        sem.acquire();
        h.setTunelActual(this);
        ventanaPrincipal.mostrarHumanoEnTunel(id, h.getIdHumano());
        SistemaDeLog.get().log(h.getIdHumano() + " cruza el túnel " + id);
        try {
            Thread.sleep(1000);
        } finally {
            ventanaPrincipal.limpiarTunel(id);
            sem.release();
            synchronized (lockDireccion) {
                direccionActual = Direccion.NINGUNA;
                lockDireccion.notifyAll();
            }
        }
    }

    public void entrarDesdeExterior(Humano h) throws InterruptedException {
        esperaExterior.add(h);
        actualizarUI();

        // Verifica inmediatamente si ha muerto antes de esperar
        if (!Vivos.humanosVivos.containsKey(h.getIdHumano())) {
            esperaExterior.remove(h);
            actualizarUI();
            return;
        }

        synchronized (lockDireccion) {
            while (direccionActual != Direccion.NINGUNA) {
                lockDireccion.wait();
            }

            // Verifica justo antes de adquirir el túnel por si murió mientras esperaba
            if (!Vivos.humanosVivos.containsKey(h.getIdHumano())) {
                esperaExterior.remove(h);
                actualizarUI();
                return;
            }

            direccionActual = Direccion.ENTRADA;
        }

        sem.acquire();

        esperaExterior.remove(h);
        actualizarUI();
        h.setTunelActual(this);
        ventanaPrincipal.mostrarHumanoEnTunel(id, h.getIdHumano());
        SistemaDeLog.get().log(h.getIdHumano() + " vuelve por el túnel " + id);

        try {
            Thread.sleep(1000);
        } finally {
            ventanaPrincipal.limpiarTunel(id);
            sem.release();
            synchronized (lockDireccion) {
                direccionActual = Direccion.NINGUNA;
                lockDireccion.notifyAll();
            }
        }
    }
    public int getId() { return id; }

    private void actualizarUI() {
        ventanaPrincipal.actualizarTunelIzquierda(id, formatear(esperaExterior));
        ventanaPrincipal.actualizarTunelDerecha(id, formatear(esperaInterior));
    }

    private String formatear(List<Humano> lista) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Humano h : lista) {
            sb.append(h.getIdHumano());
            if (++i % 4 == 0) sb.append("\n");
            else sb.append(", ");
        }
        return sb.toString().trim();
    }
}
