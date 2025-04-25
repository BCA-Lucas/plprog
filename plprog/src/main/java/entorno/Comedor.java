/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entorno;

import interfaz.ventanaPrincipal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import modelo.Humano;

/**
 *
 * @author Rodri
 */
public class Comedor {
    private int comidaDisponible = 0;
    private final List<Humano> presentes = new CopyOnWriteArrayList<>();

    public synchronized void depositarComida(int cantidad) {
        comidaDisponible += cantidad;
        ventanaPrincipal.actualizarComida(comidaDisponible);
        notifyAll();
    }

    public void comer(Humano h) throws InterruptedException {
        presentes.add(h);
        actualizarUI();

        synchronized (this) {
            while (comidaDisponible < 1) {
                wait();
            }
            comidaDisponible--;
            ventanaPrincipal.actualizarComida(comidaDisponible);
        }
        

        Thread.sleep((int)(Math.random() * 2000) + 3000);
        SistemaDeLog.get().log(h.getIdHumano() + " ha comido en el comedor.");
        presentes.remove(h);
        actualizarUI();
    }

    private void actualizarUI() {
        ventanaPrincipal.actualizarComedor(formatearIds(presentes));
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
    
    public int getComida(){
        return comidaDisponible;
    }
    
    public int getPresentes(){
        return presentes.size();
    }
}