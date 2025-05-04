/*
 * Esta clase representa el comedor dentro del entorno del sistema.
 * Gestiona la comida disponible y la presencia de objetos de tipo Humano que intentan alimentarse.
 */
package entorno;

import interfaz.VentanaPrincipal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import modelo.Humano;

/**
 * Clase Comedor que simula un entorno compartido donde humanos pueden comer si hay comida disponible.
 * La clase también actualiza la interfaz gráfica y lleva registro de los humanos presentes.
 */
public class Comedor {
    // Variable que almacena la cantidad actual de comida disponible en el comedor.
    private int comidaDisponible = 0;

    // Lista compartida y segura para múltiples hilos que almacena los humanos presentes en el comedor.
    private final static List<Humano> presentes = new CopyOnWriteArrayList<>();

    /**
     * Método sincronizado que permite añadir comida al comedor.
     * También notifica a los hilos que podrían estar esperando para comer.
     * 
     * @param cantidad La cantidad de comida a depositar.
     */
    public synchronized void depositarComida(int cantidad) {
        comidaDisponible += cantidad; // Aumenta la comida disponible
        VentanaPrincipal.actualizarComida(comidaDisponible); // Actualiza la UI
        notifyAll(); // Notifica a los hilos que podrían estar esperando comida
    }

    /**
     * Método que simula el proceso de un humano que llega al comedor y come si hay comida disponible.
     * Utiliza sincronización para asegurar el acceso seguro a la comida.
     * 
     * @param h El humano que intenta comer.
     * @throws InterruptedException Si el hilo es interrumpido mientras espera comida.
     */
    public void comer(Humano h) throws InterruptedException {
        presentes.add(h); // Se registra que el humano ha llegado al comedor
        actualizarUI();   // Se actualiza la interfaz gráfica con los presentes

        synchronized (this) {
            // Espera hasta que haya al menos una unidad de comida disponible
            while (comidaDisponible < 1) {
                wait();
            }
            comidaDisponible--; // El humano toma una unidad de comida
            VentanaPrincipal.actualizarComida(comidaDisponible); // Actualiza la UI
        }

        // Simula el tiempo que tarda en comer (entre 3 y 5 segundos)
        Thread.sleep((int)(Math.random() * 2000) + 3000);
        
        // Registra en el log que el humano ha comido
        SistemaDeLog.get().log(h.getIdHumano() + " ha comido en el comedor.");
        
        // El humano se retira del comedor y se actualiza la UI
        presentes.remove(h);
        actualizarUI();
    }

    /**
     * Método auxiliar que actualiza la interfaz con la lista de humanos presentes.
     */
    private void actualizarUI() {
        VentanaPrincipal.actualizarComedor(formatearIds(presentes));
    }

    /**
     * Convierte la lista de humanos presentes en una cadena de texto formateada.
     * Cada 4 humanos, se inserta un salto de línea.
     * 
     * @param lista Lista de humanos presentes.
     * @return Cadena con los IDs formateados.
     */
    private String formatearIds(List<Humano> lista) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            sb.append(lista.get(i).getIdHumano());
            if ((i + 1) % 4 == 0) sb.append("\n"); // Salto de línea cada 4 IDs
            else sb.append(", ");
        }
        return sb.toString().trim();
    }

    /**
     * Retorna la cantidad actual de comida disponible.
     * 
     * @return Cantidad de comida.
     */
    public int getComida(){
        return comidaDisponible;
    }

    /**
     * Retorna la cantidad de humanos actualmente presentes en el comedor.
     * 
     * @return Número de humanos presentes.
     */
    public static int getPresentes(){
        return presentes.size();
    }
}
