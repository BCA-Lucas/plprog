package entorno;

import control.ControlGlobal;
import interfaz.VentanaPrincipal;
import java.util.*;
import java.util.concurrent.*;
import modelo.Humano;

/**
 * Clase que representa un túnel por el cual los humanos pueden salir al exterior o volver al refugio.
 * Utiliza mecanismos de sincronización para garantizar el acceso ordenado y seguro en un entorno multihilo.
 */
public class Túnel {
    private final int id; // Identificador único del túnel

    // Semáforo que permite un solo humano a la vez dentro del túnel
    private final Semaphore sem = new Semaphore(1);

    // Barrera cíclica para formar grupos de humanos que cruzan juntos al exterior
    private final CyclicBarrier barrera;

    // Listas sincronizadas para humanos que esperan dentro o fuera del túnel
    private final List<Humano> esperaInterior = Collections.synchronizedList(new ArrayList<>());
    private final List<Humano> esperaExterior = Collections.synchronizedList(new ArrayList<>());

    // Direcciones posibles del uso del túnel
    private enum Direccion { ENTRADA, SALIDA, NINGUNA }
    private Direccion direccionActual = Direccion.NINGUNA;

    // Objeto utilizado como lock para controlar el acceso a la dirección del túnel
    private final Object lockDireccion = new Object();

    /**
     * Constructor del túnel. Recibe su ID y el tamaño del grupo que debe formarse para salir.
     */
    public Túnel(int id, int grupo) {
        this.id = id;
        this.barrera = new CyclicBarrier(grupo);
    }

    /**
     * Método que permite a un humano esperar para formar un grupo antes de salir al exterior.
     * El humano se bloquea hasta que el grupo esté completo.
     */
    public void entrarGrupo(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        esperaInterior.add(h);
        actualizarUI();
        SistemaDeLog.get().log(h.getIdHumano() + " espera para formar grupo en el túnel " + id);

        try {
            barrera.await(); // Espera a que se forme el grupo completo
        } catch (BrokenBarrierException e) {
            barrera.reset();
            Thread.currentThread().interrupt();
        }

        ControlGlobal.esperarSiPausado();
        esperaInterior.remove(h);
        actualizarUI();
        SistemaDeLog.get().log(h.getIdHumano() + " forma parte de un grupo en el túnel " + id);
    }

    /**
     * Permite a un humano atravesar el túnel hacia el exterior.
     * Usa sincronización para evitar colisiones de dirección y un semáforo para controlar el acceso exclusivo.
     */
    public void atravesar(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();

        synchronized (lockDireccion) {
            // Espera si el túnel ya está siendo usado en otra dirección o hay humanos por entrar
            while (direccionActual != Direccion.NINGUNA || !esperaExterior.isEmpty()) {
                lockDireccion.wait();
            }
            direccionActual = Direccion.SALIDA;
        }

        sem.acquire(); // Obtiene acceso exclusivo al túnel
        h.setTunelActual(this);
        VentanaPrincipal.mostrarHumanoEnTunel(id, h.getIdHumano());
        SistemaDeLog.get().log(h.getIdHumano() + " cruza el túnel " + id);
        ControlGlobal.esperarSiPausado();

        try {
            ControlGlobal.sleepInterrumpible(1000);// Simula el tiempo de cruce
        } finally {
            ControlGlobal.esperarSiPausado();
            VentanaPrincipal.limpiarTunel(id);
            sem.release();
            synchronized (lockDireccion) {
                direccionActual = Direccion.NINGUNA;
                lockDireccion.notifyAll(); // Notifica a los otros hilos esperando
            }
        }
    }

    /**
     * Permite a un humano volver desde el exterior hacia el refugio, pasando por el túnel.
     * Controla el acceso, verifica si el humano sigue vivo, y simula el cruce.
     */
    public void entrarDesdeExterior(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        esperaExterior.add(h);
        actualizarUI();

        // Verifica si el humano sigue vivo antes de esperar
        if (!Vivos.humanosVivos.containsKey(h.getIdHumano())) {
            ControlGlobal.esperarSiPausado();
            esperaExterior.remove(h);
            actualizarUI();
            return;
        }

        ControlGlobal.esperarSiPausado();
        synchronized (lockDireccion) {
            // Espera hasta que el túnel esté libre
            while (direccionActual != Direccion.NINGUNA) {
                lockDireccion.wait();
            }

            ControlGlobal.esperarSiPausado();
            // Verifica nuevamente si el humano murió mientras esperaba
            if (!Vivos.humanosVivos.containsKey(h.getIdHumano())) {
                ControlGlobal.esperarSiPausado();
                esperaExterior.remove(h);
                actualizarUI();
                return;
            }

            direccionActual = Direccion.ENTRADA;
        }

        sem.acquire(); // Obtiene acceso exclusivo al túnel

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
            ControlGlobal.sleepInterrumpible(1000);// Simula el tiempo de cruce
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

    /**
     * Devuelve el identificador del túnel.
     */
    public int getId() {
        return id;
    }

    /**
     * Actualiza la interfaz gráfica con los humanos esperando en cada extremo del túnel.
     */
    private void actualizarUI() {
        VentanaPrincipal.actualizarTunelIzquierda(id, formatear(esperaExterior));
        VentanaPrincipal.actualizarTunelDerecha(id, formatear(esperaInterior));
    }

    /**
     * Formatea una lista de humanos en una cadena de texto, insertando saltos de línea cada 4 elementos.
     */
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

    /**
     * Devuelve la cantidad de humanos esperando dentro del refugio para salir.
     */
    public int getEsperaInteriorSize() {
        return esperaInterior.size();
    }

    /**
     * Devuelve la cantidad de humanos esperando fuera del refugio para volver a entrar.
     */
    public int getEsperaExteriorSize() {
        return esperaExterior.size();
    }
}
