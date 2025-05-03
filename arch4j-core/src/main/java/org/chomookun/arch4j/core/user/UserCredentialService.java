package org.chomookun.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base32;
import org.chomookun.arch4j.core.user.entity.UserCredentialEntity;
import org.chomookun.arch4j.core.user.model.UserCredential;
import org.chomookun.arch4j.core.user.repository.UserCredentialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserCredential saveCredential(UserCredential userCredential) {
        UserCredentialEntity userCredentialEntity;
        if (userCredential.getUserId() != null) {
            userCredentialEntity = userCredentialRepository.findByUserIdAndType(userCredential.getUserId(), userCredential.getType()).orElseThrow();
        } else {
            userCredentialEntity = UserCredentialEntity.builder()
                    .userId(userCredential.getUserId())
                    .type(userCredential.getType())
                    .build();
        }
        userCredentialEntity.setChangedAt(Instant.now());
        userCredentialEntity.setCredential(userCredential.getCredential());
        UserCredentialEntity savedUserCredentialEntity = userCredentialRepository.saveAndFlush(userCredentialEntity);
        return UserCredential.from(savedUserCredentialEntity);
    }

    /**
     * Retrieves user credential by user id and type
     * @param userId user id
     * @param type credential type
     * @return user credential
     */
    public Optional<UserCredential> getCredential(String userId, UserCredential.Type type) {
        return userCredentialRepository.findByUserIdAndType(userId, type)
                .map(UserCredential::from);
    }

    /**
     * Deletes user credential by user id
     * @param userId user id
     */
    @Transactional
    public void deleteCredentials(String userId) {
        userCredentialRepository.deleteByUserId(userId);
    }

    /**
     * Generates password credential
     * @param password password
     * @return password credential
     */
    public String generatePasswordCredential(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Checks if password is matched
     * @param userId user id
     * @param password password
     * @return whether password is matched or not
     */
    public boolean isPasswordCredentialMatched(String userId, String password) {
        UserCredentialEntity userCredentialEntity = userCredentialRepository.findByUserIdAndType(userId, UserCredential.Type.PASSWORD).orElseThrow();
        return passwordEncoder.matches(password, userCredentialEntity.getCredential());
    }

    /**
     * Changes password
     * @param userId user id
     * @param newPassword new password
     */
    @Transactional
    public void changePasswordCredential(String userId, String newPassword) {
        userCredentialRepository.findByUserIdAndType(userId, UserCredential.Type.PASSWORD).ifPresent(userCredentialEntity -> {
            userCredentialEntity.setCredential(passwordEncoder.encode(newPassword));
            userCredentialEntity.setChangedAt(Instant.now());
            userCredentialRepository.saveAndFlush(userCredentialEntity);
        });
    }

    /**
     * Generates TOTP secret
     * @return TOTP secret
     */
    public String generateTotpCredential() {
        // creates 160 bit (20 bytes) random
        byte[] buffer = new byte[20];
        new SecureRandom().nextBytes(buffer);
        // base32 encoding
        Base32 base32 = new Base32();
        return base32.encodeToString(buffer).replace("=", "");
    }

}
