/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestor;

import modelo.Zombi;

/**
 *
 * @author Rodri
 */
public class GestorZombis {
    public void iniciarZombiInicial() {
        Zombi pacienteCero = new Zombi(0);
        pacienteCero.start();
    }
}
