package com.example.tpLabo.controllers;

import com.example.tpLabo.entities.Mascota;
import com.example.tpLabo.entities.Usuario;
import com.example.tpLabo.repositories.MascotaRepository;
import com.example.tpLabo.repositories.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {
    private final MascotaRepository mascotaRepo;
    private final UsuarioRepository usuarioRepo;

    public MascotaController(MascotaRepository mascotaRepo, UsuarioRepository usuarioRepo) {
        this.mascotaRepo = mascotaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Mascota> getMascotasPorUsuario(@PathVariable Long usuarioId) {
        return mascotaRepo.findByUsuarioId(usuarioId);
    }

    @PostMapping("/usuario/{usuarioId}")
    public Mascota crearMascota(@PathVariable Long usuarioId, @RequestBody Mascota mascota) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            mascota.setUsuario(usuarioOpt.get());
            return mascotaRepo.save(mascota);
        }
        throw new RuntimeException("Usuario no encontrado");
    }

    @DeleteMapping("/{id}")
    public void eliminarMascota(@PathVariable Long id) {
        mascotaRepo.deleteById(id);
    }
    @PostMapping("/subir-imagen/{mascotaId}")
    public ResponseEntity<String> subirImagenMascota(
            @PathVariable Long mascotaId,
            @RequestParam("imagen") MultipartFile imagen // <-- debe ser MultipartFile
    ) throws IOException {
        String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
        Path ruta = Paths.get("uploads/mascotas/" + nombreArchivo);
        Files.createDirectories(ruta.getParent());
        Files.write(ruta, imagen.getBytes());

        Mascota mascota = mascotaRepo.findById(mascotaId).orElseThrow();
        mascota.setImagen("/uploads/mascotas/" + nombreArchivo);
        mascotaRepo.save(mascota);

        return ResponseEntity.ok(mascota.getImagen());
    }
    @PutMapping("/{id}")
    public Mascota actualizarMascota(@PathVariable Long id, @RequestBody Mascota mascotaActualizada) {
        Mascota mascota = mascotaRepo.findById(id).orElseThrow();
        if (mascotaActualizada.getImagen() != null) mascota.setImagen(mascotaActualizada.getImagen());
        if (mascotaActualizada.getEtapa() != null) mascota.setEtapa(mascotaActualizada.getEtapa());
        if (mascotaActualizada.getTipo() != null) mascota.setTipo(mascotaActualizada.getTipo());
        // ...otros campos si quieres permitir editarlos...
        return mascotaRepo.save(mascota);
    }
}