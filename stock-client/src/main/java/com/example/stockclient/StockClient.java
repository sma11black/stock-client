package com.example.stockclient;

import reactor.core.publisher.Flux;

public interface StockClient {
    Flux<StockPrice> pricesFor(String symbol);
}
