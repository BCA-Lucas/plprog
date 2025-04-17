/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fase1;

/**
 *
 * @author Rodri
 */
import entorno.Refugio;
import javax.swing.*;

public class ComidaUpdater {
    public static void iniciar(JLabel label, Refugio refugio) {
        Timer timer = new Timer(1000, e -> {
            int comida = refugio.getComedor().getCantidadDisponible();
            label.setText("" + comida);
        });
        timer.start();
    }
}
