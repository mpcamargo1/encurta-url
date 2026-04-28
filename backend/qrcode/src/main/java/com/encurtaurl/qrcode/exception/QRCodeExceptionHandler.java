package com.encurtaurl.qrcode.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class QRCodeExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(QRCodeExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        final String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        String httpMethod = "UNKNOWN";

        HttpServletRequest servletRequest = request instanceof NativeWebRequest
                ? ((NativeWebRequest) request).getNativeRequest(HttpServletRequest.class)
                : null;

        if (servletRequest != null && servletRequest.getMethod() != null) {
            httpMethod = servletRequest.getMethod();
        }

        if (Objects.equals(httpMethod, HttpMethod.GET.name())
                && acceptHeader != null
                && acceptHeader.contains(MediaType.TEXT_HTML_VALUE)) {
            return ResponseEntity.notFound().build();
        }

        return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(
            Exception exception,
            HttpServletRequest httpRequest) {

        logger.error(exception.getMessage());

        Map<String, Object> corpo = new HashMap<>();
        corpo.put("status", 500);
        corpo.put("erro", "Erro Interno");
        corpo.put("mensagem", "O motor do QR Code encontrou um erro inesperado.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(corpo);
    }
}
