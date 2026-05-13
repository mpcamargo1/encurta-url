import { ComponentFixture, TestBed } from '@angular/core/testing';

import { computed, signal } from '@angular/core';
import { Message } from './Message';
import { MessageBox } from './message-box';
import { MESSAGES } from './MESSAGES';
import { MessagesService } from './messages-service';

describe('MessageBox', () => {
  let component: MessageBox;
  let fixture: ComponentFixture<MessageBox>;

  let messagesService: {
    messageSignal: ReturnType<typeof signal<Message | null>>;
    message: any;
    addMessage: any;
    clearMessage: any;
  };

  beforeEach(async () => {
    messagesService = {
      messageSignal: signal(null),

      get message() {
        return computed(() => this.messageSignal());
      },

      addMessage: vi.fn((val: Message) => messagesService.messageSignal.set(val)),
      clearMessage: vi.fn(() => messagesService.messageSignal.set(null)),
    };

    await TestBed.configureTestingModule({
      imports: [MessageBox],
      providers: [{ provide: MessagesService, useValue: messagesService }],
    }).compileComponents();

    fixture = TestBed.createComponent(MessageBox);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve ser criado', () => {
    expect(component).toBeTruthy();
  });

  it('não deve ser exibido sem uma mensagem', () => {
    messagesService.clearMessage();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#message-box-container')).toBeFalsy();
  });

  it('deve ser exibido se existir uma mensagem', () => {
    messagesService.addMessage(MESSAGES.LINK_SUCCESS('https://encurtaurl.net/1lT0Z1MeWW'));
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#message-box-container')).toBeTruthy();
  });

  it('deve exibir campo para copiar link', () => {
    messagesService.addMessage(MESSAGES.LINK_SUCCESS('https://encurtaurl.net/1lT0Z1MeWW'));
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('app-copy-field')).toBeTruthy();
  });

  it('não deve exibir campo para copiar link', () => {
    messagesService.addMessage(MESSAGES.API_ERROR);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('app-copy-field')).toBeFalsy();
  });

  it('deve fechar a mensagem ao clicar no botão', () => {
    messagesService.addMessage(MESSAGES.API_ERROR);
    fixture.detectChanges();

    const botao = fixture.nativeElement.querySelector('button');
    botao.click();

    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('#message-box-container')).toBeFalsy();
  });
});
