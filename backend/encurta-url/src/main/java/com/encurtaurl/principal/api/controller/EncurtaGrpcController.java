package com.encurtaurl.principal.api.controller;

import com.encurtaurl.principal.api.service.EncurtaService;
import com.encurtaurl.principal.api.validacao.url.ValidaURLImpl;
import com.encurtaurl.principal.grpc.EncurtaRequest;
import com.encurtaurl.principal.grpc.EncurtaResponse;
import com.encurtaurl.principal.grpc.EncurtaServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URI;

@GrpcService
public class EncurtaGrpcController extends EncurtaServiceGrpc.EncurtaServiceImplBase {

    @Autowired
    private ValidaURLImpl validaURL;

    @Autowired
    private EncurtaService service;

    @Override
    public void encurtarURL(EncurtaRequest request, StreamObserver<EncurtaResponse> responseStreamObserver) {

        if (!validaURL.isValid(request.getUrlOriginal(), null)) {
            responseStreamObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("A URL não é válida")
                            .asRuntimeException());
        }

        try {
            URI urlEncurtada = service.encurtarURL(request.getUrlOriginal());

            EncurtaResponse response = EncurtaResponse.newBuilder()
                    .setUrlCurta(urlEncurtada.toURL().toString())
                    .build();

            responseStreamObserver.onNext(response);
            responseStreamObserver.onCompleted();
        } catch (Exception ex) {
            responseStreamObserver.onError(ex);
        }

    }

}
