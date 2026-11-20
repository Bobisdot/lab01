package kz.iitu.springlab;

import kz.iitu.springlab.domain.Account;
import kz.iitu.springlab.domain.AccountRepository;
import kz.iitu.springlab.domain.AuditRecordRepository;
import kz.iitu.springlab.service.AccountService;
import kz.iitu.springlab.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired private AccountService accountService;
    @Autowired private AccountRepository accountRepository;
    @Autowired private AuditService auditService;
    @Autowired private AuditRecordRepository auditRepository;

    private Long aliceId;
    private Long bobId;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        auditRepository.deleteAll();

        Account alice = accountRepository.save(new Account("Alice", new BigDecimal("1000.00")));
        Account bob = accountRepository.save(new Account("Bob", new BigDecimal("500.00")));

        aliceId = alice.getId();
        bobId = bob.getId();
    }


    @Test
    @DisplayName("Task A1: Successful Money Transfer")
    void testSuccessfulTransfer() {
        accountService.transfer(aliceId, bobId, new BigDecimal("100.00"));

        assertEquals(new BigDecimal("900.00"), accountRepository.findById(aliceId).get().getBalance());
        assertEquals(new BigDecimal("600.00"), accountRepository.findById(bobId).get().getBalance());
    }

    @Test
    @DisplayName("Task A2: Insufficient Funds Rollback")
    void testInsufficientFundsRollback() {
        assertThrows(IllegalStateException.class, () ->
                accountService.transfer(aliceId, bobId, new BigDecimal("2000.00"))
        );

        assertEquals(new BigDecimal("1000.00"), accountRepository.findById(aliceId).get().getBalance());
        assertEquals(new BigDecimal("500.00"), accountRepository.findById(bobId).get().getBalance());
    }

    @Test
    @DisplayName("Task A3: Exception Rollback")
    void testExceptionRollback() {
        assertThrows(RuntimeException.class, () ->
                accountService.transferWithException(aliceId, bobId, new BigDecimal("100.00"))
        );

        assertEquals(new BigDecimal("1000.00"), accountRepository.findById(aliceId).get().getBalance());
    }


    @Test
    @DisplayName("Task D: Optimistic Locking Conflict Test")
    void testOptimisticLockingConflict() {

        Account acc1 = accountRepository.findById(aliceId).get();
        Account acc2 = accountRepository.findById(aliceId).get();

        // 2. Биринши копияны жаңартып, сақтаймыз (version 0-ден 1-ге өседі)
        acc1.credit(new BigDecimal("200.00"));
        accountRepository.saveAndFlush(acc1);


        acc2.debit(new BigDecimal("50.00"));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            accountRepository.saveAndFlush(acc2);
        });
    }
}