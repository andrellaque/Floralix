
package com.floralix.floralix.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.floralix.floralix.domain.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar productos por categoría
    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria")
    List<Producto> buscarPorCategoria(
            @Param("categoria") String categoria
    );

    // Buscar productos cuyo precio sea menor o igual al indicado
    @Query("SELECT p FROM Producto p WHERE p.precio <= :precioMaximo")
    List<Producto> buscarPorPrecioMaximo(
            @Param("precioMaximo") BigDecimal precioMaximo
    );

    // Buscar productos con stock disponible
    @Query("SELECT p FROM Producto p WHERE p.stock > 0")
    List<Producto> buscarProductosConStock();

}
