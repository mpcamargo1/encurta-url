import { Message } from "./Message";

export const MESSAGES = {
    LINK_SUCCESS: (url: string): Message => ({
        name: "linkEncurtado",
        header: 'Link Curto:',
        body: url,
        type: 'information',
    }),

    QRCODE_SUCCESS: (qrCodeImgUrl: string): Message => ({
        name: "qrcodeGerado",
        header: 'QR Code Gerado:',
        body: qrCodeImgUrl,
        type: 'information',
    }),

    API_ERROR: (errorMessage: string): Message => ({
        header: 'Erro ao realizar operação',
        body: `Erro: ${errorMessage}`,
        type: 'error',
    }),

    API_TIMEOUT_ERROR: (): Message =>
        MESSAGES.API_ERROR('O Servidor demorou muito para responder. Tente novamente mais tarde.'),

    API_UNEXPECTED_ERROR: (): Message =>
        MESSAGES.API_ERROR('Ocorreu um erro inesperado.')
};