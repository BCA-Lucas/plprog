package modelo;

import control.ControlGlobal;
import entorno.*;
import interfaz.ventanaPrincipal;
import java.util.concurrent.atomic.AtomicBoolean;

public class Humano extends Thread {
    private final String id;
    private final Refugio refugio;
    private final AtomicBoolean vivo = new AtomicBoolean(true);
    private volatile boolean marcado = false;
    private volatile boolean enAtaque = false;
    private int comidaRecolectada = 0;
    private Túnel tunelActual;

    public Humano(int id, Refugio refugio) {
        this.id = String.format("H%04d", id);
        this.refugio = refugio;
    }

    public synchronized boolean recibirAtaque(Zombi z) {
        if (!vivo.get()) return false;

        enAtaque = true;
        try {
            Thread.sleep((int)(Math.random() * 1000) + 500);
        } catch (InterruptedException e) {
            enAtaque = false;
            return false;
        }

        if (Math.random() < 2.0 / 3) {
            marcar();
            SistemaDeLog.get().log("El zombi " + z.getIdZombi() + " ha atacado pero la víctima " + id + " ha sobrevivido.");
        } else {
            vivo.set(false);
            Vivos.humanosVivos.remove(id);
            SistemaDeLog.get().log("El zombi " + z.getIdZombi() + " ha matado a " + id + " Muertes: " + (z.getMuertes() + 1));
            z.registrarMuerte();
            morir();
            new Zombi(Integer.parseInt(id.substring(1))).start();
        }
        enAtaque = false;
        return true;
    }

    public boolean estaSiendoAtacado() {
        return enAtaque;
    }

    @Override
    public void run() {
        Vivos.humanosVivos.put(id, this);
        while (vivo.get()) {
            try {
                ControlGlobal.esperarSiPausado();
                refugio.zonaComun(this);
                if (!vivo.get()) break;

                ControlGlobal.esperarSiPausado();
                refugio.salirAlExterior(this);
                if (!vivo.get()) break;

                ControlGlobal.esperarSiPausado();
                refugio.volverAlRefugio(this);
            } catch (InterruptedException e) {
                break;
            }
        }
        Vivos.humanosVivos.remove(id);
        if (tunelActual != null) ventanaPrincipal.limpiarTunel(tunelActual.getId());
    }

    public String getIdHumano() { return id; }
    public boolean estaMarcado() { return marcado; }
    public void marcar() { this.marcado = true; }

    public void morir() {
        if (!vivo.getAndSet(false)) return;
        this.interrupt();
        if (tunelActual != null) ventanaPrincipal.limpiarTunel(tunelActual.getId());
    }
    
    public void resetMarca() {
        this.marcado = false;
    }

    public void setComidaRecolectada(int cantidad) { this.comidaRecolectada = cantidad; }
    public int getComidaRecolectada() { return comidaRecolectada; }
    public void resetComida() { this.comidaRecolectada = 0; }
    public void setTunelActual(Túnel tunel) { this.tunelActual = tunel; }
    public Túnel getTunelActual() { return tunelActual; }
}
