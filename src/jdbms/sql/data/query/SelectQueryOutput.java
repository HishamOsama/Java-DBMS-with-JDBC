package jdbms.sql.data.query;

import java.util.ArrayList;

import jdbms.sql.parsing.util.Constants;

public class SelectQueryOutput {
	private ArrayList<String> columns;
	private ArrayList<ArrayList<String>> outputRows;
	private static final String NULL_PLACEHOLDER = "";
	public SelectQueryOutput() {
		outputRows = new ArrayList<>();
		columns = new ArrayList<>();
	}
	public void setColumns(final ArrayList<String> columns) {
		this.columns = columns;
	}
	public void setRows(final ArrayList<ArrayList<String>> rows) {
		outputRows = rows;
		for (final ArrayList<String> row : outputRows) {
			for (int i = 0  ; i < row.size() ; i++) {
				final String cell = row.get(i);
				if (cell.matches(Constants.DOUBLE_STRING_REGEX) ||
						cell.matches(Constants.STRING_REGEX)) {
					row.set(i, cell.substring(1, cell.length() - 1));
				}
			}
		}
	}
	public void printOutput() {
		final PrettyPrinter printer = new PrettyPrinter(System.out, NULL_PLACEHOLDER);
		printer.print(makeArrays());
	}
	private String[][] makeArrays() {
		final String[][] arr = new String[outputRows.size() + 1][columns.size()];
		columns.toArray(arr[0]);
		int index = 1;
		for (final ArrayList<String> row : outputRows) {
			row.toArray(arr[index]);
			index++;
		}
		return arr;
	}
}
