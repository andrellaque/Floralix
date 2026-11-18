
package com.floralix.floralix.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.floralix.floralix.domain.Producto;
import com.floralix.floralix.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Listar todos los productos
    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    // Buscar productos por categoría
    @GetMapping("/buscar/categoria")
    public List<Producto> buscarPorCategoria(
            @RequestParam String categoria) {
        return productoService.buscarPorCategoria(categoria);
    }

    // Buscar productos por precio máximo
    @GetMapping("/buscar/precio")
    public ResponseEntity<List<Producto>> buscarPorPrecioMaximo(
            @RequestParam BigDecimal maximo) {

        if (maximo.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                productoService.buscarPorPrecioMaximo(maximo)
        );
    }

    // Buscar productos con stock disponible
    @GetMapping("/buscar/stock")
    public List<Producto> buscarProductosConStock() {
        return productoService.buscarProductosConStock();
    }

    // Buscar producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(producto);
    }

    // Crear producto
    @PostMapping
    public ResponseEntity<Producto> guardar(
            @Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardar(producto));
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Producto producto) {

        Producto actualizado = productoService.actualizar(id, producto);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
