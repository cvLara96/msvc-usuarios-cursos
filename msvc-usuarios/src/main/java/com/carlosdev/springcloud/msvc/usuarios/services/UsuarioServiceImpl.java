package com.carlosdev.springcloud.msvc.usuarios.services;

import com.carlosdev.springcloud.msvc.usuarios.models.entity.Usuario;
import com.carlosdev.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    //Inyectamos el repositorio
    @Autowired
    private UsuarioRepository repository;


    @Override
    @Transactional (readOnly = true) //Indica que solo realizara lectura de datos en la bbdd
    public List<Usuario> listar() {
        return (List<Usuario>) repository.findAll();
    }

    @Override
    @Transactional (readOnly = true) //Indica que solo realizara lectura de datos en la bbdd
    public Optional<Usuario> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Usuario> porEmail(String email) {
        return repository.findByEmail(email);
    }
}
