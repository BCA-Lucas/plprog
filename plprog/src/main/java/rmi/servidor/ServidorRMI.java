package rmi.servidor;

import entorno.*;
import modelo.Zombi;
import control.ControlGlobal;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServidorRMI extends UnicastRemoteObject implements ServidorRemoto {

    public ServidorRMI() throws RemoteException {
        super();
    }

    @Override
    public int getHumanosRefugio() {
        return entorno.ZonaComun.getPresentes() + entorno.Comedor.getPresentes() + entorno.ZonaDescanso.getPresentes();
    }

    @Override
    public Map<Integer, Integer> getHumanosTuneles() {
        return Refugio.getHumanosEnTuneles();
    }

    @Override
    public Map<Integer, Integer> getHumanosZonasInseguras() {
        Map<Integer, Integer> resultado = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            resultado.put(i, ZonaInsegura.get(i).getHumanos());
        }
        return resultado;
    }

    @Override
    public Map<Integer, Integer> getZombisZonasInseguras() {
        Map<Integer, Integer> resultado = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            resultado.put(i, ZonaInsegura.get(i).getZombis());
        }
        return resultado;
    }

    @Override
    public List<String> getTop3ZombisLetales() {
        List<Zombi> zombis = new ArrayList<>(entorno.ZonaInsegura.getZombisLista());
        zombis.sort(Comparator.comparingInt(Zombi::getMuertes).reversed());

        List<String> top3 = new ArrayList<>();
        for (int i = 0; i < Math.min(3, zombis.size()); i++) {
            top3.add(zombis.get(i).getIdZombi() + " (" + zombis.get(i).getMuertes() + " muertes)");
        }
        return top3;
    }

    @Override
    public void pausar() {
        ControlGlobal.pausar();
    }

    @Override
    public void reanudar() {
        ControlGlobal.reanudar();
    }
}
