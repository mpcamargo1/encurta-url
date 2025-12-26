import { Component } from '@angular/core';
import { EncurtarLinkInput } from '../encurtar-link/encurtar-link-input';
import { EncurtarLinkButton } from "../encurtar-link/encurtar-link-button";

@Component({
  selector: 'app-home',
  imports: [EncurtarLinkInput, EncurtarLinkButton],
  templateUrl: 'home.html',
  styleUrl: './home.css',
})
export class Home { }
