package com.carlosdev.springcloud.msvc.usuarios.controller;

import com.carlosdev.springcloud.msvc.usuarios.models.entity.Usuario;
import com.carlosdev.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController //Indicamos que es un controlador
public class UsuarioController {

    //Inyectamos el servicio
    @Autowired
    private UsuarioService service;

    //GET
    @GetMapping //Dejamos la url raiz por defecto
    public List<Usuario> listar(){

        //Gracias a rest controller la respuesta sera en formato json
        return service.listar();
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

    //POST
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario){

        //Indicamos el codigo de respuesta correcto, el cual para created es 201
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));

    }

    //PUT
    @PutMapping("/{id}")
    //Enviamos el usuario con los nuevos datos y su id
    public ResponseEntity<?> editar(@RequestBody Usuario usuario, @PathVariable Long id){

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

}
