package modelo.actividades;

import modelo.Estudiante;
import certificacion.Certificable;
import excepciones.DatosInvalidosException;

public class Curso extends Actividad implements Certificable {

    private int nivel;

    public Curso(int id, String titulo, int cupoMaximo, int nivel) throws DatosInvalidosException {
        super(id, titulo, cupoMaximo);
        this.nivel = nivel;
    }

    @Override
    public double calcularCostoMateriales() {
        return 1500;
    }

    @Override
    public String getTipo() {
        return "Curso";
    }

    @Override // reemplazando o cumpliendo uno que ya existía en otro lado o en la clase padre(extends o implements)
    public String generarCertificado(Estudiante estudiante) {
        return "Certificado emitido por " + ENTIDAD_EMISORA + " a " + estudiante.getNombre() + " por participar del curso: " + getTitulo() + "(nivel " + nivel + ")";
    }

    public int getNivel() {
        return nivel;
    }
}
