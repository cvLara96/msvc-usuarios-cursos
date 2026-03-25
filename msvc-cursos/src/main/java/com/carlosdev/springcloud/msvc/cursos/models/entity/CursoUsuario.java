package com.carlosdev.springcloud.msvc.cursos.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos_usuarios")
public class CursoUsuario {

    //Esta clase registrara los ids de los alumnos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //con unique indicamos que no podremos tener el mismo alumno repetido en el curso
    @Column(name = "usuario_id", unique = true)
    private Long usuarioId;

    //Getter and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    //Sobreescribimos el metodo equals para la eliminacion de objetos
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        //Comprobamos si el obj es una instancia de CursoUsuario
        if(!(obj instanceof CursoUsuario)){
            return false;
        }

        CursoUsuario o = (CursoUsuario) obj;
        //Comparamos los ids
        return this.usuarioId != null && this.usuarioId.equals(o.usuarioId);
    }
}
