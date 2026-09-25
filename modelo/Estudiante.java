package modelo;

import excepciones.DatosInvalidosException;
import java.io.Serializable;

public class Estudiante implements Serializable{
    private static final long serialVersionUID = 1L;
    String legajo;
    String nombre;
    
    public Estudiante(String legajo, String nombre) throws DatosInvalidosException{
        if(legajo == null || legajo.isBlank()) {
            throw new DatosInvalidosException("El legajo del estudiante no puede estar vacío.");
        }
        if(nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre del estudiante no puede estar vacío.");
        }
        this.legajo = legajo;
        this.nombre = nombre;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public String getLegajo(){
        return this.legajo;
    }
}