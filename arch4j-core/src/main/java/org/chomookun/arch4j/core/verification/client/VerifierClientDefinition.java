package org.chomookun.arch4j.core.verification.client;

public abstract class VerifierClientDefinition {

    public abstract String getClientType();

    public abstract String getName();

    public abstract Class<? extends VerifierClient> getClassType();

    public abstract String getPropertiesTemplate();

}
