package entorno;

import control.ControlGlobal;
import interfaz.VentanaPrincipal;
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
        ControlGlobal.esperarSiPausado();
        esperaInterior.add(h);
        actualizarUI();
        SistemaDeLog.get().log(h.getIdHumano() + " espera para formar grupo en el túnel " + id);
        try {
            barrera.await();
        } catch (BrokenBarrierException e) {
            barrera.reset();
            Thread.currentThread().interrupt();
        }
        ControlGlobal.esperarSiPausado();
        esperaInterior.remove(h);
        actualizarUI();
        SistemaDeLog.get().log(h.getIdHumano() + " forma parte de un grupo en el túnel " + id);
    }

    public void atravesar(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        // Espera mientras haya humanos intentando volver desde el exterior
        synchronized (lockDireccion) {
            while (direccionActual != Direccion.NINGUNA || !esperaExterior.isEmpty()) {
                lockDireccion.wait();
            }
            direccionActual = Direccion.SALIDA;
        }

        sem.acquire();
        h.setTunelActual(this);
        VentanaPrincipal.mostrarHumanoEnTunel(id, h.getIdHumano());
        SistemaDeLog.get().log(h.getIdHumano() + " cruza el túnel " + id);
        ControlGlobal.esperarSiPausado();
        try {
            Thread.sleep(1000);
        } finally {
            ControlGlobal.esperarSiPausado();
            VentanaPrincipal.limpiarTunel(id);
            sem.release();
            synchronized (lockDireccion) {
                direccionActual = Direccion.NINGUNA;
                lockDireccion.notifyAll();
            }
        }
    }

    public void entrarDesdeExterior(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        esperaExterior.add(h);
        actualizarUI();

        // Verifica inmediatamente si ha muerto antes de esperar
        if (!Vivos.humanosVivos.containsKey(h.getIdHumano())) {
            ControlGlobal.esperarSiPausado();
            esperaExterior.remove(h);
            actualizarUI();
            return;
        }
        ControlGlobal.esperarSiPausado();
        synchronized (lockDireccion) {
            while (direccionActual != Direccion.NINGUNA) {
                lockDireccion.wait();
            }
            ControlGlobal.esperarSiPausado();
            // Verifica justo antes de adquirir el túnel por si murió mientras esperaba
            if (!Vivos.humanosVivos.containsKey(h.getIdHumano())) {
                ControlGlobal.esperarSiPausado();
                esperaExterior.remove(h);
                actualizarUI();
                return;
            }

            direccionActual = Direccion.ENTRADA;
        }

        sem.acquire();

        ControlGlobal.esperarSiPausado();
        esperaExterior.remove(h);
        actualizarUI();
        h.setTunelActual(this);
        ControlGlobal.esperarSiPausado();
        VentanaPrincipal.mostrarHumanoEnTunel(id, h.getIdHumano());
        SistemaDeLog.get().log(h.getIdHumano() + " vuelve por el túnel " + id);
        ControlGlobal.esperarSiPausado();
        try {
            ControlGlobal.esperarSiPausado();
            Thread.sleep(1000);
        } finally {
            ControlGlobal.esperarSiPausado();
            VentanaPrincipal.limpiarTunel(id);
            sem.release();
            synchronized (lockDireccion) {
                direccionActual = Direccion.NINGUNA;
                lockDireccion.notifyAll();
            }
        }
    }
    public int getId() { return id; }

    private void actualizarUI() {
        VentanaPrincipal.actualizarTunelIzquierda(id, formatear(esperaExterior));
        VentanaPrincipal.actualizarTunelDerecha(id, formatear(esperaInterior));
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
    
    public int getEsperaInteriorSize() {
        return esperaInterior.size();
    }

    public int getEsperaExteriorSize() {
        return esperaExterior.size();
    }

}


