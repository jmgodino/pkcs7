package com.picoto.test.web;

import java.io.IOException;
import java.util.List;

import com.picoto.jdbc.wrapper.JdbcNamedWrapper;
import com.picoto.jdbc.wrapper.JdbcWrapperFactory;
import com.picoto.test.domain.TestDomain;
import com.picoto.test.exceptions.TestException;
import com.picoto.test.util.DbUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebServlet(urlPatterns = "/TestServlet", displayName = "Test")
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = -6186219472085351504L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {

			JdbcNamedWrapper<TestDomain> testWrap = JdbcWrapperFactory.getJdbcNamedWrapper(TestDomain.class, DbUtils.getConnection("testdb"), false,
					true);

			List<TestDomain> tests = testWrap.query(
					"select id, descripcion from test",  c -> {
						TestDomain t = new TestDomain();
						t.setId(c.getInt(1));
						t.setDescripcion(c.getString(2));
						return t;
					});

			testWrap.debug("* Libros recuperados por ISBN y título");
			for (TestDomain t : tests) {
				debug(resp, t.toString());
			}
			

		} catch (Exception e) {
			debug (resp, e.getMessage());
		}
	}

	private void debug(HttpServletResponse resp, String str) {
		try {
			resp.getWriter().println(str);
		} catch (Exception e) {
			throw new TestException(String.format("Error al conectar al escribir la respuesta %s",e.getMessage()));
		}
		
	}
	
}
