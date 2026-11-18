
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, RouterLink, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {

  sesionActiva = false;
  esAdmin = false;

  username = '';
  password = '';

  cargando = false;
  error = '';
  mensaje = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    const token = this.authService.obtenerToken();

    if (token) {
      this.sesionActiva = true;
      this.esAdmin = localStorage.getItem('username') === 'admin';
    }
  }

  iniciarSesion(): void {
    this.error = '';
    this.mensaje = '';
    this.cargando = true;

    this.authService.login({
      username: this.username,
      password: this.password
    }).subscribe({
      next: () => {
        this.sesionActiva = true;
        this.esAdmin = this.username === 'admin';
        this.mensaje = 'Inicio de sesión exitoso.';
        this.password = '';
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error de inicio de sesión:', err);
        this.error = 'Usuario o contraseña incorrectos.';
        this.cargando = false;
      }
    });
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.sesionActiva = false;
    this.esAdmin = false;
    this.username = '';
    this.password = '';
    this.mensaje = '';
    this.error = '';
  }
}
