/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo.plprog;

/**
 *
 * @author Lucasbe
 */
public class zombis extends Thread{
    private String id;
    private int Muertos;
    
    public zombis(String id){
        this.id=id;
        this.Muertos=0;
    }
    
    public String getid(){
        return id;
    }
    
    @Override
    public void run(){
        System.out.println("Soy el zombi "+id+" soy un pedazo de virgen");
        while(true)
    }
}
