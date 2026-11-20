package kz.iitu.springlab.service;

import kz.iitu.springlab.domain.AuditRecord;
import kz.iitu.springlab.domain.AuditRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditRecordRepository auditRepository;

    public AuditService(AuditRecordRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    // REQUIRES_NEW арқылы бөлек транзакция ашамыз
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logActionRequiresNew(String action) {
        auditRepository.save(new AuditRecord(action));
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void logActionRequired(String action) {
        auditRepository.save(new AuditRecord(action));
    }
}