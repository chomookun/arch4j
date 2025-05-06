package org.chomookun.arch4j.core.verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.verification.entity.VerifierEntity;
import org.chomookun.arch4j.core.verification.model.Verifier;
import org.chomookun.arch4j.core.verification.model.VerifierSearch;
import org.chomookun.arch4j.core.verification.repository.VerifierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerifierService {

    private final VerifierRepository verifierRepository;

    public Verifier saveVerifier(Verifier verifier) {
        VerifierEntity verifierEntity = verifierRepository.findById(verifier.getVerifierId()).orElse(null);
        if (verifierEntity == null) {
            verifierEntity = VerifierEntity.builder()
                    .verifierId(verifier.getVerifierId())
                    .build();
        }
        verifierEntity.setName(verifier.getName());
        verifierEntity.setProcessorType(verifier.getProcessorType());
        verifierEntity.setProcessorConfig(verifier.getProcessorConfig());
        verifierEntity.setEnabled(verifier.isEnabled());
        verifierEntity.setNote(verifier.getNote());
        VerifierEntity savedVerifierEntity = verifierRepository.saveAndFlush(verifierEntity);
        return Verifier.from(savedVerifierEntity);
    }

    public Page<Verifier> getVerifiers(VerifierSearch verifierSearch, Pageable pageable) {
        Page<VerifierEntity> verifierEntityPage = verifierRepository.findAll(verifierSearch, pageable);
        List<Verifier> verifiers = verifierEntityPage.getContent().stream()
                .map(Verifier::from)
                .toList();
        long total = verifierEntityPage.getTotalElements();
        return new PageImpl<>(verifiers, pageable, total);
    }

    public Optional<Verifier> getVerifier(String verificationId) {
        return verifierRepository.findById(verificationId)
                .map(Verifier::from);
    }

    public void deleteVerifier(String verificationId) {
        VerifierEntity verifierEntity = verifierRepository.findById(verificationId).orElseThrow();
        verifierRepository.delete(verifierEntity);
        verifierRepository.flush();
    }

}
