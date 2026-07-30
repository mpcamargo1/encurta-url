import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { finalize, timeout } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MESSAGES } from '../../shared/components/message-box/MESSAGES';
import { MessagesService } from '../../shared/components/message-box/messages-service';
import { EncurtarLinkRequest } from './EncurtarLinkRequest';
import { EncurtarLinkResponse } from './EncurtarLinkResponse';

@Injectable({
  providedIn: 'root',
})
export class EncurtarLinkService {
  private baseApiUrl = environment.baseApiUrl;
  private apiUrl = `${this.baseApiUrl}/shorten`;

  private httpClient = inject(HttpClient);

  private messagesService = inject(MessagesService);

  readonly waitingResponse = signal<boolean>(false);

  encurtarURL(request: EncurtarLinkRequest) {

    this.waitingResponse.set(true);

    this.httpClient.post<EncurtarLinkResponse>(this.apiUrl, request)
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
}
