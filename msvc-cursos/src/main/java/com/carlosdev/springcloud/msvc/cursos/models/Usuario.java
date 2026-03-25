package com.carlosdev.springcloud.msvc.cursos.models;

//no sera un entity
public class Usuario {

    //Tendra los mismos atributos que el entity del microservicio usuario
    private Long id;
    private String nombre;
    private String email;
    private String password;

    //Getter and setter

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
