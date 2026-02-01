import { Injectable, signal } from '@angular/core';
import { Message } from './Message';

@Injectable({
  providedIn: 'root',
})
export class MessagesService {

  private messageSignal = signal<Message | null>(null);

  message = this.messageSignal.asReadonly();

  addMessage(msg: Message) {
    this.messageSignal.set(msg);
  }

  clearMessage() {
    this.messageSignal.set(null);
  }
}

