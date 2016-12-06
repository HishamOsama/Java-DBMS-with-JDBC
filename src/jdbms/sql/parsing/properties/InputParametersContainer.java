package jdbms.sql.parsing.properties;

import java.util.ArrayList;

import jdbms.sql.data.ColumnIdentifier;
import jdbms.sql.parsing.expressions.math.AssignmentExpression;
import jdbms.sql.parsing.expressions.math.BooleanExpression;

public class InputParametersContainer {
	private String databaseName;
	private BooleanExpression condition;
	private String tableName;
	private ArrayList<String> columns;
	private ArrayList<ArrayList<String>> values;
	private ArrayList<ColumnIdentifier> columnDefinitions;
	private ArrayList<AssignmentExpression> assignmentList;
	private boolean distinct;
	private Boolean isAscending;
	private String sortingColumnName;

	public InputParametersContainer() {
		this.distinct = false;
		this.isAscending = null;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public BooleanExpression getCondition() {
		return condition;
	}

	public void setCondition(BooleanExpression condition) {
		this.condition = condition;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ArrayList<String> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<String> columns) {
		this.columns = columns;
	}

	public ArrayList<ArrayList<String>> getValues() {
		return values;
	}

	public void setValues(ArrayList<ArrayList<String>> values) {
		this.values = values;
	}

	public ArrayList<ColumnIdentifier> getColumnDefinitions() {
		return columnDefinitions;
	}
	public void setColumnDefinitions(ArrayList<ColumnIdentifier> columnDefinitions) {
		this.columnDefinitions = columnDefinitions;
	}

	public ArrayList<AssignmentExpression> getAssignmentList() {
		return assignmentList;
	}

	public void setAssignmentList(ArrayList<AssignmentExpression> assignmentList) {
		this.assignmentList = assignmentList;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return this.distinct;
	}

	public void setAscending(boolean ascending) {
		this.isAscending = ascending;
	}

	public boolean isAscending() {
		return this.isAscending;
	}

	public void setSortingColumnName(String columnName) {
		this.sortingColumnName = columnName;
	}

	public String getSortingColumnName() {
		return this.sortingColumnName;
	}
}
