package control;

/**
 *
 * @author Rodri
 */
public class ControlGlobal {
    private static volatile boolean pausado = false;

    public static synchronized void pausar() {
        pausado = true;
    }

    public static synchronized void reanudar() {
        pausado = false;
        ControlGlobal.class.notifyAll();
    }

    public static synchronized void esperarSiPausado() throws InterruptedException {
        while (pausado) {
            ControlGlobal.class.wait();
        }
    }

    public static void iniciar() {
        pausado = false;
    }
}
