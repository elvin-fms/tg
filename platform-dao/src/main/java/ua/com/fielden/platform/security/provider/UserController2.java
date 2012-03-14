package ua.com.fielden.platform.security.provider;

import java.util.ArrayList;
import java.util.List;

import ua.com.fielden.platform.dao.annotations.SessionRequired;
import ua.com.fielden.platform.dao2.CommonEntityDao2;
import ua.com.fielden.platform.dao2.IUserAndRoleAssociationDao2;
import ua.com.fielden.platform.dao2.IUserRoleDao2;
import ua.com.fielden.platform.dao2.QueryExecutionModel;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.query.fetch;
import ua.com.fielden.platform.entity.query.fetchAll;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity.query.model.OrderingModel;
import ua.com.fielden.platform.security.user.User;
import ua.com.fielden.platform.security.user.UserAndRoleAssociation;
import ua.com.fielden.platform.security.user.UserRole;
import ua.com.fielden.platform.swing.review.annotations.EntityType;

import com.google.inject.Inject;

import static ua.com.fielden.platform.entity.query.fluent.query.orderBy;
import static ua.com.fielden.platform.entity.query.fluent.query.select;


/**
 * Implementation of the user controller, which should be used managing system user information.
 *
 * @author TG Team
 *
 */
@EntityType(User.class)
public class UserController2 extends CommonEntityDao2<User> implements IUserController2 {

    private final IUserRoleDao2 userRoleDao;
    private final IUserAndRoleAssociationDao2 userAssociationDao;

    @SuppressWarnings("unchecked")
    private final fetch<User> fetchModel = new fetch(User.class).with("roles", new fetch(UserAndRoleAssociation.class).with("userRole"));

    @Inject
    public UserController2(final IUserRoleDao2 userRoleDao, final IUserAndRoleAssociationDao2 userAssociationDao, final IFilter filter) {
	super(filter);
	this.userRoleDao = userRoleDao;
	this.userAssociationDao = userAssociationDao;
    }

    @Override
    public List<? extends UserRole> findAllUserRoles() {
	return userRoleDao.findAll();
    }

    @Override
    public List<User> findAllUsers() {
	return findAllUsersWithRoles();
    }

    @Override
    @SessionRequired
    public void updateUser(final User user, final List<UserRole> checkedRoles) {
	// remove list at the first stage of the algorithm contains the associations of the given user.
	// At the last stage of the algorithm that list contains only associations those must be removed from the data base
	final List<UserAndRoleAssociation> removeList = new ArrayList<UserAndRoleAssociation>(user.getRoles());
	// contains the list of associations those must be saved
	final List<UserAndRoleAssociation> saveList = new ArrayList<UserAndRoleAssociation>();

	for (int userRoleIndex = 0; userRoleIndex < checkedRoles.size(); userRoleIndex++) {
	    final UserAndRoleAssociation roleAssociation = user.getEntityFactory().newByKey(UserAndRoleAssociation.class, user, checkedRoles.get(userRoleIndex));
	    if (!removeList.contains(roleAssociation)) {
		saveList.add(roleAssociation);
	    } else {
		removeList.remove(roleAssociation);
	    }
	}

	// first remove user/role associations
	userAssociationDao.removeAssociation(removeList);
	// then insert new user/role associations
	saveAssociation(saveList);
    }

    private void saveAssociation(final List<UserAndRoleAssociation> associations) {
	for (final UserAndRoleAssociation association : associations) {
	    userAssociationDao.save(association);
	}
    }

    @Override
    public User findUserByIdWithRoles(final Long id) {
	return findById(id, fetchModel);
    }

    @Override
    public User findUserByKeyWithRoles(final String key) {
	final EntityResultQueryModel<User> model = select(User.class).where().prop("key").eq().val(key).model();
	return getEntity(new QueryExecutionModel.Builder<User>(model).fetchModel(fetchModel).build());
    }

    @Override
    public List<User> findAllUsersWithRoles() {
	final EntityResultQueryModel<User> model = select(User.class).where().prop("key").isNotNull().model();
	final OrderingModel orderBy = orderBy().prop("key").asc().model();
	return getEntities(new QueryExecutionModel.Builder<User>(model).fetchModel(fetchModel).orderModel(orderBy).build());
    }

    @Override
    public User findUser(final String username) {
	return findByKeyAndFetch(new fetchAll<User>(User.class), username);
    }
}