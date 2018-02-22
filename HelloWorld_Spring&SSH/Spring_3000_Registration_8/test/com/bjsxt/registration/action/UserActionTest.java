package com.bjsxt.registration.action;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bjsxt.registration.vo.UserRegisterInfo;

public class UserActionTest {

	@Test
	public void testExecute() throws Exception {
		UserAction ua = new UserAction();
		UserRegisterInfo info = new UserRegisterInfo();
		info.setUsername("x");
		info.setPassword("x");
		info.setPassword2("x");
		ua.setInfo(info);
		String ret = ua.execute();
		assertEquals("success", ret);
	}

}
