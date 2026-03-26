package com.carlosdev.springcloud.msvc.cursos.controllers;

import com.carlosdev.springcloud.msvc.cursos.models.Usuario;
import com.carlosdev.springcloud.msvc.cursos.models.entity.Curso;
import com.carlosdev.springcloud.msvc.cursos.services.CursoService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    //GET
    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        //Optional<Curso> o = service.porId(id);
        //Ahora utilizaremos el metodo mas completo
        Optional<Curso> o = service.porIdConUsuarios(id);

        //Controlamos si existe
        if(o.isPresent()){

            return ResponseEntity.ok(o.get());

        }else{
            return ResponseEntity.notFound().build();
        }

    }

    //POST
    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result){

        //Controlamos la validacion:
        if(result.hasErrors()){

            //Almacenamos los errores en un Map
            return validar(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(curso));

    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){

        //Controlamos la validacion:
        if(result.hasErrors()){

            //Almacenamos los errores en un Map
            return validar(result);
        }

        //Buscamos el curso
        Optional<Curso> o = service.porId(id);

        //Comprobamos si existe
        if(o.isPresent()){
            //Asigamos los nuevos campos
            Curso cursoModificado = o.get();
            cursoModificado.setNombre(curso.getNombre());

            //devolvemos el ok
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoModificado));

        }else{

            return ResponseEntity.notFound().build();
        }

    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        //Validamos si existe:
        Optional<Curso> o = service.porId(id);

        if(o.isPresent()){

            //Si existe, lo eliminamos
            service.eliminar(id);
            //Devolvemos la respuesta
            return ResponseEntity.noContent().build();

        }else{
            //Si no existe devolvemos que no lo encontro
            return ResponseEntity.notFound().build();

        }

    }


    //-------------------------

    //Peticiones de cliente
    //ASIGNAR
    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){

        //Comprobamos si esta presente tanto el usuario como el cursoId
        Optional<Usuario> o;

        try{
            //Si existe, le asignamos el valor a la variable o
            o = service.asignarUsuario(usuario, cursoId);
        }catch (FeignException e){

            //Si el usuario no existe, controlamos la excepcion Feign que puede saltar de la comunicacion
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No existe el usuario por el id o " +
                    "error en la comunicacion: " + e.getMessage()));
        }

        if(o.isPresent()){
            //Si existe, devolvemos el usuario creado
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        //Si el cursoId no existe:
        return ResponseEntity.notFound().build();
    }

    //CREAR
    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){

        //Comprobamos si esta presente tanto el usuario como el cursoId
        Optional<Usuario> o;

        try{
            //Si existe, le asignamos el valor a la variable o
            o = service.crearUsuario(usuario, cursoId);
        }catch (FeignException e){

            //Si el usuario no existe, controlamos la excepcion Feign que puede saltar de la comunicacion
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No se pudo crear el usuario o " +
                    "error en la comunicacion: " + e.getMessage()));
        }

        if(o.isPresent()){
            //Si existe, devolvemos el usuario creado
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        //Si el cursoId no existe:
        return ResponseEntity.notFound().build();
    }

    //ELIMINAR O DESASIGNAR
    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){

        //Comprobamos si esta presente tanto el usuario como el cursoId
        Optional<Usuario> o;

        try{
            //Si existe, le asignamos el valor a la variable o
            o = service.eliminarUsuario(usuario, cursoId);
        }catch (FeignException e){

            //Si el usuario no existe, controlamos la excepcion Feign que puede saltar de la comunicacion
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No existe el usuario por el id o " +
                    "error en la comunicacion: " + e.getMessage()));
        }

        if(o.isPresent()){
            //Si existe, devolvemos el usuario eliminado
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        //Si el cursoId no existe:
        return ResponseEntity.notFound().build();
    }

    //DELETE CURSO USUARIO, sera llamado desde el microservicio usuarios cada vez que
    //se elimine un usuario
    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id){
        service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }


    //------------------------

    //Creamos un metodo para validar los request body
    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        //Almacenamos los errores en un Map
        Map<String, String> errores = new HashMap<>();
        //Recorremos el result de errores y vamos añadiendo una clave y un valor al map:
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });

        //Devolvemos la lista en el response entity
        return ResponseEntity.badRequest().body(errores);
    }

}
