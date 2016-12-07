package jdbms.sql.parsing.expressions.util;

import jdbms.sql.parsing.util.Constants;

/**
 * The Class Value Expression.
 */
public class ValueExpression {

	/** The expression. */
	private String expression;
	
	/**
	 * Instantiates a new value expression.
	 * @param expression the value expression
	 */
	public ValueExpression(String expression) {
		this.expression = expression.trim();
	}

	/**
	 * Checks if is valid expression name.
	 * @return true, if is valid expression name
	 */
	public boolean isValidExpressionName() {
		return this.expression.matches(Constants.INT_REGEX)
				|| this.expression.matches(Constants.STRING_REGEX)
				|| this.expression.matches(Constants.DOUBLE_STRING_REGEX)
				|| this.expression.matches(Constants.FLOAT_REGEX);
	}
	
	/**
	 * Gets the value expression.
	 * @return the value expression
	 */
	public String getExpression() {
		return expression;
	}
}