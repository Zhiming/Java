package com.bjsxt.registration.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import com.bjsxt.registration.model.User;
import com.bjsxt.registration.util.HibernateUtil;

public class UserManager {
	public boolean exists(User u) throws Exception {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session s = sf.getCurrentSession();
		s.beginTransaction();
		long count = (Long)s.createQuery("select count(*) from User u where u.username = :username")
			.setString("username", u.getUsername())
			.uniqueResult();
		s.getTransaction().commit();
		if(count > 0) return true;
		return false;
		
	}
	
	public void add(User u) throws Exception {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session s = sf.getCurrentSession();
		s.beginTransaction();
		s.save(u);
		s.getTransaction().commit();

		
	
	}
}
