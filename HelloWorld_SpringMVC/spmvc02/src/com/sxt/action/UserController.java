package com.sxt.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sxt.po.User;
import com.sxt.service.UserService;

@Component
@RequestMapping("/user.do")
public class UserController  {

	@Resource
	private UserService userService;
	
	
	@RequestMapping(params="method=reg")
	public String reg(String uname){
		System.out.println("UserController.reg()");
		System.out.println(uname);
		userService.add(uname);
		return "index";
	}
	
	@RequestMapping(params="method=reg2")
	public String reg2(User user){
		System.out.println("UserController.reg2()");
		System.out.println(user.getUname());
		return "index";
	}
	
	

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
}
