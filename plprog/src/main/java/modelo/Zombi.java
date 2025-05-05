package modelo;

import entorno.ZonaInsegura;
import control.ControlGlobal;
import entorno.SistemaDeLog;

/**
 * Clase que representa un zombi dentro del sistema. Cada zombi actúa como un hilo independiente
 * que se desplaza constantemente entre zonas inseguras y ataca a humanos cuando los encuentra.
 */
public class Zombi extends Thread {
    private final String id;      // ID único del zombi (ej. Z0001)
    private int muertes = 0;      // Contador de humanos eliminados por este zombi
    private int zonaActual = -1;  // Zona insegura en la que estuvo por última vez

    /**
     * Constructor del zombi, recibe un número entero y lo convierte en ID formateado.
     */
    public Zombi(int id) {
        this.id = String.format("Z%04d", id); // Ej: Z0000, Z0003
    }

    /**
     * Método principal del hilo. El zombi recorre zonas inseguras aleatorias constantemente
     * y trata de atacar humanos presentes. El ciclo continúa hasta que el hilo sea interrumpido.
     */
    @Override
    public void run() {
        while (true) {
            try {
                ControlGlobal.esperarSiPausado(); // Respeta el sistema global de pausas

                // Selecciona una zona insegura aleatoria distinta a la anterior
                ZonaInsegura zona = ZonaInsegura.getDistintaAleatoria(zonaActual);
                zonaActual = zona.getId();

                ControlGlobal.esperarSiPausado();
                // Registra el cambio de zona en el log
                SistemaDeLog.get().log(this.getIdZombi() + " se cambia a la zona insegura " + zonaActual);
                zona.entrarZombi(this); // Entra a la zona para intentar atacar

                

            } catch (InterruptedException e) {
                break; // Si se interrumpe el hilo, termina su ejecución
            }
        }
    }

    /**
     * Método para registrar que este zombi ha matado a un humano.
     */
    public void registrarMuerte() {
        muertes++;
    }

    /**
     * Devuelve el número total de muertes causadas por este zombi.
     */
    public int getMuertes() {
        return muertes;
    }

    /**
     * Devuelve el ID único de este zombi.
     */
    public String getIdZombi() {
        return id;
    }
}
