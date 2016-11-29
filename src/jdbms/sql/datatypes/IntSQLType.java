package jdbms.sql.datatypes;

import jdbms.sql.datatypes.util.SQLTypeFactory;

public class IntSQLType extends SQLType<Long> {
	static {
		SQLTypeFactory.getInstance().
		registerType("INTEGER", IntSQLType.class);
		SQLTypeFactory.getInstance().
		registerType("INT", IntSQLType.class);
	}
	public IntSQLType(final String value) {
		super(value == null ? null : Long.parseLong(value));
	}
	@Override
	public String getType() {
		return "INTEGER";
	}
	@Override
	public String getStringValue() {
		if (value == null) {
			return "";
		}
		return value.toString();
	}
}
