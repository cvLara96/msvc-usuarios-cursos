package com.carlosdev.springcloud.msvc.usuarios.controller;

import com.carlosdev.springcloud.msvc.usuarios.models.entity.Usuario;
import com.carlosdev.springcloud.msvc.usuarios.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController //Indicamos que es un controlador
public class UsuarioController {

    //Inyectamos el servicio
    @Autowired
    private UsuarioService service;

    //GET
    @GetMapping //Dejamos la url raiz por defecto
    public Map <String, List<Usuario>> listar(){

        //Gracias a rest controller la respuesta sera en formato json
        //return service.listar();
        return Collections.singletonMap("usuarios", service.listar());
    }

    //GET POR ID
    @GetMapping("/{id}")
    //devolvera un response entity indicando un ? ya que puede devolver un json con
    //la info del usuario si esta presente, o un json con otro contenido en caso
    //de no existir ese usuario.
    public ResponseEntity<?> detalle(@PathVariable Long id){

        Optional<Usuario> usuarioOptional = service.porId(id);

        //Comprobamos si esta presente, es decir, si existe, y controlamos que no sea null
        if(usuarioOptional.isPresent()){
            //Si existe devolvemos el contenido en el response entity
            return ResponseEntity.ok(usuarioOptional.get());
        }else{
            //Si no existe devolvemos que no lo encontro
            return ResponseEntity.notFound().build();
        }
    }

    //GET USUARIOS POR IDS
    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.listarPorIds(ids));
    }

    //POST
    //Añadimos @Valid y BindingResult result para validar, debe ir despues del REQUEST BODY
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result){

        //Controlamos si el usuario ya existe con el email indicado
        if (service.porEmail(usuario.getEmail()).isPresent()) {
            //Si existe indicamos que no se ha podido realizar
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese email"));
        }


        //Controlamos la validacion:
        if(result.hasErrors()){

            return validar(result);
        }

        //Indicamos el codigo de respuesta correcto, el cual para created es 201
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));

    }

    //PUT
    @PutMapping("/{id}")
    //Enviamos el usuario con los nuevos datos y su id
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result,@PathVariable Long id){

        //Controlamos si el email que se indica como nuevo ya existe en la bbdd
        if (service.porEmail(usuario.getEmail()).isPresent()) {
            //Si existe indicamos que no se ha podido realizar
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese email"));
        }

        //Controlamos la validacion:
        if(result.hasErrors()){

            //Almacenamos los errores en un Map
            return validar(result);
        }

        //Obtenemos el usuario
        Optional<Usuario> usuarioOptional = service.porId(id);

        //Controlamos si existe
        if(usuarioOptional.isPresent()){
            //Creamos un usuario para meter los datos nuevos
            Usuario usuarioModificado = usuarioOptional.get();
            usuarioModificado.setNombre(usuario.getNombre());
            usuarioModificado.setEmail(usuario.getEmail());
            usuarioModificado.setPassword(usuario.getPassword());
            //devolvemos el json con los nuevos datos de usuario
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioModificado));
        }else{
            //Si no existe devolvemos que no lo encontro
            return ResponseEntity.notFound().build();

        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        //Validamos si existe:
        Optional<Usuario> usuarioOptional = service.porId(id);

        if(usuarioOptional.isPresent()){

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

    //Metodo para validar:
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
