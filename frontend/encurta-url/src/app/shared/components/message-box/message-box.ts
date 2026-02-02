import { Component } from '@angular/core';
import { MessagesService } from './messages-service';
import { CopyField } from '../copy-field/copy-field';

@Component({
  selector: 'app-message-box',
  imports: [CopyField],
  templateUrl: './message-box.html',
  styleUrl: './message-box.css',
})
export class MessageBox {

  constructor(public messagesService: MessagesService) { }

}
