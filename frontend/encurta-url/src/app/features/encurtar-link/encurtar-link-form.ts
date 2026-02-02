import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { EncurtarLinkRequest } from './EncurtarLinkRequest';
import { EncurtarLinkService } from './encurtar-link-service';
import { MessagesService } from '../../shared/components/message-box/messages-service';
import { EncurtarLinkResponse } from './EncurtarLinkResponse';
import { finalize, Subscription, timeout } from 'rxjs';
import { MESSAGES } from '../../shared/components/message-box/MESSAGES';

@Component({
  selector: 'app-encurtar-link-form',
  imports: [
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './encurtar-link-form.html',
  styleUrl: './encurtar-link-form.css',
})
export class EncurtarLinkForm implements OnInit, OnDestroy {

  encurtaForm!: FormGroup;

  response!: EncurtarLinkResponse;

  waitingResponse = signal(false);

  private responseSubscription!: Subscription;

  constructor(private encurtaService: EncurtarLinkService, private messagesService: MessagesService) { }

  ngOnInit(): void {
    this.encurtaForm = new FormGroup({
      longUrl: new FormControl('', [Validators.required, Validators.maxLength(2048)])
    });
  }

  ngOnDestroy(): void {
    if (this.responseSubscription) {
      this.responseSubscription.unsubscribe();
    }
  }

  submit() {
    if (this.encurtaForm.invalid) {
      return;
    }

    this.waitingResponse.set(true);

    const longUrl: string = this.encurtaForm.get('longUrl')!.value;

    const request: EncurtarLinkRequest = { longUrl: longUrl };

    this.encurtaService.encurtarURL(request)
      .pipe(
        timeout(30000),
        finalize(() => this.waitingResponse.set(false)))
      .subscribe({
        next: (res) => {
          this.messagesService.addMessage(MESSAGES.LINK_SUCCESS(res.shortUrl))
        },
        error: (err) => {

          let errorMessage = 'Ocorreu um erro inesperado.'

          if (err.name === 'TimeoutError') {
            errorMessage = 'O Servidor demorou muito para responder. Tente novamente mais tarde.'

          }
          else if (err.error?.errors && err.error.errors.length > 0) {

            errorMessage = err.error.errors[0].defaultMessage;
          }

          this.messagesService.addMessage(MESSAGES.API_ERROR(errorMessage));
        }
      });
  }

  get longUrl() {
    return this.encurtaForm.get('longUrl')!;
  }
}
