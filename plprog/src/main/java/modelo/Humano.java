package modelo;

import entorno.Refugio;
import control.ControlGlobal;
import entorno.Vivos;
import entorno.Túnel;
import interfaz.ventanaPrincipal;
import java.util.concurrent.atomic.AtomicBoolean;

public class Humano extends Thread {
    private final String id;
    private final Refugio refugio;
    private final AtomicBoolean vivo = new AtomicBoolean(true);
    private volatile boolean marcado = false;
    private int comidaRecolectada = 0;
    private Túnel tunelActual;

    public Humano(int id, Refugio refugio) {
        this.id = String.format("H%04d", id);
        this.refugio = refugio;
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
    }

    public String getIdHumano() { return id; }
    public boolean estaMarcado() { return marcado; }
    public void marcar() { this.marcado = true; }

    public void morir() {
        if (!vivo.getAndSet(false)) return;
        this.interrupt();
        if (tunelActual != null) {
            ventanaPrincipal.limpiarTunel(tunelActual.getId());
        }
    }
    
    public void setComidaRecolectada(int cantidad) { this.comidaRecolectada = cantidad; }
    public int getComidaRecolectada() { return comidaRecolectada; }
    public void resetComida() { this.comidaRecolectada = 0; }
    public void setTunelActual(Túnel tunel) { this.tunelActual = tunel; }
    public Túnel getTunelActual() { return tunelActual; }
}