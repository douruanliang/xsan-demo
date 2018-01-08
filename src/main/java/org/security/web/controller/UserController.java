package org.security.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.security.core.properties.SecurityProperties;
import org.security.dto.User;
import org.security.dto.User.UserSimpleView;
import org.security.dto.UserQueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	//@Autowired
   // private AppSingUpUtils appSingUpUtils;
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@PostMapping("/regist")
	public void regist(User user,HttpServletRequest request){
		//不管是注册用户还是绑定用户，都会拿到一个用户唯一标识
		String userId = user.getUsername();
		//绑定并入库
		providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
		//appSingUpUtils.doPostSignUp(new ServletWebRequest(request),userId);
	
	}
	
	@GetMapping("/me")
	public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
		
		/* jwt getCurrentUser(Authentication user,HttpServletRequest request) 
		 * String header = request.getHeader("Authorization");
		String token = StringUtils.substringAfter(header, "bearer ");
		Claims claims =Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey())
		.parseClaimsJws(token).getBody();
		
		String company =(String) claims.get("company");*/
		return user;
	}
	
	@GetMapping
	@JsonView(UserSimpleView.class)
	@ApiOperation(value="获取用户列表", notes="")
	public List<User> query(UserQueryCondition condition,@PageableDefault(page=2,size=17,sort="username,desc")Pageable pageable){
		System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));
		
		System.out.println(ReflectionToStringBuilder.toString(pageable, ToStringStyle.MULTI_LINE_STYLE));
		List<User> list = new ArrayList<>();
		list.add(new User());
		list.add(new User());
		list.add(new User());
		return list;
		
	}
	
	@GetMapping("/{id:\\d+}")
	@JsonView(User.UserDetailView.class)
	public User getInfo(@ApiParam("用户id") @PathVariable String id) {
//		throw new RuntimeException("user not exist");
		System.out.println("进入getInfo服务");
		User user = new User();
		user.setUsername("tom");
		return user;
	}
	
}
