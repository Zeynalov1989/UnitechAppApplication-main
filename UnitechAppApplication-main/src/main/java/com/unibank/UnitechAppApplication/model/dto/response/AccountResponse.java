package com.unibank.UnitechAppApplication.model.dto.response;

import com.unibank.UnitechAppApplication.model.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    private long id;
    private double balance;
    private String status;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance().doubleValue();
        this.status = String.valueOf(account.getStatus());
    }
}
