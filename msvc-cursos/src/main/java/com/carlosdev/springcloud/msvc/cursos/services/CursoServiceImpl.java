package com.carlosdev.springcloud.msvc.cursos.services;

import com.carlosdev.springcloud.msvc.cursos.models.Usuario;
import com.carlosdev.springcloud.msvc.cursos.models.entity.Curso;
import com.carlosdev.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService{

    //Inyectamos el repositorio
    @Autowired
    private CursoRepository repository;

    @Override
    @Transactional(readOnly = true) //Sera una transaccion de solo lectura
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true) //Sera una transaccion de solo lectura
    public Optional<Curso> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    //Metodos de comunicacion HTTP:

    @Override
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long curso_id) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> crearUsuario(Usuario usuario, Long curso_id) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long curso_id) {
        return Optional.empty();
    }
}
