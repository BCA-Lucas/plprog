package rmi.servidor;

import entorno.*;
import modelo.Zombi;
import control.ControlGlobal;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Implementación del servidor RMI que expone métodos remotos
 * para consultar el estado de la simulación y controlar su ejecución.
 * Extiende UnicastRemoteObject para poder ser accedido de forma remota.
 */
public class ServidorRMI extends UnicastRemoteObject implements ServidorRemoto {

    /**
     * Constructor necesario para RMI. Llama al constructor de la superclase.
     */
    public ServidorRMI() throws RemoteException {
        super();
    }

    /**
     * Devuelve el total de humanos actualmente dentro del refugio,
     * sumando los presentes en la zona común, comedor y zona de descanso.
     */
    @Override
    public int getHumanosRefugio() {
        return ZonaComun.getPresentes() + Comedor.getPresentes() + ZonaDescanso.getPresentes();
    }

    /**
     * Devuelve un mapa con el número de humanos esperando en cada túnel.
     * La clave es el ID del túnel y el valor es la cantidad de humanos.
     */
    @Override
    public Map<Integer, Integer> getHumanosTuneles() {
        return Refugio.getHumanosEnTuneles();
    }

    /**
     * Devuelve un mapa con el número de humanos en cada zona insegura.
     */
    @Override
    public Map<Integer, Integer> getHumanosZonasInseguras() {
        Map<Integer, Integer> resultado = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            resultado.put(i, ZonaInsegura.get(i).getHumanos());
        }
        return resultado;
    }

    /**
     * Devuelve un mapa con el número de zombis en cada zona insegura.
     */
    @Override
    public Map<Integer, Integer> getZombisZonasInseguras() {
        Map<Integer, Integer> resultado = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            resultado.put(i, ZonaInsegura.get(i).getZombis());
        }
        return resultado;
    }

    /**
     * Devuelve una lista con los 3 zombis que han causado más muertes,
     * en formato: "Z0003 (5 muertes)", ordenados de mayor a menor.
     */
    @Override
    public List<String> getTop3ZombisLetales() {
        List<Zombi> zombis = new ArrayList<>(ZonaInsegura.getZombisLista());

        // Ordena por cantidad de muertes de forma descendente
        zombis.sort(Comparator.comparingInt(Zombi::getMuertes).reversed());

        List<String> top3 = new ArrayList<>();
        for (int i = 0; i < Math.min(3, zombis.size()); i++) {
            top3.add(zombis.get(i).getIdZombi() + " (" + zombis.get(i).getMuertes() + " muertes)");
        }
        return top3;
    }

    /**
     * Método remoto que permite pausar toda la simulación desde un cliente RMI.
     */
    @Override
    public void pausar() {
        ControlGlobal.pausar();
    }

    /**
     * Método remoto que permite reanudar la simulación si está pausada.
     */
    @Override
    public void reanudar() {
        ControlGlobal.reanudar();
    }
}
