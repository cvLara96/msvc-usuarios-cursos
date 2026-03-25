package com.carlosdev.springcloud.msvc.cursos.controllers;

import com.carlosdev.springcloud.msvc.cursos.models.entity.Curso;
import com.carlosdev.springcloud.msvc.cursos.services.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional<Curso> o = service.porId(id);

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
