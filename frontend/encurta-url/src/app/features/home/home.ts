import { Component } from '@angular/core';
import { EncurtarLinkInput } from '../encurtar-link/encurtar-link-input/encurtar-link-input';
import { Logo } from '../../shared/components/logo/logo';

@Component({
  selector: 'app-home',
  imports: [EncurtarLinkInput, Logo],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {}
