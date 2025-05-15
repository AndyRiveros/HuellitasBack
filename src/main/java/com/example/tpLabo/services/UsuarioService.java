package com.example.tpLabo.services;

import com.example.tpLabo.entities.Usuario;
import com.example.tpLabo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario createUsuario(Usuario usuario) {
        try {
            usuario.setClave(usuario.encriptarClave(usuario.getClave()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public Optional<Usuario> getUsuario(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario updateUsuario(Usuario usuario) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(usuario.getId());
        if (existingUsuario.isPresent()) {
            Usuario updatedUsuario = existingUsuario.get();
            updatedUsuario.setNombre(usuario.getNombre());
            updatedUsuario.setApellido(usuario.getApellido());
            updatedUsuario.setDireccion(usuario.getDireccion());
            updatedUsuario.setDNI(usuario.getDNI());
            updatedUsuario.setMail(usuario.getMail());
            updatedUsuario.setTelefono(usuario.getTelefono());
            updatedUsuario.setRol(usuario.getRol());
            return usuarioRepository.save(updatedUsuario);
        }
        return null;
    }

    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }
}