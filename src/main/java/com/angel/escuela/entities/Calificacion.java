package com.angel.escuela.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="CALIFICACIONES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CALIFICACION")
    private Long id;

    @Column(name = "CALIFICACION", nullable = false)
    private BigDecimal calificacion;

    @Builder.Default
    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDate fechaRegistro = LocalDate.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn (name="ID_INSCRIPCION", unique=true, nullable = false)
    private Inscripcion inscripcion;

    public void asignarValores(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public void validarDatos(Inscripcion inscripcion, BigDecimal calificacion) {

        if (inscripcion == null || inscripcion.getId() < 0 )
            throw new IllegalArgumentException("El idInscripcion es requerido y debe ser positivo");
        if (calificacion == null)
            throw new IllegalArgumentException("El idInscripcion es requerido y debe estar entre 0 y 10");

    }

    public void actualizar(Inscripcion  inscripcion, BigDecimal calificacion) {

        validarDatos(inscripcion, calificacion);

        this.calificacion = calificacion;
        this.inscripcion = inscripcion;

    }

    public boolean cambioEnDatos(Inscripcion inscripcion, BigDecimal calificacion) {
        return !this.calificacion.equals(calificacion) ||
                !this.inscripcion.equals(inscripcion);
    }


}
