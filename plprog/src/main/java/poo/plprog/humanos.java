package poo.plprog;

import java.util.Random;

public class humanos extends Thread {
    private String id;
    private refugio refugio;
    private tunel[] tuneles;
    private zriesgo[] zonasRiesgo;
    private boolean herido = false;
    private Random random = new Random();
    private int comidaRecolectada = 0;

    public humanos(String id, Refugio refugio, Túnel[] tuneles, Zriesgo[] zonasRiesgo) {
        this.id = id;
        this.refugio = refugio;
        this.tuneles = tuneles;
        this.zonasRiesgo = zonasRiesgo;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 1. ZONA COMÚN
                refugio.zonaComun.entrar(id);
                dormirEntre(1000, 2000);
                refugio.zonaComun.salir(id);

                // 2. Selección de túnel
                int indexTúnel = random.nextInt(tuneles.length);
                tunel tunel = tuneles[indexTúnel];
                tunel.unirseAlGrupo(this);

                // 3. Acceder a zona de riesgo
                zriesgo zona = zonasRiesgo[indexTúnel];
                zona.agregarHumano(id);  // registrar humano en zona
                int tiempoExterior = random.nextInt(2000) + 3000;

                // ¿Ataque?
                if (zona.hayZombi()) {
                    boolean sobrevives = zona.simularAtaque(id);
                    if (!sobrevives) {
                        System.out.println(id + " ha muerto en la zona " + zona.getId());
                        zona.transformarEnZombi(id);
                        break;  // fin de vida como humano
                    } else {
                        herido = true;
                        comidaRecolectada = 0;
                        System.out.println(id + " fue herido por un zombi y vuelve al refugio sin comida.");
                    }
                } else {
                    // 4. Recolectar comida (2 piezas)
                    dormirEntre(tiempoExterior, tiempoExterior + 1000);
                    comidaRecolectada = 2;
                    System.out.println(id + " recolectó 2 piezas de comida 🍎");
                }

                zona.removerHumano(id); // salir de la zona

                // 5. Volver al refugio
                tunel.regresar(this);
                refugio.depositarComida(comidaRecolectada);
                comidaRecolectada = 0;

                // 6. ZONA DE DESCANSO
                refugio.descanso.entrar(id);
                dormirEntre(2000, 4000);
                refugio.descanso.salir(id);

                // 7. COMEDOR
                refugio.comedor.entrar(id);
                while (!refugio.tomarComida(id)) {
                    Thread.sleep(1000); // Espera ordenada
                }
                dormirEntre(3000, 5000); // Comer
                refugio.comedor.salir(id);

                // 8. Si está herido, vuelve a descansar
                if (herido) {
                    refugio.descanso.entrar(id);
                    dormirEntre(3000, 5000);
                    refugio.descanso.salir(id);
                    herido = false;
                }

                // Y se repite el ciclo automáticamente

            } catch (InterruptedException e) {
                System.out.println(id + " fue interrumpido.");
            }
        }
    }

    private void dormirEntre(int min, int max) throws InterruptedException {
        int tiempo = min + random.nextInt(max - min + 1);
        Thread.sleep(tiempo);
    }

    public String getIdHumano() {
        return id;
    }
}