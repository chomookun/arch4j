package org.chomookun.arch4j.core.verification.verifier;

import org.chomookun.arch4j.core.common.pbe.PbePropertiesUtil;
import org.chomookun.arch4j.core.verification.model.Verification;

import java.lang.reflect.Constructor;
import java.util.Properties;

public abstract class VerifierDefinition {

    public abstract String getType();

    public abstract String getName();

    public abstract Class<? extends Verifier> getTypeClass();

    public abstract String configTemplate();

}
