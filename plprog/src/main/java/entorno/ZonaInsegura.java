package entorno;

import control.ControlGlobal;
import interfaz.VentanaPrincipal;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import modelo.Humano;
import modelo.Zombi;

/**
 * Clase que representa una zona insegura fuera del refugio.
 * Los humanos pueden explorarla para recolectar comida, mientras los zombis la recorren intentando atacarlos.
 * Hay un total de 4 zonas inseguras, accesibles a través de túneles.
 */
public class ZonaInsegura {
    // Arreglo estático con las 4 instancias fijas de zonas inseguras
    private static final ZonaInsegura[] zonas = new ZonaInsegura[4];

    // Inicialización estática de las zonas inseguras
    static {
        for (int i = 0; i < 4; i++) zonas[i] = new ZonaInsegura(i);
    }

    /**
     * Devuelve la zona insegura con el ID especificado (0 a 3).
     */
    public static ZonaInsegura get(int id) {
        return zonas[id];
    }

    /**
     * Devuelve una zona insegura aleatoria distinta a la actual.
     * Útil para que zombis se desplacen entre zonas.
     */
    public static ZonaInsegura getDistintaAleatoria(int actual) {
        List<ZonaInsegura> otras = new ArrayList<>();
        for (ZonaInsegura z : zonas) {
            if (z.id != actual) otras.add(z);
        }
        return otras.get(new Random().nextInt(otras.size()));
    }

    private final int id; // Identificador de la zona
    private final Queue<Humano> humanos = new ConcurrentLinkedQueue<>(); // Humanos actualmente en la zona
    private final Queue<Zombi> zombis = new ConcurrentLinkedQueue<>();   // Zombis actualmente en la zona

    /**
     * Constructor privado para evitar creación externa de zonas.
     */
    private ZonaInsegura(int id) {
        this.id = id;
    }

    /**
     * Devuelve el identificador de esta zona.
     */
    public int getId() {
        return id;
    }

    /**
     * Método que permite a un humano entrar a esta zona para explorar y recolectar comida.
     * Se registran eventos y se verifica si fue atacado antes de recolectar.
     */
    public void entrarHumano(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        if (!Vivos.humanosVivos.containsKey(h.getIdHumano())) return;

        humanos.add(h);
        actualizarUI();
        Thread.sleep((int)(Math.random() * 2000) + 3000); // Tiempo de exploración

        // Espera a que termine cualquier ataque que esté recibiendo
        while (h.estaSiendoAtacado()) Thread.sleep(100);

        ControlGlobal.esperarSiPausado();

        // Solo si sigue vivo y no está herido, recolecta comida
        if (Vivos.humanosVivos.containsKey(h.getIdHumano()) && !h.estaMarcado()) {
            h.setComidaRecolectada(2);
            SistemaDeLog.get().log(h.getIdHumano() + " ha recolectado 2 unidades de comida.");
        }

        ControlGlobal.esperarSiPausado();
        humanos.remove(h); // Sale de la zona
        actualizarUI();
    }

    /**
     * Método que permite a un zombi entrar en esta zona e intentar atacar a un humano.
     * Solo puede atacar una vez por entrada.
     */
    public void entrarZombi(Zombi z) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        zombis.add(z);
        actualizarUI();

        boolean ataco = false;
        List<Humano> candidatos;

        // Se hace una copia segura de los humanos presentes
        synchronized (this) {
            candidatos = new ArrayList<>(humanos);
        }

        for (Humano h : candidatos) {
            synchronized (h) {
                // Verifica que el humano sigue vivo
                if (Vivos.humanosVivos.containsKey(h.getIdHumano())) {
                    ControlGlobal.esperarSiPausado();
                    humanos.remove(h); // Se remueve para evitar ataques múltiples
                    h.recibirAtaque(z); // Aplica el ataque
                    ataco = true;
                    break; // Solo un ataque por entrada
                }
            }
        }

        // Si no atacó, espera un tiempo simulado antes de salir
        if (!ataco) Thread.sleep((int)(Math.random() * 1000) + 2000);

        ControlGlobal.esperarSiPausado();
        zombis.remove(z); // El zombi se retira de la zona
        actualizarUI();
    }

    /**
     * Actualiza la interfaz gráfica mostrando los IDs de humanos y zombis presentes.
     */
    private void actualizarUI() {
        VentanaPrincipal.actualizarZonaInseguraHumanos(id, formatearIds(humanos));
        VentanaPrincipal.actualizarZonaInseguraZombis(id, formatearIds(zombis));
    }

    /**
     * Formatea una colección de objetos (humanos o zombis) a una cadena de texto de sus IDs.
     * Agrega un salto de línea cada 4 elementos para mayor claridad.
     */
    private <T extends Thread> String formatearIds(Collection<T> lista) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (T t : lista) {
            String id = (t instanceof Humano) ? ((Humano)t).getIdHumano() : ((Zombi)t).getIdZombi();
            sb.append(id);
            if (++i % 4 == 0) sb.append("\n");
            else sb.append(", ");
        }
        return sb.toString().trim();
    }

    /**
     * Devuelve la cantidad de humanos presentes en esta zona insegura.
     */
    public int getHumanos() {
        return humanos.size();
    }

    /**
     * Devuelve la cantidad de zombis presentes en esta zona insegura.
     */
    public int getZombis() {
        return zombis.size();
    }

    /**
     * Devuelve una lista combinada con todos los zombis de todas las zonas inseguras.
     */
    public static List<Zombi> getZombisLista() {
        List<Zombi> zombisLista = new ArrayList<>();
        for (ZonaInsegura zona : zonas) {
            zombisLista.addAll(zona.zombis);
        }
        return zombisLista;
    }
}
