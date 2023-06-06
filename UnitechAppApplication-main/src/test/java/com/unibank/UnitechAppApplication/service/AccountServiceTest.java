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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void testGetAllActiveAccounts() {
        String token = "sampleToken";
        User user = new User();
        user.setId(1L);
        when(jwtTokenProvider.getPin(token)).thenReturn("samplePin");
        when(userRepository.findByPin("samplePin")).thenReturn(Optional.of(user));

        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(BigDecimal.valueOf(123));
        account1.setStatus(Account.Status.ACTIVE);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(BigDecimal.valueOf(55));
        account2.setStatus(Account.Status.ACTIVE);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);
        when(accountRepository.findByUserIdAndStatus(1L, Account.Status.ACTIVE)).thenReturn(accounts);

        List<AccountResponse> response = accountService.getAllActiveAccounts(token);

        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals(2L, response.get(1).getId());
    }

    @Test
    void testTransferAmountToAccount_SuccessfulTransfer() {
        String token = "sampleToken";
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        when(jwtTokenProvider.getPin(token)).thenReturn("samplePin");
        when(userRepository.findByPin("samplePin")).thenReturn(Optional.of(user));

        Account fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(100));
        fromAccount.setUser(user);
        fromAccount.setStatus(Account.Status.ACTIVE);

        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.ZERO);
        toAccount.setUser(user2);
        toAccount.setStatus(Account.Status.ACTIVE);

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAccountFrom(1L);
        transferRequest.setAccountTo(2L);
        transferRequest.setAmount(BigDecimal.valueOf(50));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        TransferResponse response = accountService.transferAmountToAccount(token, transferRequest);

        assertEquals("Transfer completed successfully.", response.getSuccess());
        assertEquals(BigDecimal.valueOf(50), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(50), toAccount.getBalance());
        verify(accountRepository, times(1)).save(fromAccount);
        verify(accountRepository, times(1)).save(toAccount);
    }

    @Test
    void testTransferAmountToAccount_InvalidTransfer() {
        String token = "sampleToken";
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        when(jwtTokenProvider.getPin(token)).thenReturn("samplePin");
        when(userRepository.findByPin("samplePin")).thenReturn(Optional.of(user));

        Account fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(100));
        fromAccount.setUser(user);

        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.ZERO);
        toAccount.setUser(user2);
        toAccount.setStatus(Account.Status.DEACTIVE);

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAccountFrom(1L);
        transferRequest.setAccountTo(2L);
        transferRequest.setAmount(BigDecimal.valueOf(200));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(BadRequestException.class,
            () -> accountService.transferAmountToAccount(token, transferRequest),
            "You can not make transfer to inactive account"
        );
        assertEquals(BigDecimal.valueOf(100), fromAccount.getBalance());
        assertEquals(BigDecimal.ZERO, toAccount.getBalance());
        verify(accountRepository, never()).save(fromAccount);
        verify(accountRepository, never()).save(toAccount);
    }
}