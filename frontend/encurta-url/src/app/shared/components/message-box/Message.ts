import { MessageType } from "./MessageType";

export interface Message {
    name?: string,
    header: string,
    body: string,
    type: MessageType
}