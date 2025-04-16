package modelo;

import entorno.ZonaInsegura;
import control.ControlGlobal;

public class Zombi extends Thread {
    private final String id;
    private int muertes = 0;

    public Zombi(int id) {
        this.id = String.format("Z%04d", id);
    }

    @Override
    public void run() {
        while (true) {
            try {
                ControlGlobal.esperarSiPausado();
                ZonaInsegura zona = ZonaInsegura.getAleatoria();
                zona.entrarZombi(this);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void registrarMuerte() {
        muertes++; 
    }
    public int getMuertes() {
        return muertes; 
    }
    public String getIdZombi() {
        return id;
    }
}
