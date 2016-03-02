package ua.com.fielden.platform.security.user;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import ua.com.fielden.platform.entity.AbstractFunctionalEntityForCollectionModification;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.security.user.User;
import ua.com.fielden.platform.security.user.UserRole;

/** 
 * Master entity object.
 * 
 * @author Developers
 *
 */
@CompanionObject(IUserRolesUpdater.class)
@KeyType(User.class)
@MapEntityTo
@KeyTitle(value = "User", desc = "User, whose 'roles' collection modifies by this functional action.")
public class UserRolesUpdater extends AbstractFunctionalEntityForCollectionModification<User, Long> {
    private static final long serialVersionUID = 1L;
    
    @IsProperty(UserRole.class)
    @Title(value = "A list of applicable roles", desc = "A list of applicable roles")
    private Set<UserRole> roles = new LinkedHashSet<UserRole>();

    @Observable
    protected UserRolesUpdater setRoles(final Set<UserRole> roles) {
        this.roles.clear();
        this.roles.addAll(roles);
        return this;
    }

    public Set<UserRole> getRoles() {
        return Collections.unmodifiableSet(roles);
    }
}
