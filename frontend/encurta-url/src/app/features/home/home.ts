import { Component } from '@angular/core';
import { EncurtarLinkForm } from '../encurtar-link/encurtar-link-form';

@Component({
  selector: 'app-home',
  imports: [EncurtarLinkForm],
  templateUrl: 'home.html',
  styleUrl: './home.css',
})
export class Home { }
