import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-encurtar-link-form',
  imports: [
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './encurtar-link-form.html',
  styleUrl: './encurtar-link-form.css',
})
export class EncurtarLinkForm implements OnInit {

  encurtaForm!: FormGroup;

  ngOnInit(): void {
    this.encurtaForm = new FormGroup({
      longUrl: new FormControl('', [Validators.required, Validators.maxLength(2048)])
    });
  }

  submit(): void {
    if (this.encurtaForm.invalid) {
      return;

    }
    window.alert("clicou em enviar");

  }

  get longUrl() {
    return this.encurtaForm.get('longUrl')!;
  }

}
