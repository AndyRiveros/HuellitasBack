package com.example.tpLabo.controllers;

import com.example.tpLabo.*;
import com.example.tpLabo.entities.Categoria;
import com.example.tpLabo.entities.Instrumento;
import com.example.tpLabo.services.CategoriaService;
import com.example.tpLabo.services.InstrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class InstrumentoController {
    private final InstrumentoService instrumentoService;
    private final CategoriaService categoriaService;

    @Autowired
    public InstrumentoController(InstrumentoService instrumentoService, CategoriaService categoriaService) {
        this.instrumentoService = instrumentoService;
        this.categoriaService = categoriaService;

    }

    @GetMapping("/api/instrumentos")
    public List<Instrumento> getAllInstrumentos() {
        return instrumentoService.findAll();
    }

    @GetMapping("/api/instrumentos/eliminados")
    public List<Instrumento> getInstrumentosEliminados() {
        return instrumentoService.findAllDeleted();
    }


    @GetMapping("/api/instrumentos/{id}")
    public Instrumento getInstrumento(@PathVariable Integer id) {
        return instrumentoService.findById(id);
    }

    @PostMapping("/api/instrumentos")
    public Instrumento createInstrumento(@RequestBody Instrumento instrumento, @RequestParam("idCategoria") int idCategoria) {

        Categoria categoria = categoriaService.findById(idCategoria);
        if (categoria == null) {
            throw new CategoriaNotFoundException("Categoria no encontrada con ID: " + idCategoria);
        }

        instrumento.setCategoria(categoria);
        return instrumentoService.save(instrumento);
    }

    @PutMapping("/api/instrumentos/{id}")
    public Instrumento updateInstrumento(@PathVariable Integer id, @RequestBody Instrumento instrumento) {
        Integer idCategoria = instrumento.getIdCategoria();
        if (idCategoria != null) {
            Categoria categoria = categoriaService.findById(idCategoria);
            if (categoria == null) {
                throw new CategoriaNotFoundException("Categoria no encontrada con ID: " + idCategoria);
            }
            instrumento.setCategoria(categoria);
        } else {
            // Si idCategoria es null, mantener la categoría actual del instrumento
            Instrumento instrumentoActual = instrumentoService.findById(id);
            if (instrumentoActual != null) {
                instrumento.setCategoria(instrumentoActual.getCategoria());
            }
        }
        return instrumentoService.update(id, instrumento);
    }

    @DeleteMapping("/api/instrumentos/{id}")
    public void deleteInstrumento(@PathVariable Integer id) {
        Instrumento instrumento = instrumentoService.findById(id);
        if (instrumento == null) {
            throw new RuntimeException("Instrumento no encontrado con ID: " + id);
        }
        instrumento.setIsDeleted(true);
        instrumentoService.save(instrumento);
    }

    @PutMapping("/api/instrumentos/{id}/restaurar")
    public ResponseEntity<Instrumento> restaurarInstrumento(@PathVariable Integer id) {
        try {
            Instrumento instrumento = instrumentoService.findById(id);

            if (instrumento == null) {
                return ResponseEntity.notFound().build(); // 404 Not Found si no se encuentra
            }

            if (!instrumento.getIsDeleted()) {
                return ResponseEntity.badRequest().body(instrumento); // 400 Bad Request si ya está activo
            }

            instrumento.setIsDeleted(false);
            Instrumento instrumentoActualizado = instrumentoService.save(instrumento);

            return ResponseEntity.ok(instrumentoActualizado); // 200 OK con el instrumento actualizado
        } catch (Exception e) {
            // Manejo de excepciones generales (puedes personalizar esto)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @GetMapping("/api/instrumentos/categoria/{idCategoria}")
    public List<Instrumento> getInstrumentosByCategoria(@PathVariable int idCategoria) {
        return instrumentoService.findByCategoria(idCategoria);
    }

    @PutMapping("/api/instrumentos/{id}/venta")
    public Instrumento incrementarCantidadVendida(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        Integer cantidad = body.get("cantidad");
        Instrumento instrumentoExistente = instrumentoService.findById(id);
        if (instrumentoExistente == null) {
            throw new RuntimeException("Instrumento no encontrado con ID: " + id);
        }
        instrumentoExistente.setCantidadVendida(instrumentoExistente.getCantidadVendida() + cantidad);
        return instrumentoService.update(id, instrumentoExistente);
    }

//    @GetMapping("/instrumentos")
//    public String getInstrumentos(Model model) {
//        // Lee el archivo JSON y conviértelo en una lista de Instrumento
//        ObjectMapper mapper = new ObjectMapper();
//        List<Instrumento> instrumentos;
//        try {
//            File file = new ClassPathResource("instrumentos.json").getFile();
//            instrumentos = Arrays.asList(mapper.readValue(file, Instrumento[].class));
//        } catch (IOException e) {
//            throw new RuntimeException("No se pudo leer el archivo JSON", e);
//        }
//
//        // Agrega los instrumentos al modelo
//        model.addAttribute("instrumentos", instrumentos);
//
//        // Retorna el nombre de la vista (un archivo HTML en src/main/resources/templates)
//        return "instrumentos";
//    }
}