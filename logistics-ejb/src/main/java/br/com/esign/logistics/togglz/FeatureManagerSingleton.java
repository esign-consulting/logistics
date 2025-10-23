package br.com.esign.logistics.togglz;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.mem.InMemoryStateRepository;
import org.togglz.core.user.SimpleFeatureUser;

@Singleton
public class FeatureManagerSingleton {

    private FeatureManager featureManager;

    @PostConstruct
    public void init() {
        featureManager = new FeatureManagerBuilder()
                .featureEnum(MyFeatures.class)
                .stateRepository(new InMemoryStateRepository())
                .userProvider(() -> new SimpleFeatureUser("admin", true))
                .build();
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }

}
