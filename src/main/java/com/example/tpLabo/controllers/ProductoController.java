package com.example.tpLabo.controllers;

import com.example.tpLabo.*;
import com.example.tpLabo.entities.Categoria;
import com.example.tpLabo.entities.Producto;
import com.example.tpLabo.services.CategoriaService;
import com.example.tpLabo.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    @Autowired
    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;

    }

    @GetMapping("/api/productos")
    public List<Producto> getAllProductos() {
        return productoService.findAll();
    }

    @GetMapping("/api/productos/eliminados")
    public List<Producto> getProductosEliminados() {
        return productoService.findAllDeleted();
    }


    @GetMapping("/api/productos/{id}")
    public Producto getProducto(@PathVariable Integer id) {
        return productoService.findById(id);
    }

    @PostMapping("/api/productos")
    public Producto createProducto(@RequestBody Producto producto, @RequestParam("idCategoria") int idCategoria) {

        Categoria categoria = categoriaService.findById(idCategoria);
        if (categoria == null) {
            throw new CategoriaNotFoundException("Categoria no encontrada con ID: " + idCategoria);
        }

        producto.setCategoria(categoria);
        return productoService.save(producto);
    }

    @PutMapping("/api/productos/{id}")
    public Producto updateProducto(@PathVariable Integer id, @RequestBody Producto producto) {
        Integer idCategoria = producto.getIdCategoria();
        if (idCategoria != null) {
            Categoria categoria = categoriaService.findById(idCategoria);
            if (categoria == null) {
                throw new CategoriaNotFoundException("Categoria no encontrada con ID: " + idCategoria);
            }
            producto.setCategoria(categoria);
        } else {
            // Si idCategoria es null, mantener la categoría actual del producto
            Producto productoActual = productoService.findById(id);
            if (productoActual != null) {
                producto.setCategoria(productoActual.getCategoria());
            }
        }
        return productoService.update(id, producto);
    }

    @DeleteMapping("/api/productos/{id}")
    public void deleteProducto(@PathVariable Integer id) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        producto.setIsDeleted(true);
        productoService.save(producto);
    }

    @PutMapping("/api/productos/{id}/restaurar")
    public ResponseEntity<Producto> restaurarProducto(@PathVariable Integer id) {
        try {
            Producto producto = productoService.findById(id);

            if (producto == null) {
                return ResponseEntity.notFound().build(); // 404 Not Found si no se encuentra
            }

            if (!producto.getIsDeleted()) {
                return ResponseEntity.badRequest().body(producto); // 400 Bad Request si ya está activo
            }

            producto.setIsDeleted(false);
            Producto productoActualizado = productoService.save(producto);

            return ResponseEntity.ok(productoActualizado); // 200 OK con el producto actualizado
        } catch (Exception e) {
            // Manejo de excepciones generales (puedes personalizar esto)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @GetMapping("/api/productos/categoria/{idCategoria}")
    public List<Producto> getProductosByCategoria(@PathVariable int idCategoria) {
        return productoService.findByCategoria(idCategoria);
    }

    @PutMapping("/api/productos/{id}/venta")
    public Producto incrementarCantidadVendida(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        Integer cantidad = body.get("cantidad");
        Producto productoExistente = productoService.findById(id);
        if (productoExistente == null) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoExistente.setCantidadVendida(productoExistente.getCantidadVendida() + cantidad);
        return productoService.update(id, productoExistente);
    }

//    @GetMapping("/productos")
//    public String getProductos(Model model) {
//        // Lee el archivo JSON y conviértelo en una lista de Producto
//        ObjectMapper mapper = new ObjectMapper();
//        List<Producto> productos;
//        try {
//            File file = new ClassPathResource("productos.json").getFile();
//            productos = Arrays.asList(mapper.readValue(file, Producto[].class));
//        } catch (IOException e) {
//            throw new RuntimeException("No se pudo leer el archivo JSON", e);
//        }
//
//        // Agrega los productos al modelo
//        model.addAttribute("productos", productos);
//
//        // Retorna el nombre de la vista (un archivo HTML en src/main/resources/templates)
//        return "productos";
//    }
}