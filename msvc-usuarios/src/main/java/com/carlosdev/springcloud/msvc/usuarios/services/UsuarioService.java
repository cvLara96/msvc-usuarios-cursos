package com.carlosdev.springcloud.msvc.usuarios.services;

import com.carlosdev.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

//En el servicio implementaremos los metodos necesarios
public interface UsuarioService {

    //Metodo de listar usuarios
    List<Usuario> listar();

    //Obtener usuario por id
    //Lo manejamos con el optional para controlar si es nulo o no.
    Optional<Usuario> porId(Long id);

    //Metodo para guardar el usuario, devuelve el usuario que se ha guardado
    Usuario guardar(Usuario usuario);

    //Metodo para eliminar
    void eliminar(Long id);

    //Metodo para buscar por email
    Optional<Usuario> porEmail(String email);

    //Meotodo para obtener lista de usuarios por ids, es decir, recibira una
    //lista de ids y nos devolvera la lista de usuarios con esos ids:
    List<Usuario> listarPorIds(Iterable<Long> ids);


}
