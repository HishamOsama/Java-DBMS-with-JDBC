package jdbms.sql.datatypes;

import jdbms.sql.datatypes.util.SQLTypeFactory;
/**
 * A java object representing the
 * SQL Integer type.
 * @author Mohammed Abdelbarry
 */
public class IntSQLType extends SQLType<Integer> {
	static {
		SQLTypeFactory.getInstance().
		registerType("INTEGER", IntSQLType.class);
		SQLTypeFactory.getInstance().
		registerType("INT", IntSQLType.class);
	}
	public IntSQLType(final String value) {
		super(value == null ? null : Integer.parseInt(value));
	}
	@Override
	public String getType() {
		return "INTEGER";
	}
}
