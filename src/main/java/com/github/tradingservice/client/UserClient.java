package com.github.tradingservice.client;

import com.github.tradingservice.dto.TransactionRequest;
import com.github.tradingservice.dto.TransactionResponse;
import com.github.tradingservice.dto.UserDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserClient {

    private RSocketRequester requester;

    public UserClient(RSocketRequester.Builder builder,
                      RSocketConnectorConfigurer connectorConfigurer,
                      @Value("${user.service.host}") final String host,
                      @Value("${user.service.port}") final int port) {
        this.requester = builder.rsocketConnector(connectorConfigurer)
                .transport(TcpClientTransport.create(host, port));
    }

    public Mono<TransactionResponse> doTransaction(final TransactionRequest request) {
        return this.requester.route("user.transaction")
                .data(request)
                .retrieveMono(TransactionResponse.class)
                .doOnNext(System.out::println);
    }

    public Flux<UserDto> allUsers() {
        return this.requester.route("user.get.all" )
                .retrieveFlux(UserDto.class);
    }
}
