package modelo.actividades;


import certificacion.Certificable;
import excepciones.DatosInvalidosException;

import modelo.Estudiante;

public class Taller extends Actividad implements Certificable {
    private boolean requiereNotebook;
    
    public Taller(int id, String titulo, int cupoMaximo, boolean requiereNotebook) throws DatosInvalidosException {
        super(id,titulo,cupoMaximo);
        this.requiereNotebook = requiereNotebook;
    }
    
    
    //implementa metodo abstracto
    @Override
    public double calcularCostoMateriales(){
        if(requiereNotebook){
            return 5000;
        } else {
            return 2000;
        }
    }
    
    @Override
    public String getTipo(){
        return "Taller";
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "Certificado emitido por " + ENTIDAD_EMISORA + " a " + estudiante.getNombre() + " por participar del taller: " + getTitulo();
    }

    public boolean isRequiereNotebook() {
        return requiereNotebook;
    }
}
