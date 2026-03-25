package com.carlosdev.springcloud.msvc.usuarios.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity //Indicamos que es una entidad
@Table (name="usuarios") //Indicamos el nombre de la tabla en la bbdd
public class Usuario {

    @Id //Indicamos que es el id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //el id se genera de forma incremental
    private Long id;

    @NotBlank //Validation: Este campo no puede estar vacio
    private String nombre;

    @Column(unique = true) //Este campo sera unico
    @NotEmpty //Validation: Este campo no puede estar vacio y no admite caracteres en blanco
    @Email //Validation: validara que sea un email
    private String email;

    @NotBlank //Validation: Este campo no puede estar vacio y no admite caracteres en blanco
    private String password;

    //Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
