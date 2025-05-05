package org.chomookun.arch4j.core.verification.verifier;

import org.chomookun.arch4j.core.common.pbe.PbePropertiesUtil;
import org.chomookun.arch4j.core.verification.model.Verification;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.Properties;

public abstract class VerifierFactory {

    public abstract Class<? extends Verifier> getTypeClass();

    public Verifier getVerifier(Verification verification) {
        VerifierDefinition verifierDefinition = VerifierDefinitionRegistry.getVerifierDefinition(verification.getVerifierType()).orElseThrow();
        try {
            Constructor<? extends Verifier> constructor = verifierDefinition.getTypeClass().getConstructor(Properties.class);
            Properties config = loadPropertiesFromString(verification.getVerifierConfig());
            return constructor.newInstance(config);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Verifier constructor not found: " + verification.getVerifierType(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Properties loadPropertiesFromString(String config) {
        return PbePropertiesUtil.loadProperties(config);
    }

}
