package entorno;

import interfaz.VentanaPrincipal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import modelo.Humano;

/**
 * Clase que representa la zona común del refugio, donde los humanos pueden pasar un tiempo de forma temporal.
 * Utiliza una lista segura para múltiples hilos para manejar a los humanos presentes.
 */
public class ZonaComun {
    // Lista segura para hilos que contiene a los humanos actualmente en la zona común
    private static final List<Humano> presentes = new CopyOnWriteArrayList<>();

    /**
     * Método que permite a un humano entrar a la zona común por un tiempo determinado.
     * Se registra su entrada y salida, y se actualiza la interfaz gráfica.
     * 
     * @param h El humano que entra a la zona común.
     * @throws InterruptedException Si el hilo es interrumpido durante el tiempo de espera.
     */
    public void entrar(Humano h) throws InterruptedException {
        presentes.add(h); // El humano entra en la zona común
        SistemaDeLog.get().log(h.getIdHumano() + " entra en la zona común.");
        actualizarUI(); // Actualiza la interfaz gráfica

        // El humano permanece en la zona durante 1 a 2 segundos
        Thread.sleep((int)(Math.random() * 1000) + 1000);

        presentes.remove(h); // El humano sale de la zona
        actualizarUI(); // Se actualiza nuevamente la interfaz
    }

    /**
     * Actualiza la visualización de la zona común en la interfaz con los IDs de los presentes.
     */
    private void actualizarUI() {
        VentanaPrincipal.actualizarZonaComun(formatearIds(presentes));
    }

    /**
     * Devuelve una cadena con los IDs de los humanos formateados, separando en líneas cada 4 elementos.
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
     * Devuelve una representación en texto de todos los IDs presentes, en formato de lista.
     * (Este método no es usado en el resto de la clase, pero puede servir para depuración).
     */
    private String idsPresentes() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < presentes.size(); i++) {
            sb.append(presentes.get(i).getIdHumano());
            if (i < presentes.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Devuelve la cantidad de humanos presentes actualmente en la zona común.
     */
    public static int getPresentes() {
        return presentes.size();
    }
}
