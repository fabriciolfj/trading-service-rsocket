package com.github.tradingservice.controller;

import com.github.tradingservice.client.StockClient;
import com.github.tradingservice.dto.StockTickDto;
import com.github.tradingservice.dto.StockTradeRequest;
import com.github.tradingservice.dto.StockTradeResponse;
import com.github.tradingservice.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;
    private final StockClient stockClient;

    @GetMapping(value = "/tick/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockTickDto> stockTickDtoFlux() {
        return this.stockClient.getStockStream();
    }

    @PostMapping("/trade")
    public Mono<ResponseEntity<StockTradeResponse>> trade(@RequestBody final Mono<StockTradeRequest> tradeRequestMono) {
        return tradeRequestMono
                .filter(tr -> tr.getQuantity() > 0)
                .flatMap(tradeService::trade)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
