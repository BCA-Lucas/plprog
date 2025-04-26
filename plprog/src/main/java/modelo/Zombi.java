package modelo;

import entorno.ZonaInsegura;
import control.ControlGlobal;
import entorno.SistemaDeLog;

public class Zombi extends Thread {
    private final String id;
    private int muertes = 0;
    private int zonaActual = -1;

    public Zombi(int id) {
        this.id = String.format("Z%04d", id);
    }

    @Override
    public void run() {
        while (true) {
            try {
                ControlGlobal.esperarSiPausado();
                ZonaInsegura zona = ZonaInsegura.getDistintaAleatoria(zonaActual);
                zonaActual = zona.getId();
                ControlGlobal.esperarSiPausado();
                zona.entrarZombi(this);
                SistemaDeLog.get().log(this.getIdZombi() + " se cambia a la zona insegura " + zonaActual);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void registrarMuerte() { muertes++; }
    public int getMuertes() { return muertes; }
    public String getIdZombi() { return id; }
}
