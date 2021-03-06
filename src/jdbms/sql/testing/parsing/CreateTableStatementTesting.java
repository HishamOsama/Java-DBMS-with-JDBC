package jdbms.sql.testing.parsing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import jdbms.sql.data.ColumnIdentifier;
import jdbms.sql.parsing.parser.StringNormalizer;
import jdbms.sql.parsing.statements.CreateTableStatement;
import jdbms.sql.parsing.statements.InitialStatement;
import jdbms.sql.util.ClassRegisteringHelper;

public class CreateTableStatementTesting {

    private StringNormalizer normalizer;
    private InitialStatement createTable;

    @Before
    public void executedBeforeEach() {
        normalizer = new StringNormalizer();
        createTable = new CreateTableStatement();
        ClassRegisteringHelper.registerInitialStatements();
    }

    @Test
    public void testCreateTable() {
        String sqlCommand = "CREATE TABLE NEWTABLE (ID INTEGER, AGE INTEGER, "
                + "NAME TEXT) ;";
        sqlCommand = normalizer.normalizeCommand(sqlCommand);
        ArrayList<ColumnIdentifier> columnId = new ArrayList<>();
        String name = "NEWTABLE";
        ColumnIdentifier cd = new ColumnIdentifier("ID", "INTEGER");
        columnId.add(cd);
        cd = new ColumnIdentifier("AGE", "INTEGER");
        columnId.add(cd);
        cd = new ColumnIdentifier("NAME", "TEXT");
        columnId.add(cd);
        assertEquals(createTable.interpret(sqlCommand), true);
        assertEquals(createTable.getParameters().getTableName(), name);
        assertEquals(createTable.getParameters().getColumnDefinitions(),
                columnId);
    }

    @Test
    public void tesFloatCreateTable() {
        String sqlCommand = "CREATE TABLE NEWTABLE (ID floAt, AGE reAl, "
                + "Coolness REAL) ;";
        sqlCommand = normalizer.normalizeCommand(sqlCommand);
        ArrayList<ColumnIdentifier> columnId = new ArrayList<>();
        String name = "NEWTABLE";
        columnId.add(new ColumnIdentifier("ID", "FLOAT"));
        columnId.add(new ColumnIdentifier("AGE", "REAL"));
        columnId.add(new ColumnIdentifier("Coolness", "REAL"));
        assertEquals(createTable.interpret(sqlCommand), true);
        assertEquals(createTable.getParameters().getTableName(), name);
        assertEquals(createTable.getParameters().getColumnDefinitions(),
                columnId);
    }

    @Test
    public void testInvalidCreateTable() {
        String sqlCommand = "CREATE TABLE (ID INTEGER, AGE INTEGER, NAME "
                + "TEXT) ;";
        sqlCommand = normalizer.normalizeCommand(sqlCommand);
        assertEquals(createTable.interpret(sqlCommand), false);
    }
}
