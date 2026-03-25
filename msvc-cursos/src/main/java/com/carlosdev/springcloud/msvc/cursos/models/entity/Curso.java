package com.carlosdev.springcloud.msvc.cursos.models.entity;

import com.carlosdev.springcloud.msvc.cursos.models.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nombre;

    //Creamos la relacion con usuarios:
    //Relacion: un curso puede tener muchos usuarios
    //Si eliminamos un curso, eliminara los users registrados
    //orphan eliminara todos los ids de cursos que sean nulos
    //Con JoinColumn creamos la clave foranea con la clase CursoUsuario
    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "curso_id")
    private List<CursoUsuario> cursoUsuarios;

    //Indicara que este atributo no esta mapeado a tablas
    @Transient
    private List<Usuario> usuarios;

    //Creamos el contructor
    public Curso(){
        cursoUsuarios = new ArrayList<>();
        usuarios = new ArrayList<>();
    }

    //Añadimos metodos para eliminar o agregar curso usuarios
    public void addCursoUsuario(CursoUsuario cursoUsuario){
        cursoUsuarios.add(cursoUsuario);
    }
    public void removeCursoUsuario(CursoUsuario cursoUsuario){
        cursoUsuarios.remove(cursoUsuario);
    }


    //Getter y setter


    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<CursoUsuario> getCursoUsuarios() {
        return cursoUsuarios;
    }

    public void setCursoUsuarios(List<CursoUsuario> cursoUsuarios) {
        this.cursoUsuarios = cursoUsuarios;
    }

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
}
