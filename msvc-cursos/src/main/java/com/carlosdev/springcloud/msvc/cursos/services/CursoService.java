package com.carlosdev.springcloud.msvc.cursos.services;

import com.carlosdev.springcloud.msvc.cursos.models.Usuario;
import com.carlosdev.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    //Metodo listar cursos
    List<Curso> listar();

    //Obtener un curso
    Optional<Curso> porId(Long id);

    //Guardar un curso
    Curso guardar(Curso curso);

    //Eliminar
    void eliminar(Long id);

    //-----------------------------

    //Comunicacion HTTP
    //asignarUsuario, recibe el usuario y el id del curso
    Optional<Usuario> asignarUsuario(Usuario usuario, Long curso_id);

    //crearUsuario, recibe un nuevo usuario para que lo cree en el
    //microservicio usuarios y se inserte en un curso
    Optional<Usuario> crearUsuario(Usuario usuario, Long curso_id);

    //eliminarUsuario, desasigna a un usuario de un curso
    Optional<Usuario> eliminarUsuario(Usuario usuario, Long curso_id);

}
