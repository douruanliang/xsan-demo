package org.security.security;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.security.domain.Menu;
import org.security.dto.RoleDto;
import org.security.dto.UserDto;
import org.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;


@Component
public class DemoUserDetailsService  implements UserDetailsService,SocialUserDetailsService{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	
	//表单登录
	@Override
	public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
		logger.info("登录用户名："+loginName);
		//根据用户名查找用户信息
		UserDto user = userService.findByLoginName(loginName);
		if (user == null) {
			throw new UsernameNotFoundException(loginName);
		} else {
			if (!"1".equals(user.getState())) {
				throw new UsernameNotFoundException("该用户处于锁定状态");
			}
		}
		Collection<GrantedAuthority> grantedAuths = obtionGrantedAuthorities(user);
		//封装 spring security 会自己拿着 用户输入的密码 和 数据库返回的密码 进行比对 (passwordEncoder)
		User userdetail = new User(user.getUserName(), user.getPassword(),
				true, true, true,
				true, grantedAuths);
		
		
		return userdetail;
	}
    //第三方
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		logger.info("设计登录用户Id："+userId);
		return buildUser(userId);
	}

	/**
	 * 默认有 ROLE_USER 才能访问
	 * @param userId  admin,ROLE_USER,ROLE_ADMIN
	 * @return
	 */
	private SocialUserDetails buildUser(String userId) {
		String password= passwordEncoder.encode("123456") ;
		//String password= DigestUtils.sha256Hex("admin");
		logger.info("数据库密码是:"+password);
		return new SocialUser(userId,password, 
				true,true,true,true, 
				AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
	}

	
	// 取得用户的权限
    private Set<GrantedAuthority> obtionGrantedAuthorities(UserDto user) {
			Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
			// 获取用户角色
			Set<RoleDto> roles = user.getRoles();
			if (null != roles && !roles.isEmpty())
				for (RoleDto role : roles) {
					authSet.add(new SimpleGrantedAuthority(role.getRoleName()));
				}
			return authSet;
		}
	  
	
	
}
