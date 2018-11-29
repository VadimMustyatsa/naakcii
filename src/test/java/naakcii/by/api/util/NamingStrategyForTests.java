package naakcii.by.api.util;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class NamingStrategyForTests extends PhysicalNamingStrategyStandardImpl {
	
	private static final long serialVersionUID = -2602880813200030898L;

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		return new Identifier("TEST_T_" + name.getText(), name.isQuoted());
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
		return new Identifier("TEST_F_" + name.getText(), name.isQuoted());
	}
}
