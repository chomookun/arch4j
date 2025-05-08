package org.chomookun.arch4j.core.verification.client;

import org.chomookun.arch4j.core.common.pbe.PbePropertiesUtil;
import org.chomookun.arch4j.core.verification.model.Verifier;

import java.lang.reflect.Constructor;
import java.util.Properties;

public abstract class VerifierClientFactory {

    public abstract Class<? extends VerifierClient> getTypeClass();

    public VerifierClient getObject(Verifier verifier) {
        VerifierClientDefinition verifierDefinition = VerifierClientDefinitionRegistry.getDefinition(verifier.getClientType()).orElseThrow();
        try {
            Constructor<? extends VerifierClient> constructor = verifierDefinition.getClassType().getConstructor(Properties.class);
            Properties config = loadPropertiesFromString(verifier.getClientProperties());
            return constructor.newInstance(config);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Verifier constructor not found: " + verifier.getClientType(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Properties loadPropertiesFromString(String config) {
        return PbePropertiesUtil.loadProperties(config);
    }

}
