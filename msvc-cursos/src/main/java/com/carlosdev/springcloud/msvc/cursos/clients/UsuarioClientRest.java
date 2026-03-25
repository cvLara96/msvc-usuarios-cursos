package com.carlosdev.springcloud.msvc.cursos.clients;

import com.carlosdev.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Indicamos el nombre del microservicio cliente y su url
//Cuando integremos spring cloud con kubernetes esto se elimina: url="localhost:8001" ya que se desacopla
@FeignClient(name="msvc-usuarios", url="localhost:8001")
public interface UsuarioClientRest {

    //Debe ser lo mas similar posible al controller
    @GetMapping("/{id}")
    Usuario detalle(@PathVariable  Long id);

    @PostMapping("/")
    Usuario crear(@RequestBody Usuario usuario);

}
