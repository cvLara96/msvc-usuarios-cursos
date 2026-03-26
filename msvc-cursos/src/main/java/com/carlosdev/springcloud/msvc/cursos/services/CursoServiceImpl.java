package com.carlosdev.springcloud.msvc.cursos.services;

import com.carlosdev.springcloud.msvc.cursos.clients.UsuarioClientRest;
import com.carlosdev.springcloud.msvc.cursos.models.Usuario;
import com.carlosdev.springcloud.msvc.cursos.models.entity.Curso;
import com.carlosdev.springcloud.msvc.cursos.models.entity.CursoUsuario;
import com.carlosdev.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService{

    //Inyectamos el repositorio
    @Autowired
    private CursoRepository repository;

    //Inyectamos la interfaz del clientRest
    @Autowired
    private UsuarioClientRest client;

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

    //Metodo para asignar un usuario a un curso en especifico:
    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long curso_id) {
        //Validamos si el curso existe en el microservicio cursos
        Optional<Curso> o = repository.findById(curso_id);
        if(o.isPresent()){
            //Si el curso existe, nos comunicamos con el microservicio usuarios para comprobar si existe
            //con el metodo del client
            Usuario usuarioMsvc = client.detalle(usuario.getId());

            //Si sale bien, crearemos un cursoUsuario para registrar el usuario
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            //Lo agregamos al curso
            curso.addCursoUsuario(cursoUsuario);
            //guardamos el curso con el cambio
            repository.save(curso);
            //Devolvemos el usuario guardado
            return Optional.of(usuarioMsvc);
        }

        //Si no esta presente el curso, devolvemos un empty:
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long curso_id) {

        //Validamos si el curso existe en el microservicio cursos
        Optional<Curso> o = repository.findById(curso_id);
        if(o.isPresent()){
            //Si el curso existe, nos comunicamos con el microservicio usuarios para crear el nuevo usuario
            //con el metodo del client
            Usuario usuarioNuevoMsvc = client.crear(usuario);

            //Si sale bien, crearemos un cursoUsuario para registrar el usuario que acabamos de crear
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            //Lo agregamos al curso
            curso.addCursoUsuario(cursoUsuario);
            //guardamos el curso con el cambio
            repository.save(curso);
            //Devolvemos el usuario guardado
            return Optional.of(usuarioNuevoMsvc);
        }

        //Si no esta presente el curso, devolvemos un empty:
        return Optional.empty();

    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long curso_id) {
        //Validamos si el curso existe en el microservicio cursos
        Optional<Curso> o = repository.findById(curso_id);
        if(o.isPresent()){
            //Si el curso existe, nos comunicamos con el microservicio usuarios para comprobar si existe
            //con el metodo del client
            Usuario usuarioMsvc = client.detalle(usuario.getId());

            //Si sale bien, crearemos un cursoUsuario para registrar el usuario
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            //Lo desasignamos del curso
            curso.removeCursoUsuario(cursoUsuario);
            //guardamos el curso con el cambio
            repository.save(curso);
            //Devolvemos el usuario eliminado
            return Optional.of(usuarioMsvc);
        }

        //Si no esta presente el curso, devolvemos un empty:
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {

        //Obtenemos el curso para ver si esta presente
        Optional<Curso> o = repository.findById(id);
        if(o.isPresent()){

            //Si esta presente, obtenemos el curso
            Curso curso = o.get();

            //validamos para asegurarnos que la lista de ids no este vacia
            if(!curso.getCursoUsuarios().isEmpty()){

                //si NO esta vacio, obtenemos los ids
                //utilizamos stream para recorrer el listado que da getCursoUsuarios()
                //utilizamos el metodo map de stream, por cada elemento de cursoUsuario devolvemos el id:
                List<Long> ids = curso.getCursoUsuarios()
                        .stream().map(cu -> cu.getUsuarioId())
                        //Como el resultado debe ser una lista, utilizamos collect para convertirlo
                        .collect(Collectors.toList());
                //A continuacion obtenemos del microservicio usuarios la lista de usuarios
                List<Usuario> usuarios = client.obtenerAlumnosPorCurso(ids);
                //establecemos los usuarios al curso
                curso.setUsuarios(usuarios);
            }

            //Devolvemos el curso modificado
            return Optional.of(curso);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        repository.eliminarCursoUsuarioPorId(id);
    }
}
