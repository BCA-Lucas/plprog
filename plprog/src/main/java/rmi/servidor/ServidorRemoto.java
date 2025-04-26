package rmi.servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServidorRemoto extends Remote {
    int getHumanosRefugio() throws RemoteException;
    Map<Integer, Integer> getHumanosTuneles() throws RemoteException;
    Map<Integer, Integer> getHumanosZonasInseguras() throws RemoteException;
    Map<Integer, Integer> getZombisZonasInseguras() throws RemoteException;
    List<String> getTop3ZombisLetales() throws RemoteException;
    void pausar() throws RemoteException;
    void reanudar() throws RemoteException;
}
