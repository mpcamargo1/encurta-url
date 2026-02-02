import { Message } from "./Message";

export const MESSAGES = {
    LINK_SUCCESS: (url: string): Message => ({
        name: "linkEncurtado",
        header: 'Link Curto:',
        body: url,
        type: 'information',
    }),

    API_ERROR: (errorMessage: string): Message => ({
        header: 'Erro ao realizar operação',
        body: `Erro: ${errorMessage}`,
        type: 'error',
    })
};