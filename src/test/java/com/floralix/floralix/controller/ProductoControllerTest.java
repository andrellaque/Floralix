
package com.floralix.floralix.controller;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.floralix.floralix.config.SecurityConfig;
import com.floralix.floralix.config.UserDetailsServiceConfig;
import com.floralix.floralix.domain.Producto;
import com.floralix.floralix.security.JwtAuthenticationFilter;
import com.floralix.floralix.service.ProductoService;

@WebMvcTest(ProductoController.class)
@Import({
    SecurityConfig.class,
    UserDetailsServiceConfig.class,
    JwtAuthenticationFilter.class
})
@WithMockUser(roles = "ADMIN")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private ProductoService productoService;

    @Test
    void debeObtenerTodosLosProductos() throws Exception {

        Producto producto = new Producto(
                "Ramo de Rosas Rojas",
                "Ramo de 12 rosas rojas",
                new BigDecimal("89.90"),
                15,
                "Ramos"
        );

        when(productoService.obtenerTodos())
                .thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].nombre").value("Ramo de Rosas Rojas"))
                .andExpect(jsonPath("$[0].precio").value(89.90));
    }

    @Test
    void debeGuardarProducto() throws Exception {

        Producto producto = new Producto(
                "Caja de Tulipanes",
                "Caja con 10 tulipanes",
                new BigDecimal("99.90"),
                10,
                "Cajas"
        );

        when(productoService.guardar(
                org.mockito.ArgumentMatchers.any(Producto.class)
        )).thenReturn(producto);

        mockMvc.perform(
                post("/api/productos")
                        .contentType("application/json")
                        .content("""
                                {
                                    "nombre": "Caja de Tulipanes",
                                    "descripcion": "Caja con 10 tulipanes",
                                    "precio": 99.90,
                                    "stock": 10,
                                    "categoria": "Cajas"
                                }
                                """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Caja de Tulipanes"))
                .andExpect(jsonPath("$.precio").value(99.90));
    }

    @Test
    void debeActualizarProducto() throws Exception {

        Producto producto = new Producto(
                "Ramo de Rosas Premium",
                "Ramo de 15 rosas rojas",
                new BigDecimal("109.90"),
                20,
                "Ramos"
        );

        when(productoService.actualizar(
                org.mockito.ArgumentMatchers.eq(1L),
                org.mockito.ArgumentMatchers.any(Producto.class)
        )).thenReturn(producto);

        mockMvc.perform(
                put("/api/productos/1")
                        .contentType("application/json")
                        .content("""
                                {
                                    "nombre": "Ramo de Rosas Premium",
                                    "descripcion": "Ramo de 15 rosas rojas",
                                    "precio": 109.90,
                                    "stock": 20,
                                    "categoria": "Ramos"
                                }
                                """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ramo de Rosas Premium"))
                .andExpect(jsonPath("$.precio").value(109.90))
                .andExpect(jsonPath("$.stock").value(20));
    }

    @Test
    void debeEliminarProducto() throws Exception {

        when(productoService.obtenerPorId(1L))
                .thenReturn(new Producto(
                        "Ramo de Girasoles",
                        "Ramo de 10 girasoles",
                        new BigDecimal("79.90"),
                        8,
                        "Ramos"
                ));

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }
}
