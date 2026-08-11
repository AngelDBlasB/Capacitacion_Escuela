package com.angel.escuela.entities;

import com.angel.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="ALUMNOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder@Getter
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALUMNO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "MATRICULA", nullable = false, length = 10, unique = true)
    private String matricula;

    @Builder.Default
    @Column(name = "FECHA_INGRESO")
    private LocalDate fechaIngreso = LocalDate.now();

    private void validarDatos (String nombre, String apellidoPaterno,
                               String apellidoMaterno) {

        StringCustomUtils.validarTamanio(nombre,1,50,
                "El nombre es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoPaterno,1,50,
                "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoMaterno,1,50,
                "El apellido materno es requerido y debe tener entre 1 y 50 caracteres");

    }

    public boolean cambioEnDatos(String nombre, String apellidoPaterno,
                              String apellidoMaterno){
        return !this.nombre.equals(nombre) ||
                !this.apellidoPaterno.equals(apellidoPaterno) ||
                !this.apellidoMaterno.equals(apellidoMaterno);
    }

    public void asignarDatosAcademicos(String email, String matricula){

        StringCustomUtils.validarTamanio(email,8,100,
                "El email es requerido y debe tener entre 8 y 100 caracteres");

        StringCustomUtils.validarTamanio(matricula,10,10,
                "La matricula es requerida y debe tener exactamente 10 caracteres");

        this.email = email.toLowerCase().trim();
        this.matricula = matricula.trim();

    }

    public void actualizar(String nombre, String apellidoPaterno,
                           String apellidoMaterno, String email, String matricula) {

        validarDatos(nombre,apellidoPaterno,apellidoMaterno);
        asignarDatosAcademicos(email,matricula);

        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();

    }
}
