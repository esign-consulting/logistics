package br.com.esign.logistics.togglz;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.spi.FeatureManagerProvider;

public class EJBSingletonFeatureManagerProvider implements FeatureManagerProvider {

    @Override
    public int priority() {
        return 30;
    }

    @Override
    public FeatureManager getFeatureManager() {
        try {
            InitialContext context = new InitialContext();
            FeatureManagerSingleton singleton = (FeatureManagerSingleton) context.lookup("java:app/logistics-ejb-1.0-SNAPSHOT/FeatureManagerSingleton");
            return singleton.getFeatureManager();
        } catch (NamingException e) {
            return null;
        }
    }

}
