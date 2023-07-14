package com.github.tradingservice.controller;

import com.github.tradingservice.client.UserClient;
import com.github.tradingservice.dto.UserDto;
import com.github.tradingservice.dto.UserStockDto;
import com.github.tradingservice.service.UserStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;
    private final UserStockService userStockService;

    @GetMapping("/all")
    public Flux<UserDto> allUsers() {
        return this.userClient.allUsers();
    }

    @GetMapping("/{userId}/stocks")
    public Flux<UserStockDto> getUserStocks(@PathVariable final String userId) {
        return this.userStockService.getUserStocks(userId);
    }
}
