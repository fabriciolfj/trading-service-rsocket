package com.github.tradingservice.util;

import com.github.tradingservice.dto.StockTradeRequest;
import com.github.tradingservice.dto.StockTradeResponse;
import com.github.tradingservice.dto.TradeStatus;
import com.github.tradingservice.dto.TradeType;
import com.github.tradingservice.dto.TransactionRequest;
import com.github.tradingservice.dto.TransactionType;
import com.github.tradingservice.entity.UserStock;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static StockTradeResponse toTradeResponse(final StockTradeRequest request, final TradeStatus status, final int price) {
        final StockTradeResponse response = new StockTradeResponse();
        BeanUtils.copyProperties(request, response);
        response.setTradeStatus(status);
        response.setPrice(price);

        return response;
    }

    public static UserStock toUserStock(final StockTradeRequest request) {
        return UserStock.builder()
                .userId(request.getUserId())
                .stockSymbol(request.getStockSymbol())
                .quantity(request.getQuantity())
                .build();
    }

    public static TransactionRequest toTransactionRequest(final StockTradeRequest request, int amount) {
        return TransactionRequest.builder()
                .type(request.getTradeType().equals(TradeType.BUY) ?
                        TransactionType.DEBIT :
                        TransactionType.CREDIT)
                .userId(request.getUserId())
                .amount(amount)
                .build();
    }
}
