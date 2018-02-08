package org.security.demo;

import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.security.Application;
import org.security.domain.Role;
import org.security.domain.RoleRepository;
import org.security.domain.User;
import org.security.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class AdminTest {

	@Inject
	UserRepository userRepository;

	@Inject
	RoleRepository roleRepository;
    //默认的Spring Security
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Test
	public void initData() throws InterruptedException {
		//add admin 角色
		if (roleRepository.findByRoleNameAndFlag("ROLE_ADMIN", 1) == null) {
			Role role = new Role();
			role.setRoleName("ROLE_ADMIN");
			role.setFlag(1);
			roleRepository.save(role);
		}
		//add ROLE_USER 角色
		if (roleRepository.findByRoleNameAndFlag("ROLE_USER", 1) == null) {
			Role role = new Role();
			role.setRoleName("ROLE_USER");
			role.setFlag(1);
			roleRepository.save(role);
		}
		
		System.out.println("----》初始化角色表结束《----");
		
		Long count = userRepository.count();
		
		System.out.println("初始化前用户数量《----"+count);
		//add admin 用户
		if (userRepository.findByLoginName("admin") == null) {
			User user = new User();
			user.setId("admin");
			user.setLoginName("admin");
			user.setUserName("管理员");
			user.setPassword(passwordEncoder.encode("123456"));
			user.setState("1");
			user.setEmail("douruanliang@gmail.com");
			userRepository.save(user);
		}
		/*if (userRepository.findByLoginName("root") == null) {
			User user = new User();
			user.setId("admin");
			user.setLoginName("admin");
			user.setUserName("管理员");
			user.setPassword(passwordEncoder.encode("dourl"));
			user.setState("1");
			user.setEmail("douruanliang@gmail.com");
			userRepository.save(user);
		}*/
		count = userRepository.count();
		System.out.println("初始化后用户数量----》"+count);
		
		System.out.println("----》初始化用户表表结束《----");
		//查询 admin 用户
		User admin = userRepository.findByLoginName("admin");
		 //查询 admin 角色
		Role roleAdmin = roleRepository.findByRoleNameAndFlag("ROLE_ADMIN", 1);
		
		if (!admin.getRoles().contains(roleAdmin)) {
			//添加 admin角色
			admin.getRoles().add(roleAdmin);
			System.out.print("当前用户角色数量"+admin.getRoles().size());
			 //更新当前用户权限
			admin = userRepository.saveAndFlush(admin);
		}

		Role roleUser = roleRepository.findByRoleNameAndFlag("ROLE_USER", 1);
		if (!admin.getRoles().contains(roleUser)) {
			admin.getRoles().add(roleUser);
			userRepository.save(admin);
		}
		System.out.println("初始化admin用户的admin权限数据完成");
	}

}
