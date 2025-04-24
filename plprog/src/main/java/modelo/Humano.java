package modelo;

import entorno.Refugio;
import control.ControlGlobal;
import entorno.Muertos;
import entorno.Túnel;

public class Humano extends Thread {
    private final String id;
    private final Refugio refugio;
    private boolean marcado = false;
    private volatile boolean vivo = true;
    private int comidaRecolectada = 0;
    private Túnel tunelActual;

    public Humano(int id, Refugio refugio) {
        this.id = String.format("H%04d", id);
        this.refugio = refugio;
    }

    @Override
    public void run() {
        while (vivo) {
            try {
                if (Muertos.humanosMuertos.containsKey(id)) break;
                ControlGlobal.esperarSiPausado();
                refugio.zonaComun(this);

                if (!vivo) break;
                ControlGlobal.esperarSiPausado();
                refugio.salirAlExterior(this);

                if (!vivo) break;
                ControlGlobal.esperarSiPausado();
                refugio.volverAlRefugio(this);

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public String getIdHumano() { return id; }
    public boolean estaMarcado() { return marcado; }
    public void marcar() { this.marcado = true; }

    public void morir() {
        this.vivo = false;
        this.interrupt();
    }

    public void setComidaRecolectada(int cantidad) { this.comidaRecolectada = cantidad; }
    public int getComidaRecolectada() { return comidaRecolectada; }
    public void resetComida() { this.comidaRecolectada = 0; }
    public void setTunelActual(Túnel tunel) { this.tunelActual = tunel; }
    public Túnel getTunelActual() { return tunelActual; }
}