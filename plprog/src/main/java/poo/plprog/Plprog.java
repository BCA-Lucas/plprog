package poo.plprog;

public class Plprog {

    public static void main(String[] args) {
        System.out.println("🧟‍♂️ Simulación Zombi Iniciada 🧟‍♀️");

        // Crear zonas de riesgo
        zriesgo zr1 = new zriesgo(1);
        zriesgo zr2 = new zriesgo(2);
        zriesgo zr3 = new zriesgo(3);
        zriesgo zr4 = new zriesgo(4);

        // Crear humano y añadirlo a la zona 1
        humanos h1 = new humanos(0001);
        humanos h2 = new humanos(0010);
        zr1.hactuales.add(h1.getId());
        zr1.hactuales.add(h2.getId());

        // Crear zombi inicial y lanzarlo
        zombis zomb = new zombis("Z0000", zr1, zr2, zr3, zr4);
        zomb.start();


    }
}
