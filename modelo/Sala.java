package modelo;

import java.io.Serializable;

public class Sala implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;
    
    public Sala (int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public int getId(){
        return this.id;
    }
    
    
}