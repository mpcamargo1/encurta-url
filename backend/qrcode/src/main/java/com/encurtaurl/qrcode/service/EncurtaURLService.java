package com.encurtaurl.qrcode.service;

import com.encurtaurl.qrcode.exception.URLInvalidaException;
import com.encurtaurl.qrcode.grpc.EncurtaRequest;
import com.encurtaurl.qrcode.grpc.EncurtaResponse;
import com.encurtaurl.qrcode.grpc.EncurtaServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class EncurtaURLService {

    @GrpcClient("encurtaurl")
    private EncurtaServiceGrpc.EncurtaServiceBlockingStub encurtaService;

    public String encurtar(String url) throws Exception {
        EncurtaRequest request = EncurtaRequest.newBuilder()
                .setUrlOriginal(url)
                .build();
        try {
            EncurtaResponse response = encurtaService
                    .withDeadlineAfter(3000, TimeUnit.MILLISECONDS)
                    .encurtarURL(request);
            return response.getUrlCurta();
        } catch (Exception ex) {
            throw encapsularExcecao(ex);
        }

    }

    private Exception encapsularExcecao(Exception ex) {

        if (ex instanceof StatusRuntimeException) {
            StatusRuntimeException sre = ((StatusRuntimeException) ex);
            boolean urlInvalida = sre.getStatus().getCode() == Status.INVALID_ARGUMENT.getCode();
            if (urlInvalida) {
                return new URLInvalidaException(sre.getMessage());
            }
        }

        return ex;
    }

}
