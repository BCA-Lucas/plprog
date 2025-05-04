package entorno;

import control.ControlGlobal;
import interfaz.VentanaPrincipal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import modelo.Humano;

/**
 * Clase que representa la zona de descanso del refugio.
 * Los humanos pueden entrar aquí para descansar normalmente o recuperarse si están heridos.
 * Se utiliza una lista segura para múltiples hilos para mantener a los humanos presentes.
 */
public class ZonaDescanso {
    // Lista de humanos presentes en la zona de descanso (estructura segura para entornos concurrentes)
    private static final List<Humano> presentes = new CopyOnWriteArrayList<>();

    /**
     * Permite a un humano entrar a la zona de descanso por un tiempo determinado.
     * 
     * @param h Humano que desea descansar.
     * @throws InterruptedException si el hilo es interrumpido durante la espera.
     */
    public void entrar(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        presentes.add(h); // El humano entra a descansar
        SistemaDeLog.get().log(h.getIdHumano() + " entra en la zona de descanso.");
        actualizarUI();

        ControlGlobal.esperarSiPausado();
        Thread.sleep((int)(Math.random() * 2000) + 2000); // Descanso entre 2 y 4 segundos
        ControlGlobal.esperarSiPausado();

        presentes.remove(h); // El humano sale de la zona de descanso
        actualizarUI();
    }

    /**
     * Permite a un humano herido recuperarse en la zona de descanso.
     * Además de descansar, se le quita la marca de herido.
     * 
     * @param h Humano herido que se recupera.
     * @throws InterruptedException si el hilo es interrumpido durante la espera.
     */
    public void recuperarse(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        presentes.add(h);
        SistemaDeLog.get().log(h.getIdHumano() + " se recupera de sus heridas en la zona de descanso.");
        h.resetMarca(); // Se le quita la marca de herido
        actualizarUI();

        ControlGlobal.esperarSiPausado();
        Thread.sleep((int)(Math.random() * 2000) + 3000); // Tiempo de recuperación entre 3 y 5 segundos
        ControlGlobal.esperarSiPausado();

        presentes.remove(h); // Sale de la zona de descanso
        actualizarUI();
    }

    /**
     * Actualiza la interfaz gráfica con la lista de humanos presentes en la zona de descanso.
     */
    private void actualizarUI() {
        VentanaPrincipal.actualizarZonaDescanso(formatearIds(presentes));
    }

    /**
     * Formatea los IDs de los humanos presentes para mostrarlos en la interfaz.
     * Cada 4 IDs agrega un salto de línea para mayor claridad visual.
     */
    private String formatearIds(List<Humano> lista) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            sb.append(lista.get(i).getIdHumano());
            if ((i + 1) % 4 == 0) sb.append("\n");
            else sb.append(", ");
        }
        return sb.toString().trim();
    }

    /**
     * Devuelve la cantidad de humanos presentes actualmente en la zona de descanso.
     * 
     * @return Número de humanos presentes.
     */
    public static int getPresentes() {
        return presentes.size();
    }
}
