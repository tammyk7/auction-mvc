package com.adaptive.mockserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketDataRequest {
    String action;
    String[] trades;
    String[] quotes;
    String[] bars;
}
