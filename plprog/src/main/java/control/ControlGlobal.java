package control;

/**
 * Clase utilizada para controlar el estado global de pausa y reanudación en una aplicación.
 * Útil cuando varios hilos deben coordinarse para detenerse o continuar su ejecución.
 */
public class ControlGlobal {
    // Variable compartida que indica si el sistema está en pausa.
    // La palabra clave 'volatile' garantiza que los cambios sean visibles entre hilos.
    private static volatile boolean pausado = false;

    /**
     * Método sincronizado que activa el modo pausa.
     * Establece la variable 'pausado' en true.
     */
    public static synchronized void pausar() {
        pausado = true;
    }

    /**
     * Método sincronizado que desactiva el modo pausa.
     * Establece la variable 'pausado' en false y notifica a todos los hilos que estén esperando.
     */
    public static synchronized void reanudar() {
        pausado = false;
        ControlGlobal.class.notifyAll(); // Notifica a todos los hilos que estaban esperando sobre este monitor
    }

    /**
     * Método que bloquea el hilo que lo llama si el sistema está en pausa.
     * Se utiliza en hilos que deben detenerse temporalmente hasta que se reanude el sistema.
     * 
     * @throws InterruptedException si el hilo es interrumpido mientras espera.
     */
    public static synchronized void esperarSiPausado() throws InterruptedException {
        while (pausado) {
            ControlGlobal.class.wait(); // El hilo espera mientras 'pausado' sea true
        }
    }

    /**
     * Método que simplemente inicia el sistema desactivando cualquier pausa.
     * Similar a reanudar pero sin notificar a los hilos.
     */
    public static void iniciar() {
        pausado = false;
    }
    
    /**
     * Método sleep que respeta las pausas.
     * Similar a reanudar pero sin notificar a los hilos.
     */
    public static void sleepInterrumpible(long tiempoTotal) throws InterruptedException {
        final long tick = 50; // milisegundos
        long dormido = 0;

        while (dormido < tiempoTotal) {
            esperarSiPausado(); // espera si está pausado

            long restante = tiempoTotal - dormido;
            long dormirAhora = Math.min(tick, restante);

            try {
                Thread.sleep(dormirAhora);
                dormido += dormirAhora;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // mantiene buena práctica
                break;
            }
        }
    }

}
