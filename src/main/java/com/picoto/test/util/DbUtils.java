package com.picoto.test.util;
import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.picoto.test.exceptions.TestException;

public class DbUtils {

	public static Connection getConnection(String name) {
		try {
			InitialContext cxt = new InitialContext();
			DataSource ds = (DataSource) cxt.lookup(String.format("java:/comp/env/jdbc/%s", name));
			Connection con = ds.getConnection();
			return con;
		} catch (Exception e) {
			throw new TestException(String.format("Error al conectar a BBDD %s",e.getMessage()));
		}
	}
	
}
