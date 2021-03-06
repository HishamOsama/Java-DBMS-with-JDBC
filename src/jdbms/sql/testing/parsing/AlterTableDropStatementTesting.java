package jdbms.sql.testing.parsing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import jdbms.sql.parsing.parser.StringNormalizer;
import jdbms.sql.parsing.statements.AlterTableDropStatement;
import jdbms.sql.parsing.statements.InitialStatement;
import jdbms.sql.util.ClassRegisteringHelper;

public class AlterTableDropStatementTesting {

	private StringNormalizer normalizer;
	private InitialStatement alterDrop;

	@Before
	public void executedBeforeEach() {
		normalizer = new StringNormalizer();
		alterDrop = new AlterTableDropStatement();
		ClassRegisteringHelper.registerInitialStatements();
	}
	@Test
	public void testAlterTableDropColumn() {
		String sqlCommand = "ALTER TABLE table_name DROP COLUMN col1, col2, col3;";
		sqlCommand = normalizer.normalizeCommand(sqlCommand);
		final String name = "table_name";
		final ArrayList <String> list = new ArrayList<>();
		list.add("col1");
		list.add("col2");
		list.add("col3");
		assertEquals(alterDrop.interpret(sqlCommand), true);
		assertEquals(name, alterDrop.getParameters().getTableName());
		assertEquals(list, alterDrop.getParameters().getColumns());
	}

	@Test
	public void testAlterTableAddColumn() {
		String sqlCommand = "ALTER TABLE table_name ADD column_name INT;";
		sqlCommand = normalizer.normalizeCommand(sqlCommand);
		assertEquals(alterDrop.interpret(sqlCommand), false);
	}
}
