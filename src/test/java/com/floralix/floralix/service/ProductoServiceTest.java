package com.floralix.floralix.service;

import com.floralix.floralix.domain.Producto;
import com.floralix.floralix.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void debeGuardarProducto() {
        Producto producto = new Producto(
                "Ramo de Rosas Rojas",
                "Ramo de 12 rosas rojas",
                new BigDecimal("89.90"),
                15,
                "Ramos"
        );

        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.guardar(producto);

        assertEquals(producto, resultado);
        verify(productoRepository).save(producto);
    }
    @Test
    void debeObtenerTodosLosProductos() {
        List<Producto> productos = List.of(
                new Producto(
                        "Ramo de Rosas Rojas",
                        "Ramo de 12 rosas rojas",
                        new BigDecimal("89.90"),
                        15,
                        "Ramos"
                ),
                new Producto(
                        "Caja de Tulipanes",
                        "Caja con 10 tulipanes",
                        new BigDecimal("99.90"),
                        10,
                        "Cajas"
                )
        );

        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.obtenerTodos();

        assertEquals(2, resultado.size());
        verify(productoRepository).findAll();
    }
    @Test
    void debeObtenerProductoPorId() {
        Producto producto = new Producto(
                "Ramo de Girasoles",
                "Ramo de 10 girasoles",
                new BigDecimal("79.90"),
                8,
                "Ramos"
        );

        when(productoRepository.findById(1L)).thenReturn(java.util.Optional.of(producto));

        Producto resultado = productoService.obtenerPorId(1L);

        assertEquals(producto, resultado);
        verify(productoRepository).findById(1L);
    }
    @Test
void debeActualizarProducto() {
    Producto productoExistente = new Producto(
            "Ramo de Rosas Rojas",
            "Ramo de 12 rosas rojas",
            new BigDecimal("89.90"),
            15,
            "Ramos"
    );

    Producto datosActualizados = new Producto(
            "Ramo de Rosas Premium",
            "Ramo de 15 rosas rojas",
            new BigDecimal("109.90"),
            20,
            "Ramos"
    );

    when(productoRepository.findById(1L))
            .thenReturn(java.util.Optional.of(productoExistente));

    when(productoRepository.save(productoExistente))
            .thenReturn(productoExistente);

    Producto resultado = productoService.actualizar(1L, datosActualizados);

    assertEquals("Ramo de Rosas Premium", resultado.getNombre());
    assertEquals("Ramo de 15 rosas rojas", resultado.getDescripcion());
    assertEquals(new BigDecimal("109.90"), resultado.getPrecio());
    assertEquals(20, resultado.getStock());
    assertEquals("Ramos", resultado.getCategoria());

    verify(productoRepository).findById(1L);
    verify(productoRepository).save(productoExistente);
}
@Test
void debeEliminarProducto() {
    Producto producto = new Producto(
            "Ramo de Girasoles",
            "Ramo de 10 girasoles",
            new BigDecimal("79.90"),
            8,
            "Ramos"
    );

    when(productoRepository.existsById(1L)).thenReturn(true);

    productoService.eliminar(1L);

    verify(productoRepository).existsById(1L);
    verify(productoRepository).deleteById(1L);
}
}