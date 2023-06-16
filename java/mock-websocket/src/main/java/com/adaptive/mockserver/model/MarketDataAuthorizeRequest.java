package com.adaptive.mockserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketDataAuthorizeRequest {
    String action;
    String key;
    String secret;
}
