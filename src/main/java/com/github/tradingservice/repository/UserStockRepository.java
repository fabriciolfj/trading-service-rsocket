package com.github.tradingservice.repository;

import com.github.tradingservice.entity.UserStock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserStockRepository extends ReactiveMongoRepository<UserStock, String> {

    Mono<UserStock> findByUserIdAndStockSymbol(final String userId, final String stockSymbol);
}
