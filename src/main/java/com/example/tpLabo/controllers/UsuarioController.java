package com.example.tpLabo.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import com.example.tpLabo.entities.Usuario;
import com.example.tpLabo.services.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuario(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.updateUsuario(usuario);
        return usuarioActualizado != null ? ResponseEntity.ok(usuarioActualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // 📸 Nuevo método para subir imágenes de perfil
    @PostMapping("/subir-imagen/{id}")
    public ResponseEntity<String> subirImagen(@PathVariable Long id, @RequestParam("imagen") MultipartFile imagen) {
        try {
            // Crear la carpeta si no existe
// Ruta real dentro del contexto del servidor
            String carpetaDestino = new File("src/main/resources/static/img/").getAbsolutePath() + "/";
            File directorio = new File(carpetaDestino);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }


            // Guardar la imagen en la carpeta
            String nombreArchivo = imagen.getOriginalFilename();
            String rutaImagen = carpetaDestino + nombreArchivo;
            imagen.transferTo(new File(rutaImagen));

            // Guardar solo la ruta accesible en el usuario
            Optional<Usuario> usuario = usuarioService.getUsuario(id);
            if (usuario.isPresent()) {
                Usuario usuarioActualizado = usuario.get();
                usuarioActualizado.setImagenPerfil("/uploads/img/" + nombreArchivo);
                usuarioService.updateUsuario(usuarioActualizado);
            }

            return ResponseEntity.ok("/uploads/img/" + nombreArchivo); // ✅ CORRECTO
        } catch (IOException e) {
            e.printStackTrace(); // <--- AGREGA ESTA LÍNEA
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir imagen");
        }
    }

    // 🖼 Nuevo endpoint para acceder a imágenes
    @GetMapping("/img/{nombreImagen}")
    public ResponseEntity<File> obtenerImagen(@PathVariable String nombreImagen) {
        File imagen = new File("src/main/resources/static/img/" + nombreImagen);
        if (!imagen.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imagen);
    }
}
