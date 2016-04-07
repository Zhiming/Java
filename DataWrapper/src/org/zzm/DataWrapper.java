package org.zzm;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class DataWrapper {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver") ;
		Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "admin");
		
		String sql = "select * from Person";
		PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<Integer, Map<String, String>> objMap = new HashMap<Integer, Map<String, String>>();
		String[] names = new String[columnCount];
		for (int i = 1; i <= columnCount; i++){
			String name = rsmd.getColumnName(i);
			String type = rsmd.getColumnClassName(i);
			names[i - 1] = name + "|" + type;
		}
		for(int i = 0; rs.next(); i++){
			Map<String, String> entryMap = new HashMap<String, String>();
			for(String entry : names){
				String name = entry.substring(0, entry.indexOf("|"));
				String type = entry.substring(entry.indexOf("|") + 1);
				String value = rs.getString(name);
				String valNType = value + "|" + type;
				entryMap.put(name, valNType);
			}
			objMap.put(i, entryMap);
		}
		rs.close();
		conn.close();
		for(Entry<Integer, Map<String, String>> entryMap : (objMap.entrySet())){
			Map<String, String> entry = entryMap.getValue();
			for(Entry<String, String> e : entry.entrySet()){
				System.out.println("name: " + e.getKey());
				String value = e.getValue();
				System.out.println("value: " + value.substring(0, value.indexOf("|")));
				System.out.println("type: " + value.substring(value.indexOf("|") + 1));
			}
			System.out.println();
		}
	}
}
