package com.bjsxt.registration.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.bjsxt.registration.model.User;

public class UserManager {
	public boolean exists(User u) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/spring", "root", "bjsxt");

		String sqlQuery = "select count(*) from user where username = ?";
		PreparedStatement psQuery = conn.prepareStatement(sqlQuery);
		psQuery.setString(1, u.getUsername());
		ResultSet rs = psQuery.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		psQuery.close();
		conn.close();
		
		if(count > 0) {
			return true;
		}
		return false;
	}
	
	public void add(User u) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/spring", "root", "bjsxt");

		String sql = "insert into user values (null, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, u.getUsername());
		ps.setString(2, u.getPassword());
		ps.executeUpdate();
		ps.close();
		conn.close();

		
	
	}
}
