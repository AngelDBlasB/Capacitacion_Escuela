package com.angel.escuela.entities;

import com.angel.escuela.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="HORARIOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Enumerated(EnumType.STRING)
    @Column(name = "DIA", nullable = false)
    private DiaSemana dia;

    @Column(name = "HORA_INICIO", length = 5, nullable = false)
    private String horaInicio;

    @Column(name = "HORA_FIN", length = 5, nullable = false)
    private String horaFin;

    public void asignarValores(Grupo grupo, DiaSemana dia) {
        this.grupo = grupo;
        this.dia = dia;
    }

    public void validarDatos(Grupo grupo, DiaSemana dia, String horaInicio, String horaFin ) {

        if (grupo == null || grupo.getId() < 0 )
            throw new IllegalArgumentException("El idGrupo es requerido y debe ser positivo");

        if (dia == null)
            throw new IllegalArgumentException("El día de la semana es requerido");

        if (horaInicio == null)
            throw new IllegalArgumentException("La hora inicio es requerida");

        if (horaFin == null)
            throw new IllegalArgumentException("La hora fin es requerida");

    }

    public void actualizar(Grupo grupo, DiaSemana dia, String horaInicio, String horaFin ) {

        validarDatos(grupo, dia, horaInicio, horaFin);

        this.grupo = grupo;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;

    }

    public boolean cambioEnDatos(Grupo grupo, DiaSemana dia, String horaInicio, String horaFin ) {
        return !this.grupo.equals(grupo) ||
                !this.dia.equals(dia) ||
                !this.horaInicio.equals(horaInicio) ||
                !this.horaFin.equals(horaFin);
    }


}
