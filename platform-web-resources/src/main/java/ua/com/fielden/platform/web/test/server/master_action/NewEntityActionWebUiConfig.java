package ua.com.fielden.platform.web.test.server.master_action;

import com.google.inject.Injector;

import ua.com.fielden.platform.sample.domain.TgPersistentEntityWithProperties;
import ua.com.fielden.platform.web.view.master.EntityMaster;
import ua.com.fielden.platform.web.view.master.api.IMaster;
import ua.com.fielden.platform.web.view.master.api.with_master.impl.MasterWithMaster;

public class NewEntityActionWebUiConfig {

    public static EntityMaster<NewEntityAction> createMaster(final Injector injector, final EntityMaster<TgPersistentEntityWithProperties> master) {
        final IMaster<NewEntityAction> masterWithCentre = new MasterWithMaster<>(NewEntityAction.class, master);
        return new EntityMaster<NewEntityAction>(NewEntityAction.class, NewEntityActionProducer.class, masterWithCentre, injector);
    }
}
