/*
 * Esta clase representa el núcleo del entorno de simulación, donde los humanos
 * pueden interactuar con distintas zonas como la común, de descanso, comedor y túneles.
 */
package entorno;

import control.ControlGlobal;
import java.util.*;
import modelo.Humano;

public class Refugio {
    // Instancias de las distintas zonas internas del refugio
    private final ZonaComun zonaComun = new ZonaComun();
    private final ZonaDescanso zonaDescanso = new ZonaDescanso();
    private final Comedor comedor = new Comedor();

    // Instancia global del refugio (patrón de diseño singleton simplificado)
    public static Refugio instanciaGlobal;

    // Arreglo de túneles que conectan el refugio con zonas inseguras
    private final Túnel[] túneles = {
        new Túnel(0, 3),
        new Túnel(1, 3),
        new Túnel(2, 3),
        new Túnel(3, 3)
    };

    /**
     * Constructor del refugio. Inicializa la instancia global.
     */
    public Refugio() {
        instanciaGlobal = this;
    }

    /**
     * Permite a un humano entrar en la zona común del refugio.
     * Se verifica el estado de pausa global antes de continuar.
     */
    public void zonaComun(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        zonaComun.entrar(h);
    }

    /**
     * Permite que un humano salga del refugio hacia una zona insegura usando un túnel.
     * Se respetan las pausas globales en cada paso.
     */
    public void salirAlExterior(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        Túnel tunel = seleccionarTúnel(); // Selección aleatoria del túnel
        h.setTunelActual(tunel); // Se asigna el túnel al humano
        ControlGlobal.esperarSiPausado();
        tunel.entrarGrupo(h); // El humano entra en el grupo para cruzar
        ControlGlobal.esperarSiPausado();
        tunel.atravesar(h); // Cruza el túnel
        ZonaInsegura.get(tunel.getId()).entrarHumano(h); // Llega a la zona insegura
    }

    /**
     * Permite que un humano regrese desde el exterior al refugio.
     * Puede depositar comida si no está marcado, descansar y luego comer.
     */
    public void volverAlRefugio(Humano h) throws InterruptedException {
        ControlGlobal.esperarSiPausado();
        Túnel tunel = h.getTunelActual();
        tunel.entrarDesdeExterior(h); // Entra al túnel desde el exterior

        if (!h.estaMarcado()) {
            comedor.depositarComida(h.getComidaRecolectada()); // Deja la comida recolectada
        }

        h.resetComida(); // Resetea su contador de comida
        zonaDescanso.entrar(h); // Entra a descansar
        ControlGlobal.esperarSiPausado();
        comedor.comer(h); // Luego va al comedor a comer
        ControlGlobal.esperarSiPausado();

        // Si el humano estaba marcado, se recupera
        if (h.estaMarcado()) zonaDescanso.recuperarse(h);
    }

    /**
     * Selecciona aleatoriamente uno de los túneles disponibles para salir del refugio.
     * 
     * @return Un objeto Túnel seleccionado al azar.
     */
    private Túnel seleccionarTúnel() {
        return túneles[(int)(Math.random() * túneles.length)];
    }

    /**
     * Devuelve un mapa con la cantidad de humanos que esperan en cada túnel
     * (tanto del lado interior como exterior).
     * 
     * @return Mapa con ID de túnel y cantidad de humanos esperando.
     */
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
