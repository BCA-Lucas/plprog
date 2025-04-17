package entorno;

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

    public Túnel(int id) {
        this.id = id;
        this.barrera = new CyclicBarrier(3);
    }

    public int getId() {
        return id; 
    }

    public void entrarGrupo(Humano h) throws InterruptedException {
        SistemaDeLog.get().log(h.getIdHumano() + " espera para formar grupo en el túnel " + id);
        try {
            barrera.await();
        } catch (BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
        SistemaDeLog.get().log(h.getIdHumano() + " forma parte de un grupo en el túnel " + id);
    }

    public void atravesar(Humano h) throws InterruptedException {
        sem.acquire();
        SistemaDeLog.get().log(h.getIdHumano() + " cruza el túnel " + id);
        Thread.sleep(1000);
        sem.release();
    }

    public void entrarDesdeExterior(Humano h) throws InterruptedException {
        sem.acquire();
        SistemaDeLog.get().log(h.getIdHumano() + " vuelve por el túnel " + id);
        Thread.sleep(1000);
        sem.release();
    }
}