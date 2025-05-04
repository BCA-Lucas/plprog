package rmi.servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Interfaz remota RMI que define los métodos disponibles para los clientes.
 * Cualquier clase que la implemente puede ser invocada de manera remota.
 */
public interface ServidorRemoto extends Remote {

    /**
     * Devuelve la cantidad total de humanos actualmente dentro del refugio.
     * 
     * @return Entero con la suma de humanos en zona común, comedor y zona de descanso.
     */
    int getHumanosRefugio() throws RemoteException;

    /**
     * Devuelve un mapa con la cantidad de humanos esperando en cada túnel.
     * 
     * @return Mapa con clave = ID del túnel, valor = cantidad de humanos.
     */
    Map<Integer, Integer> getHumanosTuneles() throws RemoteException;

    /**
     * Devuelve un mapa con la cantidad de humanos en cada zona insegura.
     * 
     * @return Mapa con clave = ID de zona insegura, valor = cantidad de humanos.
     */
    Map<Integer, Integer> getHumanosZonasInseguras() throws RemoteException;

    /**
     * Devuelve un mapa con la cantidad de zombis en cada zona insegura.
     * 
     * @return Mapa con clave = ID de zona insegura, valor = cantidad de zombis.
     */
    Map<Integer, Integer> getZombisZonasInseguras() throws RemoteException;

    /**
     * Devuelve una lista con los 3 zombis que más humanos han matado.
     * 
     * @return Lista con descripciones en formato "Z0003 (5 muertes)".
     */
    List<String> getTop3ZombisLetales() throws RemoteException;

    /**
     * Pausa la simulación global. Afecta a todos los hilos.
     */
    void pausar() throws RemoteException;

    /**
     * Reanuda la simulación si estaba pausada.
     */
    void reanudar() throws RemoteException;
}
