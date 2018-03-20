package br.com.esign.logistics.togglz;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;
import org.togglz.core.manager.FeatureManager;

public enum MyFeatures implements Feature {
    
    @EnabledByDefault
    @Label("First Feature")
    FEATURE_ONE,
    @Label("Second Feature")
    FEATURE_TWO;
    
    private static final Logger logger = Logger.getLogger(MyFeatures.class.getName());
    
    public boolean isActive() {
        FeatureManager manager = FeatureContext.getFeatureManager();
        boolean isActive = manager.isActive(this);
        logger.log(Level.INFO, "{0}.isActive = {1}", new Object[] {this, isActive});
        return isActive;
    }

}
