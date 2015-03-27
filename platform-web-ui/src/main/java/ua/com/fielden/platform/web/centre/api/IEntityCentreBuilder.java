package ua.com.fielden.platform.web.centre.api;

import ua.com.fielden.platform.basic.IValueMatcherWithCentreContext;
import ua.com.fielden.platform.basic.autocompleter.FallbackValueMatcherWithCentreContext;
import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.sample.domain.TgWorkOrder;
import ua.com.fielden.platform.web.centre.api.calc.IEnhanceEntityWithCalcProps;

/**
 * This contract is an entry point for an Entity Centre aPI -- an embedded domain specific language for constructing entity centres.
 *
 * @see <a href="https://github.com/fieldenms/tg/issues/140">Specification</a>
 *
 * @author TG Team
 *
 */
public interface IEntityCentreBuilder<T extends AbstractEntity<?>> {

    /**
     * Entity centre construction DSL entry point.
     *
     * @param type
     * @return
     */
    IEnhanceEntityWithCalcProps<T> forEntity(Class<T> type);


    /**
     * This is just an example for Entity Centre DSL.
     *
     * @param args
     */
    public static void main(final String[] args) {
       final IEntityCentreBuilder<TgWorkOrder> ecb = null;
       ecb.forEntity(TgWorkOrder.class)
       .addCrit("status").asMulti().autocompleter().withMatcher(MyClass.class)
       .also()
       .addCrit("intValue").asRange().integer()
       .also()
       .addCrit("intValueCritOnly").asSingle().integer()
       .also()
       .addCrit("statusCritOnly").asSingle().autocompleter();
    }

    // TODO Serves for an API example purposes. Should be removed as soon as API gets implemented.
    public static class MyClass extends FallbackValueMatcherWithCentreContext<TgWorkOrder> {

        public MyClass(final IEntityDao<TgWorkOrder> dao) {
            super(dao);
            // TODO Auto-generated constructor stub
        }

    }

}
