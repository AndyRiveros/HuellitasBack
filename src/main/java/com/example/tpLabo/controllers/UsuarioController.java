package com.example.tpLabo.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.tpLabo.dto.ResetearContrasenaRequest;
import com.example.tpLabo.dto.RecuperarContrasenaRequest;
import com.example.tpLabo.entities.Usuario;
import com.example.tpLabo.services.UsuarioService;
import com.example.tpLabo.services.EmailService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

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

    @PostMapping("/subir-imagen/{id}")
    public ResponseEntity<String> subirImagen(@PathVariable Long id, @RequestParam("imagen") MultipartFile imagen) {
        try {
            String carpetaDestino = new File("uploads/img/").getAbsolutePath() + "/";
            File directorio = new File(carpetaDestino);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            String nombreArchivo = imagen.getOriginalFilename();
            String rutaImagen = carpetaDestino + nombreArchivo;
            imagen.transferTo(new File(rutaImagen));

            Optional<Usuario> usuario = usuarioService.getUsuario(id);
            if (usuario.isPresent()) {
                Usuario usuarioActualizado = usuario.get();
                usuarioActualizado.setImagenPerfil("/uploads/img/" + nombreArchivo);
                usuarioService.updateUsuario(usuarioActualizado);
            }

            return ResponseEntity.ok("/uploads/img/" + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir imagen");
        }
    }

    @GetMapping("/img/{nombreImagen}")
    public ResponseEntity<File> obtenerImagen(@PathVariable String nombreImagen) {
        File imagen = new File("src/main/resources/static/img/" + nombreImagen);
        if (!imagen.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imagen);
    }

    @PostMapping("/resetear-contrasena")
    public ResponseEntity<String> resetearContrasena(@RequestBody ResetearContrasenaRequest request) {
        String token = request.getToken();
        String nuevaClave = request.getNuevaClave();

        Optional<Usuario> optionalUsuario = usuarioService.getUsuarioPorToken(token);

        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido o expirado");
        }

        Usuario usuario = optionalUsuario.get();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(nuevaClave.getBytes());
            String claveEncriptada = Base64.getEncoder().encodeToString(hash);

            usuario.setClave(claveEncriptada);
            usuario.setTokenRecuperacion(null);
            usuarioService.updateUsuario(usuario);

            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al encriptar la contraseña");
        }
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<String> recuperarContrasena(@RequestBody RecuperarContrasenaRequest request) {
        Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorEmail(request.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Correo no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(token);
        usuarioService.updateUsuario(usuario);

        String enlace = "http://localhost:5173/reset-password?token=" + token;

        emailService.enviarCorreo(
                usuario.getMail(),
                "Recuperación de contraseña - Huellitas",
                "Hola " + usuario.getNombreUsuario() + ",\n\n" +
                        "Hacé clic en el siguiente enlace para restablecer tu contraseña:\n" +
                        enlace + "\n\nEste enlace es válido por 1 hora."
        );

        return ResponseEntity.ok("Correo de recuperación enviado");
    }

    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody Usuario usuario) {
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().isEmpty() ||
                usuario.getClave() == null || usuario.getClave().isEmpty() ||
                usuario.getMail() == null || usuario.getMail().isEmpty() ||
                usuario.getNombre() == null || usuario.getNombre().isEmpty() ||
                usuario.getApellido() == null || usuario.getApellido().isEmpty() ||
                usuario.getDireccion() == null || usuario.getDireccion().isEmpty() ||
                usuario.getDNI() == null ||
                usuario.getTelefono() == null) {
            return ResponseEntity.badRequest().body("Faltan campos obligatorios.");
        }

        if (usuarioService.existsByMail(usuario.getMail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está en uso.");
        }
        if (usuarioService.existsByNombreUsuario(usuario.getNombreUsuario())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya está en uso.");
        }
        if (usuarioService.existsByDNI(usuario.getDNI())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El DNI ya está en uso.");
        }
        if (usuarioService.existsByTelefono(usuario.getTelefono())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El teléfono ya está en uso.");
        }

        try {
            Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario.");
        }
    }
}