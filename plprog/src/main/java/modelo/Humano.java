package modelo;

import control.ControlGlobal;
import entorno.*;
import interfaz.VentanaPrincipal;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Clase que representa un humano dentro del sistema. Cada humano se comporta como un hilo independiente,
 * realizando ciclos de acciones: ingresar al refugio, salir al exterior y regresar.
 * Puede ser atacado por zombis y transformarse si muere.
 */
public class Humano extends Thread {
    private final String id; // Identificador único del humano (ej. H0001)
    private final Refugio refugio; // Referencia al refugio para acceder a las zonas
    private final AtomicBoolean vivo = new AtomicBoolean(true); // Estado de vida del humano (seguro para hilos)
    private volatile boolean marcado = false; // Indica si fue herido por un zombi pero sobrevivió
    private volatile boolean enAtaque = false; // Indica si está siendo atacado en este momento
    private int comidaRecolectada = 0; // Cantidad de comida que ha recolectado en el exterior
    private Túnel tunelActual; // Túnel por el que se está moviendo

    /**
     * Constructor del humano. Genera su ID formateado y asocia el refugio.
     */
    public Humano(int id, Refugio refugio) {
        this.id = String.format("H%04d", id); // Ej: H0001, H0023
        this.refugio = refugio;
    }

    /**
     * Método sincronizado que se ejecuta cuando un zombi ataca a este humano.
     * Puede resultar herido o muerto, y si muere se convierte en un nuevo zombi.
     * 
     * @param z Zombi atacante.
     * @return true si el ataque fue procesado correctamente.
     */
    public synchronized boolean recibirAtaque(Zombi z) {
        if (!vivo.get()) return false; // Si ya está muerto, no se procesa el ataque

        enAtaque = true;

        try {
            Thread.sleep((int)(Math.random() * 1000) + 500); // Tiempo que tarda el ataque
        } catch (InterruptedException e) {
            enAtaque = false;
            return false;
        }

        if (Math.random() < 2.0 / 3) {
            // Sobrevive, pero queda marcado
            marcar();
            SistemaDeLog.get().log("El zombi " + z.getIdZombi() + " ha atacado pero la víctima " + id + " ha sobrevivido.");
        } else {
            // Muere y se convierte en zombi
            vivo.set(false);
            Vivos.humanosVivos.remove(id);
            SistemaDeLog.get().log("El zombi " + z.getIdZombi() + " ha matado a " + id + " Muertes: " + (z.getMuertes() + 1));
            z.registrarMuerte();
            morir();
            new Zombi(Integer.parseInt(id.substring(1))).start(); // Se crea un nuevo zombi con el mismo ID numérico
        }

        enAtaque = false;
        return true;
    }

    /**
     * Indica si el humano está siendo atacado actualmente.
     */
    public boolean estaSiendoAtacado() {
        return enAtaque;
    }

    /**
     * Método principal del hilo humano. Ejecuta su ciclo de vida mientras esté vivo.
     * Incluye entrar al refugio, salir al exterior y volver.
     */
    @Override
    public void run() {
        Vivos.humanosVivos.put(id, this); // Se registra en la lista global de humanos vivos

        while (vivo.get()) {
            try {
                ControlGlobal.esperarSiPausado();
                refugio.zonaComun(this); // Entra a la zona común
                if (!vivo.get()) break;

                ControlGlobal.esperarSiPausado();
                refugio.salirAlExterior(this); // Sale al exterior a recolectar comida
                if (!vivo.get()) break;

                ControlGlobal.esperarSiPausado();
                refugio.volverAlRefugio(this); // Regresa al refugio y descansa
            } catch (InterruptedException e) {
                break; // Sale si se interrumpe
            }
        }

        // Al morir o terminar, se elimina del registro y limpia su túnel si estaba en uno
        Vivos.humanosVivos.remove(id);
        if (tunelActual != null) {
            VentanaPrincipal.limpiarTunel(tunelActual.getId());
        }
    }

    // Getters y métodos auxiliares:

    public String getIdHumano() {
        return id;
    }

    public boolean estaMarcado() {
        return marcado;
    }

    public void marcar() {
        this.marcado = true;
    }

    /**
     * Lógica de muerte: cambia el estado, interrumpe el hilo y limpia la UI si estaba en un túnel.
     */
    public void morir() {
        if (!vivo.getAndSet(false)) return;
        this.interrupt(); // Interrumpe el hilo para forzar su detención
        if (tunelActual != null) {
            VentanaPrincipal.limpiarTunel(tunelActual.getId());
        }
    }

    public void resetMarca() {
        this.marcado = false;
    }

    public void setComidaRecolectada(int cantidad) {
        this.comidaRecolectada = cantidad;
    }

    public int getComidaRecolectada() {
        return comidaRecolectada;
    }

    public void resetComida() {
        this.comidaRecolectada = 0;
    }

    public void setTunelActual(Túnel tunel) {
        this.tunelActual = tunel;
    }

    public Túnel getTunelActual() {
        return tunelActual;
    }
}
