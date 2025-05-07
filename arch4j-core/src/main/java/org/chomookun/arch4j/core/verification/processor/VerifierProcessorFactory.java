package org.chomookun.arch4j.core.verification.processor;

import org.chomookun.arch4j.core.common.pbe.PbePropertiesUtil;
import org.chomookun.arch4j.core.verification.model.Verifier;

import java.lang.reflect.Constructor;
import java.util.Properties;

public abstract class VerifierProcessorFactory {

    public abstract Class<? extends VerifierProcessor> getTypeClass();

    public VerifierProcessor getObject(Verifier verifier) {
        VerifierProcessorDefinition verifierDefinition = VerifierProcessorDefinitionRegistry.getDefinition(verifier.getProcessorType()).orElseThrow();
        try {
            Constructor<? extends VerifierProcessor> constructor = verifierDefinition.getTypeClass().getConstructor(Properties.class);
            Properties config = loadPropertiesFromString(verifier.getProcessorConfig());
            return constructor.newInstance(config);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Verifier constructor not found: " + verifier.getProcessorType(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Properties loadPropertiesFromString(String config) {
        return PbePropertiesUtil.loadProperties(config);
    }

}
