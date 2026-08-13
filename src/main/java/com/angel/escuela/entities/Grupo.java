package com.angel.escuela.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="GRUPOS", uniqueConstraints = @UniqueConstraint(
        name = "GRUPO_CU_MA_AU_PE_UK",
        columnNames = {"ID_CURSO","ID_MAESTRO","ID_AULA","PERIODO"}
))
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MAESTRO", nullable = false)
    private Maestro maestro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aula aula;

    @Column(name = "PERIODO", length = 20, nullable = false)
    private String periodo;

    @Builder.Default
    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Horario> horarios = new ArrayList<>();


    public void asignarRelaciones(Curso curso, Maestro maestro,  Aula aula) {
        this.curso = curso;
        this.maestro = maestro;
        this.aula = aula;
    }

    public void validarDatos(Curso curso, Maestro maestro, Aula aula, String periodo) {

        if (curso == null || curso.getId() < 0 )
            throw new IllegalArgumentException("El idCurso es requerido y debe ser positivo");

        if (maestro == null || maestro.getId() < 0 )
            throw new IllegalArgumentException("El idMaestr es requerido y debe ser positivo");

        if (aula == null || aula.getId() < 0 )
            throw new IllegalArgumentException("El idAula es requerido y debe ser positivo");

        if (periodo == null)
            throw new IllegalArgumentException("El periodo es requerido");

    }

    public void actualizar(Curso curso, Maestro maestro, Aula aula ,String periodo) {

        validarDatos(curso,maestro,aula,periodo);

        this.curso = curso;
        this.maestro = maestro;
        this.aula = aula;
        this.periodo = periodo;

    }

    public boolean cambioEnDatos(Curso curso, Maestro maestro, Aula aula, String periodo) {
        return !this.curso.equals(curso) ||
                !this.maestro.equals(maestro) ||
                !this.aula.equals(aula) ||
                !this.periodo.equals(periodo);
    }

}
