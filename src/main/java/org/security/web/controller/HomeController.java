package org.security.web.controller;

import java.security.Principal;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Inject
	HttpSession session;
	
	@RequestMapping("/")
	public String home(Model model) {
		logger.info("HomeController{}", "/");
		return "index";//semantic
	}
	

	// for 403 access denied page
		@RequestMapping( "/403")
		public String accesssDenied(Model model,Principal user) {
			if (user != null) {
				model.addAttribute("msg","Hi " + user.getName() 
						+ ", you do not have permission to access this page!");
			} else {
				model.addAttribute("msg","Hi " + user.getName() 
						+ ", You do not have permission to access this page");
			}
			return "403";
		}


}