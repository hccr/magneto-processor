package cl.hccr.service.magnetoprocessor.repository;

import cl.hccr.service.magnetoprocessor.domain.MutantRequest;

public interface MutantRequestDAO {
    boolean insert(MutantRequest request);
    boolean isHashIndex(int dnaHashCode);
}
