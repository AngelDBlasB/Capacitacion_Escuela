package com.angel.escuela.entities;

import com.angel.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="AULAS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    public void validarDatos(String nombre, Integer capacidad) {

        StringCustomUtils.validarTamanio(nombre,1,100,
                "El nombre es requerido y debe tener entre 1 y 100 caracteres");

        if (capacidad == null || capacidad < 0 )
            throw new IllegalArgumentException("La capacidad es requerida y debe ser positivo");

    }

    public void actualizar (String nombre, Integer capacidad) {

        validarDatos(nombre, capacidad);

        this.nombre = nombre.trim();
        this.capacidad = capacidad;
    }

    public boolean cambioEnDatos(String nombre, Integer capacidad) {
        return !this.nombre.equals(nombre) ||
                !this.capacidad.equals(capacidad);
    }

}
