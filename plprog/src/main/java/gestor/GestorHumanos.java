package gestor;

import control.ControlGlobal;
import entorno.Refugio;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Humano;

/**
 * Clase encargada de gestionar la generación de humanos dentro del sistema.
 * Crea hilos que representan humanos y los inicia de forma controlada y pausada.
 */
public class GestorHumanos {
    private final Refugio refugio; // Referencia al refugio donde se ubicarán los humanos
    private final AtomicInteger contador = new AtomicInteger(0); // Contador seguro para múltiples hilos

    /**
     * Constructor que recibe el refugio donde se gestionarán los humanos.
     */
    public GestorHumanos(Refugio refugio) {
        this.refugio = refugio;
    }

    /**
     * Inicia un hilo que genera humanos periódicamente, hasta un máximo de 10.000.
     * Cada humano se ejecuta como un hilo independiente y se inicializa con una pausa entre creaciones.
     */
    public void iniciarGeneracionHumanos() {
        new Thread(() -> {
            while (contador.get() < 10000) {
                try {
                    ControlGlobal.esperarSiPausado(); // Respeta el sistema de pausa global

                    // Se crea un nuevo humano con un ID único y se le pasa el refugio
                    Humano h = new Humano(contador.incrementAndGet(), refugio);
                    h.start(); // Inicia el hilo del humano

                    // Espera entre 500 ms y 2 segundos antes de crear otro humano
                    try {
                        Thread.sleep((int)(Math.random() * 1500) + 500);
                    } catch (InterruptedException ignored) {} // Ignora interrupciones menores

                } catch (InterruptedException ex) {
                    // Si ocurre una interrupción grave, se registra en el log del sistema
                    Logger.getLogger(GestorHumanos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start(); // Se lanza el hilo de generación
    }
}
