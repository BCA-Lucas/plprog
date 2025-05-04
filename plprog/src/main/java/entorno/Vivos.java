package entorno;

import java.util.concurrent.ConcurrentHashMap;
import modelo.Humano;

/**
 * Clase que mantiene un registro global y concurrente de los humanos vivos en el sistema.
 * Utiliza una estructura de datos segura para múltiples hilos.
 */
public class Vivos {
    // Mapa concurrente que almacena los humanos vivos, indexados por su ID (tipo String)
    public static final ConcurrentHashMap<String, Humano> humanosVivos = new ConcurrentHashMap<>();
}
