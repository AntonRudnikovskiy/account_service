package faang.school.accountservice.service;

import faang.school.accountservice.dto.account.CreateAccountDto;
import faang.school.accountservice.dto.account.ResponseAccountDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public ResponseAccountDto open(CreateAccountDto createAccountDto) {
        Account account = accountRepository.save(accountMapper.createDtoToEntity(createAccountDto));
       account.setBalance(Balance.builder()
               .account(account)
                      .currentBalance(BigDecimal.ZERO)
               .authorizationBalance(BigDecimal.ZERO)
               .currentBalance(BigDecimal.ZERO)
                .build());
        log.info("Created Account. Id: {}", account.getId());
        return accountMapper.entityToResponseDto(account);
    }

    @Transactional(readOnly = true)
    public ResponseAccountDto get(long id) {
        return accountMapper.entityToResponseDto(getAccount(id));
    }

    @Transactional
    public void block(long id) {
        Account account = getAccount(id);
        account.setStatus(AccountStatus.FROZEN);
        log.info("Blocked Account. Id: {}", account.getId());
        accountRepository.save(account);
    }

    @Transactional
    public void close(long id) {
        Account account = getAccount(id);
        account.setStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
        log.info("Closed Account. Id: {}", account.getId());
        accountRepository.save(account);
    }

    private Account getAccount(long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found. Id" + id));
    }
}
