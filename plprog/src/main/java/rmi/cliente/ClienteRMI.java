package rmi.cliente;

import rmi.servidor.ServidorRemoto;

import java.rmi.Naming;

public class ClienteRMI {

    public static void main(String[] args) {
        try {
            ServidorRemoto servidor = (ServidorRemoto) Naming.lookup("rmi://localhost/ServidorApocalipsis");

            while (true) {
                System.out.println("--- ESTADO ---");
                System.out.println("Humanos en refugio: " + servidor.getHumanosRefugio());
                System.out.println("Humanos en túneles: " + servidor.getHumanosTuneles());
                System.out.println("Humanos en zonas inseguras: " + servidor.getHumanosZonasInseguras());
                System.out.println("Zombis en zonas inseguras: " + servidor.getZombisZonasInseguras());
                System.out.println("Top 3 zombis letales: " + servidor.getTop3ZombisLetales());
                System.out.println("---------------");
                
                Thread.sleep(5000); // cada 5 segundos refresca
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
