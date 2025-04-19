/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entorno;

import modelo.Humano;

/**
 *
 * @author Lucasbe
 */
public class Refugio {
    private final ZonaComun zonaComun = new ZonaComun();
    private final ZonaDescanso zonaDescanso = new ZonaDescanso();
    private final Comedor comedor = new Comedor();
    private final Túnel[] túneles = {
        new Túnel(0), new Túnel(1), new Túnel(2), new Túnel(3)
    };

    public void zonaComun(Humano h) throws InterruptedException {
        zonaComun.entrar(h);
    }

    public void salirAlExterior(Humano h) throws InterruptedException {
        Túnel tunel = seleccionarTúnel();
        h.setTunelActual(tunel);
        tunel.entrarGrupo(h);
        tunel.atravesar(h);
        ZonaInsegura.get(tunel.getId()).entrarHumano(h);
    }

    public void volverAlRefugio(Humano h) throws InterruptedException {
        Túnel tunel = h.getTunelActual();
        tunel.entrarDesdeExterior(h);
        if (!h.estaMarcado()) {
            comedor.depositarComida(h.getComidaRecolectada());
        }
        h.resetComida();
        zonaDescanso.entrar(h);
        comedor.comer(h);
        if (h.estaMarcado()) zonaDescanso.recuperarse(h);
    }

    private Túnel seleccionarTúnel() {
        return túneles[(int)(Math.random() * túneles.length)];
    }
}
