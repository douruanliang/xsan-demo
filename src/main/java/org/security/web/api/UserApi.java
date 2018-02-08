package org.security.web.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.security.core.properties.SecurityProperties;
import org.security.dto.SimpleResultDto;
import org.security.dto.User;
import org.security.dto.User.UserSimpleView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.security.dto.UserDto;
import org.security.dto.UserQueryCondition;
import org.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
public class UserApi {
	private static final Logger log = LoggerFactory.getLogger(UserApi.class);
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserService userService;
	private String salt = RandomStringUtils.randomAscii(20);
	//@Autowired
   // private AppSingUpUtils appSingUpUtils;
	
	@ApiOperation(value = "查询用户信息列表", notes = "sys/user-list")
	@RequestMapping(value = "/user-list", method = { RequestMethod.POST })
	public String userList(Model model, @RequestParam(value = "models", required = false) String models) {
		Map searchParameters = new HashMap();
		if (models != null && models.length() > 0) {
			try {
				searchParameters = objectMapper.readValue(models, new TypeReference<Map>() {
				});
			} catch (JsonParseException e) {
				log.error("JsonParseException{}:", e.getMessage());
			} catch (JsonMappingException e) {
				log.error("JsonMappingException{}:", e.getMessage());
			} catch (IOException e) {
				log.error("IOException{}:", e.getMessage());
			}
		}
		Map map = userService.getPage(searchParameters, salt);
		// 用户表格数据
		model.addAttribute("list", map.get("users"));
		// 总条数
		model.addAttribute("total", map.get("total"));
		return "user/user-list";
	}

	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public SimpleResultDto userAdd(Model model, @RequestParam("models") String models) {
		SimpleResultDto result = new SimpleResultDto();
		result.setSuccess(false);
		UserDto userDto = new  UserDto();
		if (models != null && models.length() > 0) {
			try {
				userDto = objectMapper.readValue(models, new TypeReference<UserDto>() {
				});
			} catch (JsonParseException e) {
				log.error("JsonParseException{}:", e.getMessage());
				model.addAttribute("error", e.getMessage());
			} catch (IOException e) {
				log.error("IOException{}:", e.getMessage());
				model.addAttribute("error", e.getMessage());
			}
		}
		if (!model.containsAttribute("error")) {
			try {
				//userService.save(list);
				result.setSuccess(true);
				result.setMsg("保存成功");
			} catch (Exception e) {
				log.error("Exception{}:", e.getMessage());
				model.addAttribute("error", e.getMessage());
				result.setSuccess(false);
				result.setMsg(e.getMessage());
			}

		}
		return result;
	}
	
	
	//-----------以上是自定义与security 无关-------
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
