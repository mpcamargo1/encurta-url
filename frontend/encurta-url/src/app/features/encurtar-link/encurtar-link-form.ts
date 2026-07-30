import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Subscription } from 'rxjs';
import { EncurtarLinkService } from './encurtar-link-service';
import { EncurtarLinkRequest } from './EncurtarLinkRequest';

@Component({
  selector: 'app-encurtar-link-form',
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './encurtar-link-form.html',
  styleUrl: './encurtar-link-form.css',
})
export class EncurtarLinkForm implements OnInit, OnDestroy {
  encurtaForm!: FormGroup;

  readonly encurtaService = inject(EncurtarLinkService);

  isSubmitted = signal(false);

  private responseSubscription!: Subscription;

  ngOnInit(): void {
    this.encurtaForm = new FormGroup({
      longUrl: new FormControl('', [
        Validators.required,
        Validators.maxLength(2048),
        Validators.pattern(/^(https?:\/\/)(?=(w{3}\.)*)\2([0-9a-zA-Z.]{2,})(\.[a-z]{2,})(\/.*)?$/),
      ]),
      isGenerateQrCode: new FormControl(false, { nonNullable: true }),
    });
  }

  ngOnDestroy(): void {
    if (this.responseSubscription) {
      this.responseSubscription.unsubscribe();
    }
  }

  submit() {
    this.isSubmitted.set(true);

    this.formatUrl();

    if (this.encurtaForm.invalid) {
      return;
    }

    const longUrl: string = this.encurtaForm.get('longUrl')!.value;
    
    const request: EncurtarLinkRequest = { longUrl: longUrl };

    this.encurtaService.encurtarURL(request);
  }

  formatUrl() {
    const control = this.encurtaForm.get('longUrl');
    let url = control?.value?.trim();

    if (url && !/^(http:\/\/|https:\/\/)/i.test(url)) {
      control?.setValue(`https://${url}`);
    }
  }

  get longUrl() {
    return this.encurtaForm.get('longUrl')!;
  }

  toggleGenerateQrCode() {
    const control = this.encurtaForm.get('isGenerateQrCode');
    control?.setValue(!control.value);
    control?.markAsTouched();
  }

  get isGenerateQrCode() {
    return this.encurtaForm.get('isGenerateQrCode')?.value;
  }
}
