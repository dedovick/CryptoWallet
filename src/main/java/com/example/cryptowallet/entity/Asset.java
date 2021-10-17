package com.example.cryptowallet.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Asset {
    private String symbol;
    private Double quantity;
    private Double price;
}
