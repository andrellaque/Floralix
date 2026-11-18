import { Routes } from '@angular/router';

import { Inicio } from './pages/inicio/inicio';
import { Productos } from './pages/productos/productos';
import { Nosotros } from './pages/nosotros/nosotros';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'inicio',
    pathMatch: 'full'
  },
  {
    path: 'inicio',
    component: Inicio
  },
  {
    path: 'productos',
    component: Productos
  },
  {
    path: 'nosotros',
    component: Nosotros
  },
  {
    path: '**',
    redirectTo: 'inicio'
  }
];