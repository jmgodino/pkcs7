package com.picoto.test.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;



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
			InitialContext cxt = new InitialContext();
			DataSource ds = (DataSource) cxt.lookup( "java:/comp/env/jdbc/test" );
			Connection con = ds.getConnection();
			PreparedStatement ps = con.prepareStatement("select id, descripcion from test");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				resp.getWriter().println(rs.getInt(1)+" "+rs.getString(2));
			}
			rs.close();
			ps.close();
			con.close();

		} catch (Exception e) {
			resp.getWriter().println(e.toString());
		}
	}
	
}
