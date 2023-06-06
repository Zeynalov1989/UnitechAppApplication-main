package com.unibank.UnitechAppApplication.service;

import com.unibank.UnitechAppApplication.config.jwt.JwtTokenProvider;
import com.unibank.UnitechAppApplication.exception.BadRequestException;
import com.unibank.UnitechAppApplication.model.Account;
import com.unibank.UnitechAppApplication.model.User;
import com.unibank.UnitechAppApplication.repository.AccountRepository;
import com.unibank.UnitechAppApplication.repository.UserRepository;
import com.unibank.UnitechAppApplication.model.dto.request.TransferRequest;
import com.unibank.UnitechAppApplication.model.dto.response.AccountResponse;
import com.unibank.UnitechAppApplication.model.dto.response.TransferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public List<AccountResponse> getAllActiveAccounts(String token) {
        User user = userRepository.findByPin(jwtTokenProvider.getPin(token)).orElse(null);
        List<AccountResponse> response = new ArrayList<>();
        accountRepository.findByUserIdAndStatus(user.getId(), Account.Status.ACTIVE)
            .forEach(account -> response.add(new AccountResponse(account)));
        return response;
    }

    @Transactional
    public TransferResponse transferAmountToAccount(String token, TransferRequest transferRequest) {
        User user = userRepository.findByPin(jwtTokenProvider.getPin(token)).orElse(null);

        if (transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Transfer amount must be greater than 0.");
        }

        Account fromAccount = accountRepository.findById(transferRequest.getAccountFrom()).orElse(null);
        Account toAccount = accountRepository.findById(transferRequest.getAccountTo()).orElse(null);

        if (fromAccount == null || toAccount == null) {
            throw new BadRequestException("One or both of the accounts do not exist.");
        }

        if (fromAccount.getUser().getId() != user.getId()) {
            throw new BadRequestException("You do not have permission to transfer from this account.");
        }

        if (fromAccount.getId() == toAccount.getId()) {
            throw new BadRequestException("You can not transfer amount to the same account.");
        }

        if (toAccount.getStatus().equals(Account.Status.DEACTIVE)) {
            throw new BadRequestException("You can not make transfer to inactive account");
        }

        if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new BadRequestException("Insufficient funds in the balance.");
        }

        BigDecimal transferAmount = transferRequest.getAmount();

        fromAccount.setBalance(fromAccount.getBalance().subtract(transferAmount));
        toAccount.setBalance(toAccount.getBalance().add(transferAmount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return new TransferResponse("Transfer completed successfully.");
    }
}
