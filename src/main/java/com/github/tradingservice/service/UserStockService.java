package com.github.tradingservice.service;

import com.github.tradingservice.dto.StockTradeRequest;
import com.github.tradingservice.dto.UserStockDto;
import com.github.tradingservice.entity.UserStock;
import com.github.tradingservice.repository.UserStockRepository;
import com.github.tradingservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserStockService {

    @Autowired
    private UserStockRepository repository;

    public Flux<UserStockDto> getUserStocks(final String userId) {
        return this.repository.findByUserId(userId)
                .map(EntityDtoUtil::toUserStockDto);
    }

    public Mono<UserStock> buyStock(final StockTradeRequest request) {
        return this.repository.findByUserIdAndStockSymbol(request.getUserId(), request.getStockSymbol())
                .defaultIfEmpty(EntityDtoUtil.toUserStock(request))
                .doOnNext(us -> us.setQuantity(us.getQuantity() + request.getQuantity()))
                .flatMap(this.repository::save);
    }

    public Mono<UserStock> sellStock(final StockTradeRequest request) {
        return this.repository.findByUserIdAndStockSymbol(request.getUserId(), request.getStockSymbol())
                .filter(us -> us.getQuantity() >= request.getQuantity())
                .doOnNext(us -> us.setQuantity(us.getQuantity() - request.getQuantity()))
                .flatMap(this.repository::save);
    }
}
