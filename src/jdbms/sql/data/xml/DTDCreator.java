package jdbms.sql.data.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import jdbms.sql.data.ColumnIdentifier;
import jdbms.sql.data.TableIdentifier;

public class DTDCreator {
	private static final String DTD_IDENTIFIER = "DTD";
	private static final String DTD_EXTENSION = ".dtd";

	public static void main(String[] args) {
		ArrayList<ColumnIdentifier> columns = new ArrayList<>();
		columns.add(new ColumnIdentifier("Grade", "INTEGER"));
		columns.add(new ColumnIdentifier("Name", "VARCHAR"));
		String db = "MyData";
		TableIdentifier identifier = new TableIdentifier("Students", columns);
//		DTDCreator creator = new DTDCreator(db, identifier);
//		System.out.println(creator.create());
	}

	public DTDCreator() {
	}

	public String create(String database, TableIdentifier identifier, String path) {
		StringBuilder dtdString = new StringBuilder("");
		//dtdString.append("<!DOCTYPE \"Table\" [");
		ArrayList<String> columns = identifier.getColumnNames();
		ArrayList<ColumnIdentifier> columnIdentifiers = identifier.getColumnsIdentifiers();
		dtdString.append("<!ELEMENT ");
		dtdString.append(identifier.getTableName() + " (row)*>");
		dtdString.append("<!ATTLIST " + identifier.getTableName() + " " +"xmlns CDATA #FIXED '' ");
		for (ColumnIdentifier column : columnIdentifiers) {
			dtdString.append(column.getName() +  " NMTOKEN " + "#REQUIRED ");
		}
		dtdString.append('>');
		dtdString.append('\n');
		dtdString.append("<!ELEMENT ");
		dtdString.append("row (");
		for (int i = 0; i < columns.size(); i++) {
			String column = columns.get(i);
			dtdString.append(column);
			if (i != columns.size() - 1) {
				dtdString.append(",");
			}
		}
		dtdString.append(")>");
		dtdString.append('\n');
		dtdString.append("<!ATTLIST row xmlns CDATA #FIXED ''>");
		dtdString.append('\n');
		for (ColumnIdentifier column : columnIdentifiers) {
			dtdString.append("<!ELEMENT " + column.getName() + " (#PCDATA)>");
			dtdString.append("<!ATTLIST " + column.getName() + " xmlns CDATA #FIXED ''>");
			dtdString.append('\n');
		}
		dtdString.append('\n');
		//dtdString.append("]>");
		String dtd = dtdString.toString();
		createFile(database, identifier, dtd, path);
		return dtd;
	}

	public void createFile(String database,TableIdentifier identifier
			, String dtd, String path) {
		File dtdFile = new File(path + database
		+ "/" + identifier.getTableName() + DTD_IDENTIFIER + DTD_EXTENSION);
		try {
			FileWriter writer = new FileWriter(dtdFile);
			writer.write(dtd);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
