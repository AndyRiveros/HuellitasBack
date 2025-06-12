package com.example.tpLabo.repositories;

import com.example.tpLabo.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findByMail(String mail); // ✅ este es el nuevo

    Optional<Usuario> findByTokenRecuperacion(String token);

}
