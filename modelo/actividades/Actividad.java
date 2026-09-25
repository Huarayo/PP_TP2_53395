package modelo.actividades;

import excepciones.CupoExcedidoException;
import excepciones.DatosInvalidosException;
import modelo.Estudiante;
import modelo.Inscripcion;

import java.util.List;
import java.util.ArrayList;
//paquete que agrupa todo lo relacionado con entrada/salida
import java.io.Serializable;


public abstract class Actividad implements Serializable {
    private static final long serialVerionUID = 1L;
    private int id;
    private String titulo;
    private int cupoMaximo;
    static final int CUPO_MINIMO = 5;
    
    public List<Inscripcion> inscripciones;
    
    public Actividad(int id, String titulo, int cupoMaximo) throws DatosInvalidosException {
        if (titulo == null || titulo.isBlank()) {
            throw new DatosInvalidosException("El título de la actividad no puede estar vacío.");
        }
        if(cupoMaximo <= 0) {
            throw new DatosInvalidosException("El cupo máximo debe ser mayor a 0 (actividad: " + titulo + ").");
        }
        this.id = id;
        this.titulo = titulo;
        this.cupoMaximo = cupoMaximo;
        this.inscripciones = new ArrayList<>(); //inicializar lista vacia
    }
    
   
    
    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
      
        //Validar cupo . si hay tantas inscripciones como cupo maximo
        if(inscripciones.size() >= cupoMaximo){
            //detecta y delega al que llama
            throw new CupoExcedidoException("Cupo agotado para la actividad: " + this.titulo + " (Máximo: " + this.cupoMaximo + ")");
        }
      
        Inscripcion inscripcion = new Inscripcion(estudiante, this);
        inscripciones.add(inscripcion);
        System.out.println("Nombre: " + estudiante.getNombre() + " Inscripto en : " + this.titulo);
        return inscripcion;
    }
    
    
    public void mostrarInscripciones(){
        System.out.printf("\n---- Inscripciones en: " + this.titulo + " ---");
        for(Inscripcion i: inscripciones){
            System.out.println("\n° nombre: " + i.getEstudiante().getNombre() +
                         "\n| fecha: " + i.getFecha() +
                        "\n| estado: " + i.getEstado()
            );
        }
    }
    
    public final void mostrarIdentificador() {
        System.out.println("ID: " + id + " | Título: " + titulo + " | Cupo: " + cupoMaximo);
    }
    
    
    //Taller y Charla lo implementan diferente -> metodo abstracta
    public abstract double calcularCostoMateriales();
    
    // devuelve tipo de actividad (charla o taller) -> metodo abstracto
    public abstract String getTipo();
    
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public int getCupoMaximo() {
        return cupoMaximo;
    }
    
    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    

}