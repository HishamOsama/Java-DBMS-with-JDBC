package jdbms.sql.parsing.statements;

import jdbms.sql.parsing.expressions.DatabaseTerminatingExpression;
import jdbms.sql.parsing.properties.DatabaseCreationParameters;
import jdbms.sql.parsing.statements.util.InitialStatementFactory;

public class CreateDatabaseStatement extends InitialStatement {
	private static final String STATEMENT_IDENTIFIER = "CREATE DATABASE";
	private static final String CLASS_ID = "CREATEDATABASESTATEMENTCLASS";
	private DatabaseCreationParameters createDBParameters;

	static {
		InitialStatementFactory.getInstance().registerStatement(CLASS_ID, CreateDatabaseStatement.class);
	}

	public CreateDatabaseStatement() {
		createDBParameters = new DatabaseCreationParameters();
	}

	@Override
	public boolean interpret(String sqlExpression) {
		if (sqlExpression.startsWith(STATEMENT_IDENTIFIER)) {
			String restOfExpression = sqlExpression.replace(STATEMENT_IDENTIFIER, "").trim();
			return new DatabaseTerminatingExpression(parameters).interpret(restOfExpression);
		}
		return false;
	}

	@Override
	public void act() {
		buildParameters();
		//System.createDatabase(dbParameters);
	}
	private void buildParameters() {
		createDBParameters.setDatabaseName(parameters.getDatabaseName());
	}
}
