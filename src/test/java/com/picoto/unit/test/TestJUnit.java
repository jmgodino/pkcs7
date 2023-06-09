package com.picoto.unit.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.picoto.jdbc.wrapper.JdbcNamedWrapper;
import com.picoto.jdbc.wrapper.JdbcWrapperFactory;
import com.picoto.test.domain.TestDomain;
import com.picoto.test.util.DbUtils;

@ExtendWith(MockitoExtension.class)
public class TestJUnit {

	@Mock
	TestDomain testMock;

	@Test
	public void testDataSource() throws SQLException {
		try (MockedStatic<DbUtils> utilities = Mockito.mockStatic(DbUtils.class)) {
			ResultSet resultSet = Mockito.mock(ResultSet.class);
			Mockito.when(resultSet.next()).thenReturn(true);
			Mockito.when(resultSet.getInt(1)).thenReturn(1);
			Mockito.when(resultSet.getString(2)).thenReturn("Test 1");

			Statement statement = Mockito.mock(Statement.class);
			Mockito.when(statement.executeQuery("SELECT * FROM test")).thenReturn(resultSet);

			Connection jdbcConnection = Mockito.mock(Connection.class);
			Mockito.when(jdbcConnection.createStatement()).thenReturn(statement);
			
			utilities.when(() -> DbUtils.getConnection(anyString())).thenReturn(jdbcConnection);

			ResultSet rs = DbUtils.getConnection("test").createStatement().executeQuery("SELECT * FROM test");
			rs.next();	
			assertEquals(rs.getInt(1),1);
			assertEquals(rs.getString(2),"Test 1");

		}
		
	}

	@Test
	public void testQuery() {
		assertNotNull(testMock);
		when(testMock.getId()).thenReturn(1);
		assertEquals(1, testMock.getId());
	}
	
	
	@Test
	public void testQueryHsql() throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:mem:testdb;INIT=RUNSCRIPT FROM 'classpath:test.sql'","sa","");
		JdbcNamedWrapper<TestDomain> testWrap = JdbcWrapperFactory.getJdbcNamedWrapper(TestDomain.class, con, false,
				true);
		assertTrue(testWrap.count("select count(*) from test") == 2);
		
	}
}
