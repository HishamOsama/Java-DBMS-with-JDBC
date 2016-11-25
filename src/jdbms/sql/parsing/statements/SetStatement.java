package jdbms.sql.parsing.statements;

import jdbms.sql.parsing.expressions.ConditionalAssignmentListExpression;
import jdbms.sql.parsing.expressions.TerminalAssignmentExpression;
import jdbms.sql.parsing.properties.InputParametersContainer;

public class SetStatement implements Statement {
	private static final String STATEMENT_IDENTIFIER = "SET";
	private InputParametersContainer parameters;
	public SetStatement(InputParametersContainer parameters) {
		this.parameters = parameters;
	}

	@Override
	public boolean interpret(String sqlExpression) {
		if (sqlExpression.startsWith(STATEMENT_IDENTIFIER)) {
			String restOfExpression = sqlExpression.replace(STATEMENT_IDENTIFIER, "").trim();
			return new ConditionalAssignmentListExpression(parameters).interpret(restOfExpression) ||
					new TerminalAssignmentExpression(parameters).interpret(restOfExpression);
		}
		return false;
	}
}
