package jdbms.sql.parsing.statements;

import jdbms.sql.parsing.expressions.columns.types.ColumnTypeExpression;
import jdbms.sql.parsing.properties.InputParametersContainer;

/**
 * The Class AddStatement.
 */
public class AddStatement implements Statement {
	
	private static final String
	STATEMENT_IDENTIFIER = "ADD";
	private InputParametersContainer parameters;
	
	/**
	 * Instantiates a new add statement.*
	 * @param parameters the input parameters
	 */
	public AddStatement(InputParametersContainer parameters) {
		this.parameters = parameters;
	}

	@Override
	public boolean interpret(String sqlExpression) {
		if (sqlExpression.startsWith(STATEMENT_IDENTIFIER)) {
			String restOfExpression = sqlExpression.
					replaceFirst(STATEMENT_IDENTIFIER
							, "").trim();
			return new ColumnTypeExpression(parameters).
					interpret(restOfExpression);
		}
		return false;
	}
}
