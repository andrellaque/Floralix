
package com.floralix.floralix.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.floralix.floralix.domain.Producto;
import com.floralix.floralix.repository.ProductoRepository;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Crear producto
    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // Listar todos los productos
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // Buscar producto por ID
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // Actualizar producto
    @Transactional
    public Producto actualizar(Long id, Producto datosActualizados) {
        Producto producto = productoRepository.findById(id).orElse(null);

        if (producto == null) {
            return null;
        }

        producto.setNombre(datosActualizados.getNombre());
        producto.setDescripcion(datosActualizados.getDescripcion());
        producto.setPrecio(datosActualizados.getPrecio());
        producto.setStock(datosActualizados.getStock());
        producto.setCategoria(datosActualizados.getCategoria());

        return productoRepository.save(producto);
    }

    // Eliminar producto
    @Transactional
    public void eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        }
    }

    // Buscar productos por categoría mediante JPQL
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.buscarPorCategoria(categoria);
    }

    // Buscar productos por precio máximo mediante JPQL
    public List<Producto> buscarPorPrecioMaximo(BigDecimal precioMaximo) {
        return productoRepository.buscarPorPrecioMaximo(precioMaximo);
    }

    // Buscar productos que tengan stock disponible mediante JPQL
    public List<Producto> buscarProductosConStock() {
        return productoRepository.buscarProductosConStock();
    }
}
