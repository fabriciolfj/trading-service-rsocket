package com.github.tradingservice.service;

import com.github.tradingservice.client.StockClient;
import com.github.tradingservice.client.UserClient;
import com.github.tradingservice.dto.StockTradeRequest;
import com.github.tradingservice.dto.StockTradeResponse;
import com.github.tradingservice.dto.TradeStatus;
import com.github.tradingservice.dto.TradeType;
import com.github.tradingservice.dto.TransactionRequest;
import com.github.tradingservice.dto.TransactionStatus;
import com.github.tradingservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TradeService {

    @Autowired
    private UserStockService stockService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private StockClient stockClient;

    public Mono<StockTradeResponse> trade(final StockTradeRequest request) {
        var transactionRequest = EntityDtoUtil.toTransactionRequest(request, this.estimatePrice(request));
        var responseMono = TradeType.BUY.equals(request.getTradeType()) ?
                buyStock(request,  transactionRequest) :
                sellStock(request, transactionRequest);

        return responseMono
                .defaultIfEmpty(EntityDtoUtil.toTradeResponse(request, TradeStatus.FAILED, 0));
    }

    private Mono<StockTradeResponse> buyStock(final StockTradeRequest tradeRequest, final TransactionRequest transactionRequest) {
        return this.userClient.doTransaction(transactionRequest)
                .filter(tr -> TransactionStatus.COMPLETED.equals(tr.getStatus()))
                .flatMap(tr -> this.stockService.buyStock(tradeRequest))
                .map(us -> EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.COMPLETED, transactionRequest.getAmount()));
    }

    private Mono<StockTradeResponse>  sellStock(final StockTradeRequest tradeRequest, final TransactionRequest transactionRequest) {
        return this.stockService.sellStock(tradeRequest)
                .flatMap(us -> this.userClient.doTransaction(transactionRequest)
                .map(tr -> EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.COMPLETED, transactionRequest.getAmount())));
    }

    private int estimatePrice(final StockTradeRequest request) {
        return request.getQuantity() * this.stockClient.getCurrentStockPrice(request.getStockSymbol());
    }
}
