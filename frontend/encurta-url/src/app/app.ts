import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from "./core/layout/header/header";
import { MessageBox } from "./shared/components/message-box/message-box";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, MessageBox],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('encurta-url');
}
