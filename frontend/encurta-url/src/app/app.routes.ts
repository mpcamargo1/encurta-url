import { Routes } from '@angular/router';
import { Home } from './features/home/home';
import { Page404 } from './features/page-404/page-404';

export const routes: Routes = [
    { path: '', component: Home },
    { path: '**', component: Page404 }
];
