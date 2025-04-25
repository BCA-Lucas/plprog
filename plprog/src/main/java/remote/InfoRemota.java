// src/main/java/remote/InfoRemota.java
package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InfoRemota extends Remote {
    int getHumanosEnRefugio() throws RemoteException;
    List<Integer> getHumanosPorTunel() throws RemoteException;
    List<Integer> getHumanosPorZonaInsegura() throws RemoteException;
    List<Integer> getZombisPorZonaInsegura() throws RemoteException;
    List<String> getTopZombisLetales() throws RemoteException;
    void pausarSimulacion() throws RemoteException;
    void reanudarSimulacion() throws RemoteException;
    boolean isSimulacionPausada() throws RemoteException;
}

