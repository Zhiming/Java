package com.sxt.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.sxt.po.User;
import com.sxt.service.UserService;

@Controller
@RequestMapping("/user.do")
@SessionAttributes({"u","a"})
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
	public ModelAndView reg2(User user){
		System.out.println("UserController.reg2()");
		System.out.println(user.getUname());
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	
	
	@RequestMapping(params="method=reg3")
	public String reg3(@RequestParam("uname") String name,HttpServletRequest req,ModelMap map){
		System.out.println("UserController.reg()");
		System.out.println(name);
		req.getSession().setAttribute("c", "ccc");
		map.put("a", "aaa");
		
		return "index";
	}
	
	@RequestMapping(params="method=reg4")
	public String reg4(@ModelAttribute("a") String a,HttpServletRequest req,ModelMap map){
		System.out.println("UserController.reg4()");
		System.out.println(a);
		return "redirect:http://www.baidu.com";
	}
	
	@RequestMapping(params="method=reg5")
	public ModelAndView reg5(String uname){
		System.out.println("UserController.reg5()");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		
		User u = new User("¿œ∏ﬂ");
		User u2 = new User("∏ﬂ‰ø");
		
		mav.addObject(u);
		mav.addObject("uu", u2);
		
		return mav;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
}
