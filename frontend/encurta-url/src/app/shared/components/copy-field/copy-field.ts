import { Component, input, signal } from '@angular/core';

@Component({
  selector: 'app-copy-field',
  imports: [],
  templateUrl: './copy-field.html',
  styleUrl: './copy-field.css',
})
export class CopyField {
  textToCopy = input.required<string>();
  isCopied = signal(false);

  copy() {
    navigator.clipboard.writeText(this.textToCopy()).then(() => {
      this.isCopied.set(true);
      setTimeout(() => this.isCopied.set(false), 2000);
    });
  }
}