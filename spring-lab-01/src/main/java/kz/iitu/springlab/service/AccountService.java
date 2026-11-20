package kz.iitu.springlab.service;

import kz.iitu.springlab.domain.Account;
import kz.iitu.springlab.domain.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Account from = repository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + fromId));
        Account to = repository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + toId));

        from.debit(amount);
        to.credit(amount);

        repository.save(from);
        repository.save(to);
    }

    // Task A #3: Exception лақтырып, Rollback-ті тексеруге арналған метод
    @Transactional
    public void transferWithException(Long fromId, Long toId, BigDecimal amount) {
        Account from = repository.findById(fromId).orElseThrow();
        Account to = repository.findById(toId).orElseThrow();

        from.debit(amount);
        repository.save(from);

        if (true) {
            throw new RuntimeException("Simulated error after debiting, before crediting!");
        }

        to.credit(amount);
        repository.save(to);
    }
}