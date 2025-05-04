package rmi.cliente;

import rmi.servidor.ServidorRemoto;
import java.rmi.Naming;

/**
 * Cliente RMI que se conecta al servidor remoto "ServidorApocalipsis"
 * y consulta periódicamente el estado del sistema.
 * Este cliente actúa como monitor de la simulación.
 */
public class ClienteRMI {

    public static void main(String[] args) {
        try {
            // Busca el servidor remoto registrado con el nombre "ServidorApocalipsis"
            ServidorRemoto servidor = (ServidorRemoto) Naming.lookup("rmi://localhost/ServidorApocalipsis");

            // Bucle infinito: consulta el estado del sistema cada 5 segundos
            while (true) {
                System.out.println("--- ESTADO ---");
                System.out.println("Humanos en refugio: " + servidor.getHumanosRefugio());
                System.out.println("Humanos en túneles: " + servidor.getHumanosTuneles());
                System.out.println("Humanos en zonas inseguras: " + servidor.getHumanosZonasInseguras());
                System.out.println("Zombis en zonas inseguras: " + servidor.getZombisZonasInseguras());
                System.out.println("Top 3 zombis letales: " + servidor.getTop3ZombisLetales());
                System.out.println("---------------");

                Thread.sleep(5000); // Espera 5 segundos antes de consultar nuevamente
            }

        } catch (Exception e) {
            e.printStackTrace(); // Muestra errores de conexión o ejecución
        }
    }
}
