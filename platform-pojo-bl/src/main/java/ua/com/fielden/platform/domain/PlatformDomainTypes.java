package ua.com.fielden.platform.domain;

import java.util.ArrayList;
import java.util.List;

import ua.com.fielden.platform.attachment.Attachment;
import ua.com.fielden.platform.attachment.EntityAttachmentAssociation;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.EntityDeleteAction;
import ua.com.fielden.platform.entity.EntityEditAction;
import ua.com.fielden.platform.entity.EntityExportAction;
import ua.com.fielden.platform.entity.EntityNewAction;
import ua.com.fielden.platform.entity.functional.centre.CentreContextHolder;
import ua.com.fielden.platform.entity.functional.centre.SavingInfoHolder;
import ua.com.fielden.platform.entity.functional.master.AcknowledgeWarnings;
import ua.com.fielden.platform.entity.functional.master.PropertyWarning;
import ua.com.fielden.platform.keygen.KeyNumber;
import ua.com.fielden.platform.menu.CustomView;
import ua.com.fielden.platform.menu.EntityCentreView;
import ua.com.fielden.platform.menu.EntityMasterView;
import ua.com.fielden.platform.menu.Menu;
import ua.com.fielden.platform.menu.MenuSaveAction;
import ua.com.fielden.platform.menu.Module;
import ua.com.fielden.platform.menu.ModuleMenuItem;
import ua.com.fielden.platform.menu.View;
import ua.com.fielden.platform.menu.WebMenuItemInvisibility;
import ua.com.fielden.platform.migration.MigrationError;
import ua.com.fielden.platform.migration.MigrationHistory;
import ua.com.fielden.platform.migration.MigrationRun;
import ua.com.fielden.platform.security.session.UserSession;
import ua.com.fielden.platform.security.user.SecurityRoleAssociation;
import ua.com.fielden.platform.security.user.SecurityTokenInfo;
import ua.com.fielden.platform.security.user.User;
import ua.com.fielden.platform.security.user.UserAndRoleAssociation;
import ua.com.fielden.platform.security.user.UserRole;
import ua.com.fielden.platform.security.user.UserRoleTokensUpdater;
import ua.com.fielden.platform.security.user.UserRolesUpdater;
import ua.com.fielden.platform.ui.config.EntityCentreAnalysisConfig;
import ua.com.fielden.platform.ui.config.EntityCentreConfig;
import ua.com.fielden.platform.ui.config.EntityLocatorConfig;
import ua.com.fielden.platform.ui.config.EntityMasterConfig;
import ua.com.fielden.platform.ui.config.MainMenuItem;
import ua.com.fielden.platform.web.centre.CentreConfigUpdater;
import ua.com.fielden.platform.web.centre.SortingProperty;

public class PlatformDomainTypes {
    public static final List<Class<? extends AbstractEntity<?>>> types = new ArrayList<Class<? extends AbstractEntity<?>>>();

    static {
        types.add(MainMenuItem.class);
        types.add(User.class);
        types.add(UserRolesUpdater.class);
        types.add(UserSession.class);
        types.add(UserRole.class);
        types.add(UserRoleTokensUpdater.class);
        types.add(SecurityTokenInfo.class);
        types.add(CentreConfigUpdater.class);
        types.add(SortingProperty.class);
        types.add(UserAndRoleAssociation.class);
        types.add(SecurityRoleAssociation.class);
        types.add(EntityCentreConfig.class);
        types.add(EntityCentreAnalysisConfig.class);
        types.add(EntityMasterConfig.class);
        types.add(EntityLocatorConfig.class);
        types.add(Attachment.class);
        types.add(EntityAttachmentAssociation.class);
        types.add(KeyNumber.class);
        types.add(MigrationRun.class);
        types.add(MigrationHistory.class);
        types.add(MigrationError.class);
        types.add(CentreContextHolder.class);
        types.add(SavingInfoHolder.class);
        types.add(EntityNewAction.class);
        types.add(EntityEditAction.class);
        types.add(EntityDeleteAction.class);
        types.add(EntityExportAction.class);
        types.add(AcknowledgeWarnings.class);
        types.add(PropertyWarning.class);
        types.add(ModuleMenuItem.class);
        types.add(EntityCentreView.class);
        types.add(View.class);
        types.add(CustomView.class);
        types.add(Module.class);
        types.add(Menu.class);
        types.add(EntityMasterView.class);
        types.add(MenuSaveAction.class);
        types.add(WebMenuItemInvisibility.class);
    }
}
