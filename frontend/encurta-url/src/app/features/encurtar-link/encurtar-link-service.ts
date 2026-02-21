import { Injectable } from '@angular/core';
import { EncurtarLinkRequest } from './EncurtarLinkRequest';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { EncurtarLinkResponse } from './EncurtarLinkResponse';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EncurtarLinkService {
  private baseApiUrl = environment.baseApiUrl;
  private apiUrl = `${this.baseApiUrl}/shorten`;

  constructor(private httpClient: HttpClient) { }

  encurtarURL(request: EncurtarLinkRequest): Observable<EncurtarLinkResponse> {
    return this.httpClient.post<EncurtarLinkResponse>(this.apiUrl, request);
  }

}
