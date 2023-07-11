package com.github.tradingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTradeResponse {

    private String userId;
    private String stockSymbol;
    private int quantity;
    private TradeType tradeType;
    private TradeStatus tradeStatus;
    private int price;
}
