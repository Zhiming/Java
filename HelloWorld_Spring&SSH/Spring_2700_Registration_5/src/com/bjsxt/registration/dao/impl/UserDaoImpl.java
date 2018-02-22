package com.bjsxt.registration.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import com.bjsxt.registration.dao.UserDao;
import com.bjsxt.registration.model.User;
import com.bjsxt.registration.util.HibernateUtil;

public class UserDaoImpl implements UserDao {

	public void save(User u) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session s = sf.getCurrentSession();
		s.beginTransaction();
		s.save(u);
		s.getTransaction().commit();
	}

	public boolean checkUserExistsWithName(String username) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session s = sf.getCurrentSession();
		s.beginTransaction();
		long count = (Long)s.createQuery("select count(*) from User u where u.username = :username")
			.setString("username", username)
			.uniqueResult();
		s.getTransaction().commit();
		if(count > 0) return true;
		return false;
	}

}
