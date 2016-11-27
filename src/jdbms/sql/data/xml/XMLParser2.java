//package jdbms.sql.data.xml;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import jdbms.sql.data.ColumnIdentifier;
//import jdbms.sql.data.Table;
//import jdbms.sql.data.TableIdentifier;
//import jdbms.sql.exceptions.ColumnAlreadyExistsException;
//import jdbms.sql.exceptions.ColumnListTooLargeException;
//import jdbms.sql.exceptions.ColumnNotFoundException;
//import jdbms.sql.exceptions.RepeatedColumnException;
//import jdbms.sql.exceptions.TypeMismatchException;
//import jdbms.sql.exceptions.ValueListTooLargeException;
//import jdbms.sql.exceptions.ValueListTooSmallException;
//import jdbms.sql.parsing.properties.InsertionParameters;
//
//public class XMLParser2 {
////
////	private TableIdentifier tableIdentifier;
////	private Table table;
////	private ArrayList<ColumnIdentifier> columnIdentifiers;
////	private Set<String> columns;
////	private String path;
////	public static ArrayList<String> columnNames;
//
////	public static void main(String[] args) {
////		try {
////			Class.forName("jdbms.sql.parsing.statements.CreateDatabaseStatement");
////			Class.forName("jdbms.sql.parsing.statements.CreateTableStatement");
////			Class.forName("jdbms.sql.parsing.statements.DropDatabaseStatement");
////			Class.forName("jdbms.sql.parsing.statements.DropTableStatement");
////			Class.forName("jdbms.sql.parsing.statements.InsertIntoStatement");
////			Class.forName("jdbms.sql.parsing.statements.DeleteStatement");
////			Class.forName("jdbms.sql.parsing.statements.SelectStatement");
////			Class.forName("jdbms.sql.parsing.statements.UpdateStatement");
////			Class.forName("jdbms.sql.parsing.statements.UseStatement");
////			Class.forName("jdbms.sql.parsing.expressions.math.EqualsExpression");
////			Class.forName("jdbms.sql.parsing.expressions.math.LargerThanEqualsExpression");
////			Class.forName("jdbms.sql.parsing.expressions.math.LessThanEqualsExpression");
////			Class.forName("jdbms.sql.parsing.expressions.math.LargerThanExpression");
////			Class.forName("jdbms.sql.parsing.expressions.math.LessThanExpression");
////			Class.forName("jdbms.sql.parsing.expressions.math.NotEqualsExpression");
////			Class.forName("jdbms.sql.datatypes.IntSQLType");
////			Class.forName("jdbms.sql.datatypes.VarcharSQLType");
////		} catch (ClassNotFoundException e) {
////			e.printStackTrace();
////		}
////		ArrayList<ColumnIdentifier> columns = new ArrayList<>();
////		columns.add(new ColumnIdentifier("Name", "VARCHAR"));
////		columns.add(new ColumnIdentifier("ID", "INTEGER"));
////		Database parent = new Database("School");
////		TableIdentifier identifier = new TableIdentifier("Students", columns);
//////		XMLParser2 parser2 = new XMLParser2(identifier, null);
//////		parser2.parse();
//////		Table test = parser2.geTable();
//////		ArrayList<TableColumn> data = test.getColumnList(columnNames);
//////		for (int i = 0; i < data.size(); i++) {
//////			TableColumn col = data.get(i);
//////			for (int j = 0; j < col.getSize(); j++) {
//////				System.out.println(col.get(j).getStringValue());
//////			}
//////		}
////	}
//////         /DatabaseName/
//	public XMLParser2() {
//
//	}
//
//	private void initializeTable(String tablename, String databaseName, String path) {
//		DTDParser parser = new DTDParser(new File(path + tablename + "DTD.dtd"));
//		TableIdentifier identifier = parser.parse();
//		this.path = path;
//		String tableName = identifier.getTableName();
//		columnIdentifiers = identifier.getColumnsIdentifiers();
//		columns = new HashSet<>(identifier.getColumnNames());
//		try {
//			table = new Table(identifier);
//		} catch (ColumnAlreadyExistsException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		columnNames = new ArrayList<>();
//		for (ColumnIdentifier col : columnIdentifiers) {
////			try {
////				//table.addTableColumn(col.getName(), col.getType());
////			} catch (ColumnAlreadyExistsException e) {
////				e.printStackTrace();
////				//ErrorHandler.printColumnAlreadyExistsColumn();
////			}
//			columnNames.add(col.getName());
//		}
//	}
//
//	public Table parse(String tablename, String databaseName, String path) {
//		initializeTable(tablename, databaseName, path);
//		try {
//			File inputFile = new File(path + table.getName() + ".xml");
//			DocumentBuilderFactory dbFactory
//			= DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc;
//			doc = dBuilder.parse(inputFile);
//			doc.getDocumentElement().normalize();
//			String tableName = doc.getDocumentElement().getNodeName();
//			 NodeList nList = doc.getElementsByTagName(tableName);
//			 ArrayList<ArrayList<String>> values
//			 = new ArrayList<>();
//			 for (int i = 0; i < nList.getLength(); i++) {
//				 Node node = nList.item(i);
//				 if (node.getNodeType() == Node.ELEMENT_NODE) {
//					 Element element = (Element) node;
//					 ArrayList<String> value = new ArrayList<>();
//					 for (String column : columnNames) {
//						 String text = (element)
//				                  .getElementsByTagName(column)
//				                  .item(0)
//				                  .getTextContent();
//						 value.add(text);
//					 }
//					 values.add(value);
//				 }
//			 }
//			 InsertionParameters insert = new InsertionParameters();
//			 insert.setColumns(columnNames);
//			 insert.setValues(values);
//			 insert.setTableName(tableName);
//			 table.insertRows(insert);
//			 return table;
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (RepeatedColumnException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ColumnListTooLargeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ColumnNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ValueListTooLargeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ValueListTooSmallException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TypeMismatchException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//}
