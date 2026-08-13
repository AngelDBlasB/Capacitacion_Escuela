package com.angel.escuela.entities;

import com.angel.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CURSOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder@Getter
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CURSO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    @Column(name = "CREDITOS", nullable = false)
    private Integer creditos;

    public void validarDatos(String nombre,Integer creditos) {

        StringCustomUtils.validarTamanio(nombre,1,100,
                "El nombre es requerido y debe tener entre 1 y 100 caracteres");

        if (creditos == null || creditos < 0 )
            throw new IllegalArgumentException("Los creditos son requerida y debe ser positivo");

    }

    public void actualizar (String nombre, String descripcion ,Integer creditos) {

        validarDatos(nombre, creditos);

        this.nombre = nombre.trim();
        this.descripcion = descripcion.trim();
        this.creditos = creditos;
    }

    public boolean cambioEnDatos(String nombre,String descripcion, Integer creditos) {
        return !this.nombre.equals(nombre) ||
                !this.descripcion.equals(descripcion) ||
                !this.creditos.equals(creditos);
    }
}
