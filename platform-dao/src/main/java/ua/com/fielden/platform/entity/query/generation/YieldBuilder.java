package ua.com.fielden.platform.entity.query.generation;

import java.util.Map;

import ua.com.fielden.platform.entity.query.fluent.TokenCategory;
import ua.com.fielden.platform.entity.query.generation.elements.ISingleOperand;
import ua.com.fielden.platform.entity.query.generation.elements.YieldModel;
import ua.com.fielden.platform.utils.Pair;

public class YieldBuilder extends AbstractTokensBuilder {

    protected YieldBuilder(final AbstractTokensBuilder parent, final DbVersion dbVersion, final Map<String, Object> paramValues) {
	super(parent, dbVersion, paramValues);
    }

    @Override
    public boolean isClosing() {
	return getSize() == 2;
    }

    @Override
    public Pair<TokenCategory, Object> getResult() {
	final ISingleOperand operand = getModelForSingleOperand(firstCat(), firstValue());
	final String alias = (String) secondValue();
	return new Pair<TokenCategory, Object>(TokenCategory.QRY_YIELD, new YieldModel(operand, alias == null ? "" : alias));
    }
}