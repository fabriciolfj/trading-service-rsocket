package com.github.tradingservice.dto;

import lombok.Data;

@Data
public class StockTradeRequest {

    private String userId;
    private String stockSymbol;
    private int quantity;
    private TradeType tradeType;
}
