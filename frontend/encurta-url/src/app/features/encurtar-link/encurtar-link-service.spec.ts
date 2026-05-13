import { TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';
import { EncurtarLinkService } from './encurtar-link-service';
import { EncurtarLinkRequest } from './EncurtarLinkRequest';

describe('EncurtarLinkService', () => {
  let service: EncurtarLinkService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EncurtarLinkService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(EncurtarLinkService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Garante que nenhuma requisição HTTP inesperada ficou pendente.
    httpMock.verify();
  });

  it('deve ser criado', () => {
    expect(service).toBeTruthy();
  });

  it('deve montar a requisição para encurtar link corretamente', () => {
    const request: EncurtarLinkRequest = { longUrl: 'https://youtube.com' };

    service.encurtarURL(request).subscribe();

    const urlEsperada = `${environment.baseApiUrl}/shorten`;

    const req = httpMock.expectOne(urlEsperada);

    expect(req.request.method).toBe('POST');
    req.flush({});
  });
});
