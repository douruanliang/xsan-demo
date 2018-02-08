package org.security.security;

import org.security.core.authorize.AuthorizeConfigProvider;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
@Order(Integer.MAX_VALUE)
public class DemoAuthorizeConfigProvider implements AuthorizeConfigProvider {

	@Override
	public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		//demo 模块的 安全授权配置
	config.antMatchers("/user")
		.hasRole("ADMIN");
	
		//config.anyRequest().access("@rbacService.hasPermission(request,authentication)");
	}

	
	
	
	
	
}
