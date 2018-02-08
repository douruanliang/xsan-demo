package org.security.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.security.domain.Role;
import org.security.dto.MenuDto;
import org.security.dto.UserDto;
import org.security.service.MenuService;
import org.security.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
public class HomeController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private MenuService  menuService;
	@Inject
	private RoleService  roleService;
	@RequestMapping("/")
	public String home(Model model) {
		logger.info("HomeController{}", "/");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		List<Role> result = new ArrayList<>();
        result = this.roleService.findRByName(roles);
      
		List<MenuDto> menus=menuService.findRootByAuthorization(result);
		if(null!=menus){
			model.addAttribute("rootMenus", menus.get(0).getChildren()); 
		}
		logger.info("menus{}", menus.isEmpty());
		
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
			return "error/403";
		}


}