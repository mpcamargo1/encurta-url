import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { finalize, timeout } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MESSAGES } from '../../shared/components/message-box/MESSAGES';
import { MessagesService } from '../../shared/components/message-box/messages-service';
import { EncurtarLinkRequest } from './EncurtarLinkRequest';
import { EncurtarLinkResponse } from './EncurtarLinkResponse';
import { EncurtarLinkToQrCodeRequest } from './EncurtarLinkToQrCodeRequest';

@Injectable({
  providedIn: 'root',
})
export class EncurtarLinkService {
  private baseApiUrl = environment.baseApiUrl;
  private shortenUrl = `${this.baseApiUrl}/shorten`;
  private qrCodeApiUrl = `${this.baseApiUrl}/qrcode`

  private httpClient = inject(HttpClient);

  private messagesService = inject(MessagesService);

  readonly waitingResponse = signal<boolean>(false);

  private qrCodeImgUrl: string | null = null;

  encurtarURL(request: EncurtarLinkRequest) {

    this.waitingResponse.set(true);

    this.httpClient.post<EncurtarLinkResponse>(this.shortenUrl, request)
      .pipe(
        timeout(30000),
        finalize(() => this.waitingResponse.set(false)),
      )
      .subscribe({
        next: (res) => {
          this.messagesService.addMessage(MESSAGES.LINK_SUCCESS(res.shortUrl));
        },
        error: (err) => {
          let errorMessage = MESSAGES.API_UNEXPECTED_ERROR();

          const errorName = err.error?.name || err.name;

          if (errorName === 'TimeoutError') {
            errorMessage = MESSAGES.API_TIMEOUT_ERROR();
          }

          this.messagesService.addMessage(errorMessage);
        },
      });;
  }

  encurtarURLParaQrCode(request: EncurtarLinkToQrCodeRequest) {

    this.waitingResponse.set(true);

    this.httpClient.post(this.qrCodeApiUrl, request, { responseType: 'blob' })
      .pipe(
        timeout(30000),
        finalize(() => this.waitingResponse.set(false)),
      )
      .subscribe({
        next: (res) => {
          // Limpeza de memória do ObjectURL antigo
          if (this.qrCodeImgUrl) {
            URL.revokeObjectURL(this.qrCodeImgUrl);
          }

          this.qrCodeImgUrl = URL.createObjectURL(res);

          this.messagesService.addMessage(MESSAGES.QRCODE_SUCCESS(this.qrCodeImgUrl));
        },
        error: (err) => {
          let errorMessage = MESSAGES.API_UNEXPECTED_ERROR();

          const errorName = err.error?.name || err.name;

          if (errorName === 'TimeoutError') {
            errorMessage = MESSAGES.API_TIMEOUT_ERROR();
          }

          this.messagesService.addMessage(errorMessage);
        },
      });;
  }
}
