package entorno;

import java.util.concurrent.ConcurrentHashMap;
import modelo.Humano;


public class Muertos {
    public static final ConcurrentHashMap<String, Humano> humanosVivos = new ConcurrentHashMap<>();
}
