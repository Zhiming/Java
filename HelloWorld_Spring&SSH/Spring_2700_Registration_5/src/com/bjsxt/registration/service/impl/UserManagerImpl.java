package com.bjsxt.registration.service.impl;

import com.bjsxt.registration.dao.UserDao;
import com.bjsxt.registration.dao.impl.UserDaoImpl;
import com.bjsxt.registration.model.User;
import com.bjsxt.registration.service.UserManager;

public class UserManagerImpl implements UserManager {
	
	private UserDao userDao = new UserDaoImpl();
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/* (non-Javadoc)
	 * @see com.bjsxt.registration.service.impl.UserManager#exists(com.bjsxt.registration.model.User)
	 */
	public boolean exists(User u) throws Exception {
		return userDao.checkUserExistsWithName(u.getUsername());
		
	}
	
	/* (non-Javadoc)
	 * @see com.bjsxt.registration.service.impl.UserManager#add(com.bjsxt.registration.model.User)
	 */
	public void add(User u) throws Exception {
		userDao.save(u);
	}
}
