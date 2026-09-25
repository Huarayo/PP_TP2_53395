package modelo;


import excepciones.DatosInvalidosException;
import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Taller;
import modelo.actividades.Curso;
import java.io.Serializable;

//abrir archivo físico en disco
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;

//convertir un objeto a bytes y viceversa
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;

public class EventoUniversitario implements Serializable{
    private static final long serialVersionUID = 1L;
    private final String Id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private Sala sala;
    private List <Actividad> actividades;
    public static int cantidadEventos = 0;
    
    public EventoUniversitario(String id,String titulo, double costoBase, boolean gratuito) throws DatosInvalidosException{
        if(id == null || id.isBlank() ) {
            throw new DatosInvalidosException("El id del evento no puede estar vacío");
        }
        if (titulo == null || titulo.isBlank()){
            throw new DatosInvalidosException("El título del evento no puede estar vacío");
        }
        if (costoBase < 0) {
            throw new DatosInvalidosException("El costo base no puede ser negativo (evento: " + titulo + ").");
        }
        this.Id = id;
        this.titulo=titulo;
        this.costoBase=costoBase;
        this.gratuito=gratuito;
        this.actividades = new ArrayList <>();
        this.cantidadEventos++;
    }
    
    public EventoUniversitario(EventoUniversitario otro) {
        this.Id = otro.Id;
        this.titulo = otro.titulo;
        this.costoBase = otro.costoBase;
        this.gratuito = otro.gratuito;
        this.actividades = new ArrayList <>(otro.actividades);

        this.cantidadEventos++;
    }
    
    public double calcularCostoEstimado(){
        if(gratuito){
            return 0.0;   
        }
        
        double sumaCostosMateriales = 0;
        for(Actividad act: actividades) {
            sumaCostosMateriales += act.calcularCostoMateriales();
        }
        
        return (costoBase + sumaCostosMateriales) * 1.21;
    }
    
    public void asignarSala(Sala sala){
        this.sala = sala;
        System.out.println("nombre: "+this.sala.getNombre()+ " asignada a " + this.titulo);
    }
    
    public void crearActividad(String tipo, int id, String titulo,int cupo, int nivel) throws DatosInvalidosException {
        if(tipo.equalsIgnoreCase("Charla")) {
            Charla charla = new Charla(id, titulo,cupo, "por definir");
            actividades.add(charla);
            System.out.println("charla creada: "+this.titulo);
        } else if (tipo.equalsIgnoreCase("Taller")){
            Taller taller = new Taller(id,titulo,cupo,false);
            actividades.add(taller);
            System.out.println("Taller creado");
        } else if (tipo.equalsIgnoreCase("Curso")){
            Curso curso = new Curso(id,titulo,cupo,nivel);
            actividades.add(curso);
            System.out.println("Curso creado: " + titulo);
        }
    }


    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> resultado = new ArrayList<>();
        for (Actividad a: actividades) {
            if (tipo.isInstance(a)) {
                resultado.add(tipo.cast(a));
            }
        }

        return resultado;
    }

    public double calcularCostoMateriales(List<? extends Actividad> actividadesLista) {
        double total = 0;
        for (Actividad a: actividadesLista) {
            total += a.calcularCostoMateriales();
        }
        return total;
    }

    public void mostrarDatos(){
        System.out.println("\n ================ EVENTO "+titulo+" ==================");
        System.out.println("Id: " + Id);
        System.out.println("Costo Base: " + costoBase);
        System.out.println("Gratuito: " + gratuito);
        System.out.println("Sala: " + (sala != null ? sala.getNombre() : "Sin Asignar"));
        System.out.println("Actividades: " + actividades.size());
        System.out.println("Costo Estimado: " + calcularCostoEstimado());
        
        System.out.println("\n --------Actividades ----------");
        for(Actividad act: actividades){
            act.mostrarIdentificador();
            System.out.println("  Tipo: "+ act.getTipo()+
                              "  Costos: " + act.calcularCostoMateriales()+
                              "  Inscriptos " + act.inscripciones.size()
                              
            );
            act.mostrarInscripciones();
        }
        
        System.out.println("=================================\n");
        
    }
    
    public List<Actividad> getActividades() {
        return actividades;
    }
    
    public String getTitulo(){
        return titulo;
    }

    public boolean persistirEvento() throws IOException {
        //crear el archivo evento_01.dat
        String nombreArchivo = "evento_" + this.Id + ".dat";

        //new FileOutputStream(nombreArchivo) -> Crea el archivo en el disco
        //new ObjectOutputStream(...) -> envuelve ese canal con una capa que sabe convertir objetos java a esos bytes crudos
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))){
            //this es el evento sobre el que se llamó al método evento.persistirEvento()
            //recorre todos sus campos(titulo,costo,sala..)
            //por cada objeto sigue la cadena de referencias y también los escribe  - cada clase necesita Serializable
            oos.writeObject(this);
        }
        return true;
    }

    public static EventoUniversitario recuperarEvento(String id) throws IOException, ClassNotFoundException {
        String nombreArchivo = "evento_" + id + ".dat";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))){
            return (EventoUniversitario) ois.readObject();
        }
    }

    public String getId() {
        return this.Id;
    }

}