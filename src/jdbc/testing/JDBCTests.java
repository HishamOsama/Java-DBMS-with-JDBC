package jdbc.testing;

import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import jdbc.TestRunner;
import jdbc.drivers.DBDriver;
import jdbc.results.DataResultSet;

public class JDBCTests {
	private final String protocol = "altdb";
	private final String tmp = System.getProperty("java.io.tmpdir");

	public static Class<?> getSpecifications() {
		return Driver.class;
	}

	private Connection createUseDatabase(final String databaseName) throws SQLException {
		final Driver driver = (DBDriver) TestRunner.getImplementationInstance();
		final Properties info = new Properties();
		final File dbDir = new File(tmp + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
		info.put("path", dbDir.getAbsoluteFile());
		final Connection connection = driver.connect("jdbc:" + protocol + "://localhost", info);
		final Statement statement = connection.createStatement();
		statement.execute("CREATE DATABASE " + databaseName);
		statement.execute("USE " + databaseName);
		statement.close();
		return connection;
	}

	@Test
	public void testSelectAll() throws SQLException {
		final Connection connection = createUseDatabase("School");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("Create table Student (ID int, Name varchar, Grade float)");
			int count = statement.executeUpdate("INSERT INTO Student (ID, Name, Grade)"
					+ " VALUES (1 ,'Ahmed Khaled', 90.5)");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			count  = statement.executeUpdate("INSERT INTO Student (ID, Name, Grade)"
					+ " VALUES (2 ,'Ahmed El Naggar', 90.2)");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			final ResultSet resultSet = statement.executeQuery("SELECT * FROM Student");
			resultSet.next();
			Assert.assertEquals("Failed to get Correct Float Value",
					90.5, resultSet.getFloat("Grade"), 0.0001);
		} catch (final SQLException e) {
			TestRunner.fail("Failed to select all from table", e);
		}
		connection.close();
	}

