package gestor;

import modelo.Zombi;

/**
 * Clase encargada de iniciar el primer zombi del sistema, conocido como "paciente cero".
 * Este zombi comienza la propagación en la simulación.
 */
public class GestorZombis {

    /**
     * Crea e inicia al primer zombi del sistema con ID 0.
     * Se ejecuta como un hilo independiente.
     */
    public void iniciarZombiInicial() {
        Zombi pacienteCero = new Zombi(0); // Se crea el primer zombi con ID 0
        pacienteCero.start();              // Se lanza el hilo del zombi
    }
}
