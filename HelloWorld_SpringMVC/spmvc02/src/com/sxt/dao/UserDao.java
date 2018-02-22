package com.sxt.dao;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.sxt.po.User;

@Component
public class UserDao {
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public void add(User u){
		System.out.println("UserDao.add()");
		hibernateTemplate.save(u);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
}
