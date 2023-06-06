package com.unibank.UnitechAppApplication.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private Long accountFrom;
    private Long accountTo;
    private BigDecimal amount;
}
