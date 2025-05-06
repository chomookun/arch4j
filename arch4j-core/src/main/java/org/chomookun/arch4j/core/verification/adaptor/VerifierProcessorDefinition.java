package org.chomookun.arch4j.core.verification.adaptor;

public abstract class VerifierProcessorDefinition {

    public abstract String getType();

    public abstract String getName();

    public abstract Class<? extends VerifierProcessor> getTypeClass();

    public abstract String configTemplate();

}
