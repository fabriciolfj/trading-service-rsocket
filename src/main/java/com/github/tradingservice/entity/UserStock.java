package com.github.tradingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStock {

    @Id
    private String id;
    private String userId;
    private String stockSymbol;
    private Integer quantity;
}
