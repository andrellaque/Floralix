
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';

import { ProductoService, Producto } from '../../services/producto';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [FormsModule, DecimalPipe],
  templateUrl: './productos.html',
  styleUrl: './productos.scss'
})
export class Productos implements OnInit {

  productos: Producto[] = [];

  sesionActiva = false;
  esAdmin = false;

  cargando = false;
  error = '';
  mensaje = '';

  nuevoProducto: Producto = {
    nombre: '',
    descripcion: '',
    precio: 0,
    stock: 0,
    categoria: ''
  };

  constructor(
    private productoService: ProductoService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const token = this.authService.obtenerToken();

    if (token) {
      this.sesionActiva = true;
      this.esAdmin = localStorage.getItem('username') === 'admin';
      this.listarProductos();
    }
  }

  listarProductos(): void {
    this.cargando = true;
    this.error = '';

    this.productoService.listar().subscribe({
      next: (datos) => {
        this.productos = datos;
        this.cargando = false;
        console.log('Productos recibidos:', datos);
      },
      error: (err) => {
        console.error('Error al cargar productos:', err);
        this.error = 'No se pudieron cargar los productos.';
        this.cargando = false;
      }
    });
  }

  registrarProducto(): void {
    this.error = '';
    this.mensaje = '';

    this.productoService.crear(this.nuevoProducto).subscribe({
      next: () => {
        this.mensaje = 'Producto registrado correctamente.';

        this.nuevoProducto = {
          nombre: '',
          descripcion: '',
          precio: 0,
          stock: 0,
          categoria: ''
        };

        this.listarProductos();
      },
      error: (err) => {
        console.error('Error al registrar:', err);
        this.error = 'No se pudo registrar el producto.';
      }
    });
  }

  editarProducto(producto: Producto): void {
    const nombre = prompt('Nuevo nombre:', producto.nombre);
    if (nombre === null || nombre.trim() === '') return;

    const descripcion = prompt(
      'Nueva descripción:',
      producto.descripcion
    );
    if (descripcion === null) return;

    const precioTexto = prompt(
      'Nuevo precio:',
      producto.precio.toString()
    );
    if (precioTexto === null) return;

    const stockTexto = prompt(
      'Nuevo stock:',
      producto.stock.toString()
    );
    if (stockTexto === null) return;

    const precio = Number(precioTexto);
    const stock = Number(stockTexto);

    if (
      precio <= 0 ||
      !Number.isFinite(precio) ||
      stock < 0 ||
      !Number.isInteger(stock)
    ) {
      alert('Ingresa un precio válido y un stock válido.');
      return;
    }

    const actualizado: Producto = {
      ...producto,
      nombre: nombre.trim(),
      descripcion: descripcion.trim(),
      precio,
      stock
    };

    this.productoService.actualizar(producto.id!, actualizado)
      .subscribe({
        next: () => {
          alert('Producto actualizado correctamente.');
          this.listarProductos();
        },
        error: (err) => {
          console.error(err);
          alert('No se pudo actualizar el producto.');
        }
      });
  }

  eliminarProducto(producto: Producto): void {
    const confirmar = confirm(
      `¿Estás seguro de eliminar "${producto.nombre}"?`
    );

    if (!confirmar) return;

    this.productoService.eliminar(producto.id!).subscribe({
      next: () => {
        alert('Producto eliminado correctamente.');
        this.listarProductos();
      },
      error: (err) => {
        console.error(err);
        alert('No se pudo eliminar el producto.');
      }
    });
  }
}
