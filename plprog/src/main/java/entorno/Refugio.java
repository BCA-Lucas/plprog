/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entorno;

import java.util.*;
import modelo.Humano;


/**
 *
 * @author Lucasbe
 */
public class Refugio {
    private final ZonaComun zonaComun = new ZonaComun();
    private final ZonaDescanso zonaDescanso = new ZonaDescanso();
    private final Comedor comedor = new Comedor();
    public static Refugio instanciaGlobal;
    private final Túnel[] túneles = {
        new Túnel(0, 3),
        new Túnel(1, 3),
        new Túnel(2, 3),
        new Túnel(3, 3)
    };
    
    public Refugio() {
        instanciaGlobal = this;
    }


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
    
    public static Map<Integer, Integer> getHumanosEnTuneles() {
        Map<Integer, Integer> resultado = new HashMap<>();
        for (int i = 0; i < instanciaGlobal.túneles.length; i++) {
            Túnel tunel = instanciaGlobal.túneles[i];
            int humanosEsperando = tunel.getEsperaInteriorSize() + tunel.getEsperaExteriorSize();
            resultado.put(i, humanosEsperando);
        }
        return resultado;
    }
}
