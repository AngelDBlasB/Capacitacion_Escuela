package com.angel.escuela.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="INSCRIPCIONES", uniqueConstraints = @UniqueConstraint(
        name = "INSCRIPCION_ALU_GRU_UK",
        columnNames = {"ID_ALUMNO","ID_GRUPO"}
))
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INSCRIPCION")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ALUMNO", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Builder.Default
    @Column(name = "FECHA_INSCRIPCION")
    private LocalDate fechaInscripcion = LocalDate.now();

    @OneToOne(mappedBy = "inscripcion")
    private Calificacion calificacion;

    public void validarDatos(Alumno alumno, Grupo grupo) {

        if (alumno == null || alumno.getId() < 0 )
            throw new IllegalArgumentException("El idAlumno es requerido y debe ser positivo");

        if (grupo == null || grupo.getId() < 0 )
            throw new IllegalArgumentException("El idGrupo es requerido y debe ser positivo");

    }

    public void actualizar(Alumno alumno, Grupo grupo) {

        validarDatos(alumno, grupo);

        this.alumno = alumno;
        this.grupo = grupo;

    }

    public boolean cambioEnDatos(Alumno alumno, Grupo grupo) {
        return !this.alumno.equals(alumno) ||
                !this.grupo.equals(grupo);
    }
}
