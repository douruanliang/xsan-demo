package org.security.web.controller;

import org.security.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/sys")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value = "/user-list", method = { RequestMethod.GET })
	public String userList() {
		log.info("sys{}", "/user-list");
		return "user/user-list";
	}
	
	@RequestMapping(value = "/user-add", method = { RequestMethod.GET })
	public String userAdd(Model model) {
		UserDto dto = new UserDto();
		model.addAttribute("userDto", dto);
		return "user/user-edit";
	}
	
}
