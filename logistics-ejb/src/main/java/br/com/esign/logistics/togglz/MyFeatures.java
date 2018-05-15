package br.com.esign.logistics.togglz;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;
import org.togglz.core.manager.FeatureManager;

public enum MyFeatures implements Feature {
    
    @EnabledByDefault
    @Label("Multiple maps remove")
    MULTI_MAPS_REMOVE;
    
    private static final Logger logger = Logger.getLogger(MyFeatures.class.getName());
    
    public boolean isActive() {
        FeatureManager manager = FeatureContext.getFeatureManager();
        boolean isActive = manager.isActive(this);
        logger.log(Level.INFO, "{0}.isActive = {1}", new Object[] {this, isActive});
        return isActive;
    }
    
    public static Map<String, Boolean> features() {
        Map<String, Boolean> features = new HashMap<>();
        for (MyFeatures feature : values()) {
            features.put(feature.name(), feature.isActive());
        }
        return features;
    }

}
