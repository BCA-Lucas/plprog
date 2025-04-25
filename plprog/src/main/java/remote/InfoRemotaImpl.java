// src/main/java/remote/InfoRemotaImpl.java
package remote;

import control.ControlGlobal;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.List;
import entorno.*;


public class InfoRemotaImpl extends UnicastRemoteObject implements InfoRemota {
    private final ControlGlobal control;

    public InfoRemotaImpl(ControlGlobal control) throws RemoteException {
        this.control = control;
    }

    public int getHumanosEnRefugio() throws RemoteException {
        return control.getGestorHumanos().getNumEnRefugio();
    }

    public List<Integer> getHumanosPorTunel() throws RemoteException {
        return control.getGestorHumanos().getHumanosPorTunel();
    }

    public List<Integer> getHumanosPorZonaInsegura() throws RemoteException {
        return control.getGestorHumanos().getHumanosPorZonaInsegura();
    }

    public List<Integer> getZombisPorZonaInsegura() throws RemoteException {
        return control.getGestorZombis().getZombisPorZonaInsegura();
    }

    public List<String> getTopZombisLetales() throws RemoteException {
        return control.getGestorZombis().getTop3ZombisLetales();
    }

    public void pausarSimulacion() throws RemoteException {
        control.pausarSimulacion();
    }

    public void reanudarSimulacion() throws RemoteException {
        control.reanudarSimulacion();
    }

    public boolean isSimulacionPausada() throws RemoteException {
        return control.isSimulacionPausada();
    }
    
    public int getNumEnRefugio() {
    return entorno.ZonaDescanso.getPresentes();
}

}
