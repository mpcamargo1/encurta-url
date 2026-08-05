import { TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { computed, signal } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Message } from '../../shared/components/message-box/Message';
import { MESSAGES } from '../../shared/components/message-box/MESSAGES';
import { MessagesService } from '../../shared/components/message-box/messages-service';
import { EncurtarLinkService } from './encurtar-link-service';
import { EncurtarLinkRequest } from './EncurtarLinkRequest';
import { EncurtarLinkResponse } from './EncurtarLinkResponse';
import { EncurtarLinkToQrCodeRequest } from './EncurtarLinkToQrCodeRequest';

describe('EncurtarLinkService', () => {
  let service: EncurtarLinkService;
  let httpMock: HttpTestingController;

  let messagesService: {
    messageSignal: ReturnType<typeof signal<Message | null>>;
    message: any;
    addMessage: any;
    clearMessage: any;
  };

  beforeEach(() => {

    messagesService = {
      messageSignal: signal(null),

      get message() {
        return computed(() => this.messageSignal());
      },

      addMessage: vi.fn((val: Message) => messagesService.messageSignal.set(val)),
      clearMessage: vi.fn(() => messagesService.messageSignal.set(null)),
    };
    
    TestBed.configureTestingModule({
      providers: [
        EncurtarLinkService,
        provideHttpClient(),
        provideHttpClientTesting(),
        {provide: MessagesService, useValue: messagesService}
      ],
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

  it('deve encurtar o link corretamente', () => {
    const request: EncurtarLinkRequest = { longUrl: 'https://youtube.com' };

    service.encurtarURL(request);

    expect(service.waitingResponse()).toBe(true);

    const urlEsperada = `${environment.baseApiUrl}/shorten`;

    const req = httpMock.expectOne(urlEsperada);

    expect(req.request.method).toBe('POST');

    const resp: EncurtarLinkResponse = {
      shortUrl: 'https://encurtaurl.net/1lT0Z1MeWW',
      originalUrl: request.longUrl,
      createdAt: ''
    };

    req.flush(resp);

    expect(messagesService.addMessage).toHaveBeenCalledWith(MESSAGES.LINK_SUCCESS('https://encurtaurl.net/1lT0Z1MeWW'));
    expect(service.waitingResponse()).toBe(false);
  });

  it('deve gerar QR Code do link corretamente', () => {
    const request: EncurtarLinkToQrCodeRequest = { url: 'https://youtube.com' };

    service.encurtarURLParaQrCode(request);

    expect(service.waitingResponse()).toBe(true);

    const urlEsperada = `${environment.baseApiUrl}/qrcode`;

    const req = httpMock.expectOne(urlEsperada);

    expect(req.request.method).toBe('POST');

    const emptyBlob = new Blob([]);

    req.flush(emptyBlob);

    expect(messagesService.addMessage).toHaveBeenCalled();
    expect(service.waitingResponse()).toBe(false);
  });

  it('deve tratar timeout', () => {
    const request: EncurtarLinkRequest = { longUrl: 'https://youtube.com' };

    service.encurtarURL(request);

    expect(service.waitingResponse()).toBe(true);

    const urlEsperada = `${environment.baseApiUrl}/shorten`;

    const req = httpMock.expectOne(urlEsperada);

    expect(req.request.method).toBe('POST');

    const error = new Error('TimeoutError');

    error.name = 'TimeoutError';

    req.flush(error,
      {
        status: 504,
        statusText: 'TimeoutError'
      });

    expect(messagesService.addMessage).toHaveBeenCalledWith(MESSAGES.API_TIMEOUT_ERROR());
    expect(service.waitingResponse()).toBe(false);
  });

  it('deve tratar erro inesperado', () => {
    const request: EncurtarLinkRequest = { longUrl: 'https://youtube.com' };

    service.encurtarURL(request);

    expect(service.waitingResponse()).toBe(true);

    const urlEsperada = `${environment.baseApiUrl}/shorten`;

    const req = httpMock.expectOne(urlEsperada);

    expect(req.request.method).toBe('POST');

    const error = new Error('Error');

    error.name = 'Error';

    req.flush(error,
      {
        status: 500,
        statusText: 'Error'
      });

    expect(messagesService.addMessage).toHaveBeenCalledWith(MESSAGES.API_UNEXPECTED_ERROR());
    expect(service.waitingResponse()).toBe(false);
  });
});
