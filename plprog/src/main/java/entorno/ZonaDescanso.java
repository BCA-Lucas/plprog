/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entorno;

import control.ControlGlobal;
import interfaz.VentanaPrincipal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import modelo.Humano;

/**
 *
 * @author Rodri
 */
public class ZonaDescanso {
    private static final List<Humano> presentes = new CopyOnWriteArrayList<>();

    public void entrar(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        presentes.add(h);
        SistemaDeLog.get().log(h.getIdHumano() + " entra en la zona de descanso.");
        actualizarUI();
        ControlGlobal.esperarSiPausado();
        Thread.sleep((int)(Math.random() * 2000) + 2000);
        ControlGlobal.esperarSiPausado();
        presentes.remove(h);
        actualizarUI();
    }

    public void recuperarse(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        presentes.add(h);
        SistemaDeLog.get().log(h.getIdHumano() + " se recupera de sus heridas en la zona de descanso.");
        h.resetMarca();
        actualizarUI();
        ControlGlobal.esperarSiPausado();
        Thread.sleep((int)(Math.random() * 2000) + 3000);
        ControlGlobal.esperarSiPausado();
        presentes.remove(h);
        actualizarUI();
    }

    private void actualizarUI() {
        VentanaPrincipal.actualizarZonaDescanso(formatearIds(presentes));
    }

    private String formatearIds(List<Humano> lista) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            sb.append(lista.get(i).getIdHumano());
            if ((i + 1) % 4 == 0) sb.append("\n");
            else sb.append(", ");
        }
        return sb.toString().trim();
    }
    
    public static int getPresentes(){
        return presentes.size();
    }
}