	@Test
	public void testSelectWhere() throws SQLException {
		final Connection connection = createUseDatabase("School");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("Create table Student (ID int, Name varchar, Grade float)");
			int count = statement.executeUpdate("INSERT INTO Student (ID, Name, Grade)"
					+ " VALUES (1 ,'Ahmed Khaled', 90.5)");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			count  = statement.executeUpdate("INSERT INTO Student (ID, Name, Grade)"
					+ " VALUES (2 ,'Ahmed El Naggar', 90.2)");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			count  = statement.executeUpdate("INSERT INTO Student (ID, Name, Grade)"
					+ " VALUES (3 ,'Ahmed Walid', 90.5)");
			count  = statement.executeUpdate("INSERT INTO Student (ID, Name, Grade)"
					+ " VALUES (4 ,'Anas Harby', 90.5)");
			final DataResultSet resultSet = (DataResultSet)
					statement.executeQuery("SELECT * FROM Student WHERE ID > 1");
			int numberOfMatches = 0;
			while (resultSet.next()) {
				numberOfMatches++;
			}
			Assert.assertEquals("Invalid Result Set Size", 3, numberOfMatches);
		} catch (final SQLException e) {
			TestRunner.fail("Failed to select conditional from table", e);
		}
		connection.close();
	}

	@Test
	public void testDateTime() throws SQLException {
		final Connection connection = createUseDatabase("sqlDatabase");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("Create table tb (ID int, Name varchar, Grade float, Birth datetime)");
			int count = statement.executeUpdate("INSERT INTO tb (ID, Name, Grade, birth)"
					+ " VALUES (-30, 'hello', -.366, '2001-10-10 02:01:01')");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			count  = statement.executeUpdate("INSERT INTO tb"
					+ " VALUES (-2 ,'A spaced string', 101.00002, '0001-01-01 01:01:01')");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			count  = statement.executeUpdate("INSERT INTO tb"
					+ " VALUES (333 ,'a float is .003', .003, '8488-11-30 05:06:07')");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			final ResultSet resultSet =
					statement.executeQuery("SELECT * FROM tb WHERE birth != '1111-11-11 11:11:11'");
			int rows = 0;
			while (resultSet.next()) {
				rows++;
			}
			Assert.assertEquals("Invalid Result Set Size", 3, rows);
		} catch (final SQLException e) {
			TestRunner.fail("Failed to select from table, testing date time", e);
		}
		connection.close();
	}

	@Test
	public void testMetaData() throws SQLException {
		final Connection connection = createUseDatabase("sqlDatabase");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("Create table tb (ID int, Name varchar, GradE float, Birth date)");
			int count = statement.executeUpdate("INSERT INTO tb (ID, Name, GrAde, birth)"
					+ " VALUES (-30, 'hello', -.366, '2001-10-10')");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			count  = statement.executeUpdate("INSERT INTO tb"
					+ " VALUES (-2 ,'A spaced string', 101.00002, '0001-01-01')");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			count  = statement.executeUpdate("INSERT INTO tb"
					+ " VALUES (333 ,'a float is .003', .003, '8488-11-30')");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			final ResultSet resultSet = statement.executeQuery("select birth, gRAde, id from tb where "
					+ "birth > '0001-01-01' order by id");
			int rows = 0;
			while (resultSet.next()) {
				rows++;
			}
			Assert.assertEquals("Invalid Result Set Size", 2, rows);
			Assert.assertEquals(Types.DATE, resultSet.
					getMetaData().getColumnType(resultSet.findColumn("BirTh")));
			Assert.assertEquals(Types.FLOAT, resultSet.
					getMetaData().getColumnType(resultSet.findColumn("gRAdE")));
			Assert.assertEquals(Types.INTEGER, resultSet.
					getMetaData().getColumnType(resultSet.findColumn("iD")));
			Assert.assertEquals(3, resultSet.getMetaData().getColumnCount());
			Assert.assertTrue(resultSet.getMetaData().getTableName(1).equalsIgnoreCase("Tb"));
			Assert.assertTrue(resultSet.getMetaData().
					getColumnLabel(1).equalsIgnoreCase("birth"));
		} catch (final SQLException e) {
			TestRunner.fail("Failed to select from table, testing date time", e);
		}
		connection.close();
	}

	@Test
	public void testResultSetGettersOrderBy() throws SQLException {
		final Connection connection = createUseDatabase("sqlDatabase");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("Create table tb (CurrentTime dateTime,"
					+ " Name varchar, GradE float, Birth date)");
			int count = statement.executeUpdate("INSERT INTO tb (Name,"
					+ " GrAde, birth, currentTime)"
					+ " VALUES ('hello', -.366, '2001-10-10', '2000-09-03 11:02:09')");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			count  = statement.executeUpdate("INSERT INTO tb"
					+ " VALUES ('2001-09-03 11:02:09','A spaced string', 101.00002, '0001-01-01')");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			count  = statement.executeUpdate("INSERT INTO tb"
					+ " VALUES ('2001-09-03 11:02:09','a float is .003', .003, '8488-11-30')");
			Assert.assertEquals("Table Insertion did not return 1", 1, count);
			final ResultSet resultSet = statement.executeQuery("select biRth, gRAde, cuRRentTiMe, naMe from tb where "
					+ "currenttime > '0001-01-01 01:01:01' order by currenttime, grade desc");
			resultSet.first();
			Assert.assertEquals(resultSet.getString(4), "hello");
			Assert.assertEquals(resultSet.getFloat(2), -0.366, 0.001);
			resultSet.next();
			Assert.assertEquals(resultSet.getString("name"), "A spaced string");
			Assert.assertEquals(resultSet.getFloat("grade"), 101.00002, 0.001);
			try {
				Assert.assertEquals(resultSet.getFloat("name"), 101.00002, 0.001);
			} catch (final SQLException e) {
			}

		} catch (final SQLException e) {
			TestRunner.fail("Failed to select from table, testing date time", e);
		}
		connection.close();
	}

	@Test
	public void testCreateTable() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 date)");
			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to create table", e);
		}
		try {
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 date)");
			Assert.fail("Created existing table successfully!");
		} catch (final SQLException e) {

		} catch (final Throwable e) {
			TestRunner.fail("Invalid Exception thrown", e);
		}

		try {
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE incomplete_table_name1");
			Assert.fail("Create invalid table succeed");
		} catch (final SQLException e) {
		} catch (final Throwable e) {
			TestRunner.fail("Invalid Exception thrown", e);
			fail("kkg");
		}
		connection.close();
	}

	@Test
	public void testInsertWithoutColumnNames() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name3(column_name1 varchar, column_name2 int, column_name3 float)");
			final int count = statement.executeUpdate("INSERT INTO table_name3 VALUES ('value1', 3, 1.3)");
			Assert.assertEquals("Insert returned a number != 1", 1, count);
			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to insert into table", e);
		}
		connection.close();
	}

	@Test
	public void testInsertWithColumnNames() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name4(column_name1 varchar, column_name2 int, column_name3 date)");
			final int count = statement.executeUpdate(
					"INSERT INTO table_name4(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', '2011-01-25', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count);
			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to insert into table", e);
		}
		connection.close();
	}

	@Test
	public void testInsertWithWrongColumnNames() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name5(column_name1 varchar, column_name2 int, column_name3 varchar)");
			statement.executeUpdate(
					"INSERT INTO table_name5(invalid_column_name1, column_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.fail("Inserted with invalid column name!!");
			statement.close();
		} catch (final SQLException e) {
		} catch (final Throwable e) {
			TestRunner.fail("Invalid Exception thrown", e);
		}
		connection.close();
	}

	@Test
	public void testUpdate() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name7(column_name1 varchar, column_name2 int, column_name3 varchar)");
			final int count1 = statement.executeUpdate(
					"INSERT INTO table_name7(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);
			final int count2 = statement.executeUpdate(
					"INSERT INTO table_name7(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count2);
			final int count3 = statement.executeUpdate(
					"INSERT INTO table_name7(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);
			final int count4 = statement.executeUpdate(
					"UPDATE table_name7 SET column_name1='1111111111', COLUMN_NAME2=2222222, column_name3='333333333'");
			Assert.assertEquals("Updated returned wrong number", count1 + count2 + count3, count4);
			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to update table", e);
		}
		connection.close();
	}

	@Test
	public void testConditionalUpdate() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement.execute(
					"CREATE TABLE table_name8(column_name1 varchar, column_name2 int, column_name3 date, column_name4 float)");

			final int count1 = statement.executeUpdate(
					"INSERT INTO table_name8(column_NAME1, COLUMN_name3, column_name2, column_name4) VALUES ('value1', '2011-01-25', 3, 1.3)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);

			final int count2 = statement.executeUpdate(
					"INSERT INTO table_name8(column_NAME1, COLUMN_name3, column_name2, column_name4) VALUES ('value1', '2011-01-28', 3456, 1.01)");
			Assert.assertEquals("Insert returned a number != 1", 1, count2);

			final int count3 = statement.executeUpdate(
					"INSERT INTO table_name8(column_NAME1, COLUMN_name3, column_name2, column_name4) VALUES ('value2', '2011-02-11', -123, 3.14159)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);

			final int count4 = statement.executeUpdate(
					"UPDATE table_name8 SET COLUMN_NAME2=222222, column_name3='1993-10-03' WHERE coLUmn_NAME1='value1'");
			Assert.assertEquals("Updated returned wrong number", count1 + count2, count4);

			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to update table", e);
		}
		connection.close();
	}

	@Test
	public void testUpdateEmptyOrInvalidTable() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name9(column_name1 varchar, column_name2 int, column_name3 varchar)");
			final int count = statement.executeUpdate(
					"UPDATE table_name9 SET column_name1='value1', column_name2=15, column_name3='value2'");
			Assert.assertEquals("Updated empty table retruned non-zero count!", 0, count);
			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to update table", e);
		}

		try {
			final Statement statement = connection.createStatement();
			statement.executeUpdate(
					"UPDATE wrong_table_name9 SET column_name1='value1', column_name2=15, column_name3='value2'");
			Assert.fail("Updated empty table retruned non-zero count!");
			statement.close();
		} catch (final SQLException e) {
		} catch (final Throwable e) {
			TestRunner.fail("Invalid exception was thrown", e);
		}
		connection.close();
	}

	@Test
	public void testDelete() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name10(column_name1 varchar, column_name2 int, column_name3 date)");
			final int count1 = statement.executeUpdate(
					"INSERT INTO table_name10(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', '2011-01-25', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);
			final int count2 = statement.executeUpdate(
					"INSERT INTO table_name10(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', '2011-01-28', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count2);
			final int count3 = statement.executeUpdate(
					"INSERT INTO table_name10(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', '2011-02-11', 5)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);
			final int count4 = statement.executeUpdate("DELETE From table_name10");
			Assert.assertEquals("Delete returned wrong number", 3, count4);
			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to delete from table", e);
		}
		connection.close();
	}

	@Test
	public void testConditionalDelete() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name11(column_name1 varchar, column_name2 int, column_name3 DATE)");
			final int count1 = statement.executeUpdate(
					"INSERT INTO table_name11(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', '2011-01-25', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);
			final int count2 = statement.executeUpdate(
					"INSERT INTO table_name11(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', '2013-06-30', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count2);
			final int count3 = statement.executeUpdate(
					"INSERT INTO table_name11(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', '2013-07-03', 5)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);
			final int count4 = statement.executeUpdate("DELETE From table_name11  WHERE coLUmn_NAME3>'2011-01-25'");
			Assert.assertEquals("Delete returned wrong number", 2, count4);
			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to delete from table", e);
		}
		connection.close();
	}

	@Test
	public void testSelect() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name12(column_name1 varchar, column_name2 int, column_name3 varchar)");
			final int count1 = statement.executeUpdate(
					"INSERT INTO table_name12(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);
			final int count2 = statement.executeUpdate(
					"INSERT INTO table_name12(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count2);
			final int count3 = statement.executeUpdate(
					"INSERT INTO table_name12(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);
			final int count4 = statement.executeUpdate(
					"INSERT INTO table_name12(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertEquals("Insert returned a number != 1", 1, count4);
			final ResultSet result = statement.executeQuery("SELECT * From table_name12");
			int rows = 0;
			while (result.next())
				rows++;
			Assert.assertNotNull("Null result retruned", result);
			Assert.assertEquals("Wrong number of rows", 4, rows);
			Assert.assertEquals("Wrong number of columns", 3, result.getMetaData().getColumnCount());
			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to select from table", e);
		}
		connection.close();
	}

	@Test
	public void testConditionalSelect() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
			final int count1 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);
			final int count2 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value3')");
			Assert.assertEquals("Insert returned a number != 1", 1, count2);
			final int count3 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);
			final int count4 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertEquals("Insert returned a number != 1", 1, count4);
			final ResultSet result = statement.executeQuery("SELECT column_name1 FROM table_name13 WHERE coluMN_NAME2 < 5");
			int rows = 0;
			while (result.next())
				rows++;
			Assert.assertNotNull("Null result retruned", result);
			Assert.assertEquals("Wrong number of rows", 2, rows);
			Assert.assertEquals("Wrong number of columns", 1, result.getMetaData().getColumnCount());
			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to select from table", e);
		}
		connection.close();
	}

	@Test
	public void testExecute() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
			final int count1 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);
			final boolean result1 = statement.execute(
					"INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 8, 'value3')");
			Assert.assertFalse("Wrong return from 'execute' for insert record", result1);
			final int count3 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);
			final int count4 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertEquals("Insert returned a number != 1", 1, count4);

			final boolean result2 = statement.execute("SELECT column_name1 FROM table_name13 WHERE coluMN_NAME2 = 8");
			Assert.assertTrue("Wrong return for select existing records", result2);

			final boolean result3 = statement.execute("SELECT column_name1 FROM table_name13 WHERE coluMN_NAME2 > 100");
			Assert.assertFalse("Wrong return for select non existing records", result3);

			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to select from table", e);
		}
		connection.close();
	}

	@Test
	public void testDistinct() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
			final int count1 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);
			final boolean result1 = statement.execute(
					"INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value5')");
			Assert.assertFalse("Wrong return for insert record", result1);
			final int count3 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);
			final int count4 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertEquals("Insert returned a number != 1", 1, count4);

			final boolean result2 = statement.execute("SELECT DISTINCT column_name2 FROM table_name13");
			Assert.assertTrue("Wrong return for select existing records", result2);
			final ResultSet res1 = statement.getResultSet();

			int rows = 0;
			while (res1.next())
				rows++;
			Assert.assertEquals("Wrong number of rows", 3, rows);

			final boolean result3 = statement
					.execute("SELECT DISTINCT column_name2, column_name3 FROM table_name13 WHERE coluMN_NAME2 < 5");
			Assert.assertTrue("Wrong return for select existing records", result3);
			final ResultSet res2 = statement.getResultSet();

			int rows2 = 0;
			while (res2.next())
				rows2++;
			Assert.assertEquals("Wrong number of rows", 2, rows2);

			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to select distinct from table", e);
		}
		connection.close();
	}

	@Test
	public void testAlterTable() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
			final int count1 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);
			final boolean result1 = statement.execute(
					"INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value5')");
			Assert.assertFalse("Wrong return for insert record", result1);
			final int count3 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);
			final int count4 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertEquals("Insert returned a number != 1", 1, count4);

			final boolean result2 = statement.execute("ALTER TABLE table_name13 ADD column_name4 date");
			Assert.assertFalse("Wrong return for ALTER TABLE", result2);

			final boolean result3 = statement.execute("SELECT column_name4 FROM table_name13 WHERE coluMN_NAME2 = 5");
			Assert.assertTrue("Wrong return for select existing records", result3);
			final ResultSet res2 = statement.getResultSet();
			int rows2 = 0;
			while (res2.next())
				rows2++;
			Assert.assertEquals("Wrong number of rows", 1, rows2);

			while (res2.previous())
				;
			res2.next();

			Assert.assertNull("Retrieved date is not null", res2.getDate("column_name4"));

			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to test ALTER TABLE from table", e);
		}
		connection.close();
	}

	@Test
	public void testOrderBy() throws SQLException {
		final Connection connection = createUseDatabase("TestDB_Create");
		try {
			final Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
			final int count1 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);
			final boolean result1 = statement.execute(
					"INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value5')");
			Assert.assertFalse("Wrong return for insert record", result1);
			final int count3 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);
			final int count4 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertEquals("Insert returned a number != 1", 1, count4);

			final boolean result3 = statement
					.execute("SELECT * FROM table_name13 ORDER BY column_name2 ASC, COLUMN_name3 DESC");
			Assert.assertTrue("Wrong return for select UNION existing records", result3);
			final ResultSet res2 = statement.getResultSet();
			int rows2 = 0;
			while (res2.next())
				rows2++;
			Assert.assertEquals("Wrong number of rows", 4, rows2);

			while (res2.previous())
				;

			res2.next();
			Assert.assertEquals("Wrong order of rows", 4, res2.getInt("column_name2"));
			Assert.assertEquals("Wrong order of rows", "value5", res2.getString("column_name3"));

			res2.next();
			Assert.assertEquals("Wrong order of rows", 4, res2.getInt("column_name2"));
			Assert.assertEquals("Wrong order of rows", "value3", res2.getString("column_name3"));

			res2.next();
			Assert.assertEquals("Wrong order of rows", 5, res2.getInt("column_name2"));

			statement.close();
		} catch (final Throwable e) {
			TestRunner.fail("Failed to test ORDER By", e);
		}
		connection.close();
	}
}
