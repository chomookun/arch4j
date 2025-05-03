package org.chomookun.arch4j.core.verification.verifier;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class VerifierFactory {

    private final Map<String, Verifier> verifierMap = new LinkedHashMap<>();


    public VerifierFactory(List<Verifier> verifies) {
        for (Verifier verifier : verifies) {
            verifierMap.put(verifier.getType(), verifier);
        }
    }

    public List<String> getAvailableTypes() {
        return List.copyOf(verifierMap.keySet());
    }

    public Verifier getVerifier(String type) {
        if (verifierMap.containsKey(type)) {
            return verifierMap.get(type);
        }
        throw new IllegalArgumentException(String.format("Invalid Verifier type: %s", type));
    }

}
