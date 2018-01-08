package org.security.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.security.domain.Role;
import org.security.domain.User;
import org.security.domain.UserRepository;
import org.security.dto.RoleDto;
import org.security.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 类名称：UserService   
 * 类描述： 用户服务类 
 * 创建人： 
 * 创建时间：2018年1月7日 下午9:52:30 
 * @version
 */
@Service
public class UserService {
	@Inject
	private UserRepository userRepository;
	
	//登录时用
	@Transactional(propagation = Propagation.SUPPORTS)
	public UserDto findByLoginName(String loginName) {
		User user = this.userRepository.findByLoginName(loginName);
		return  (user!=null)?user2Dto(user):null;
	}

	//用户实体转换DTO
	private UserDto user2Dto(User user) {
		UserDto userDto = new UserDto();
		//基本属性转换
		BeanUtils.copyProperties(user, userDto);
		 //角色
		List<Role> roleList = user.getRoles();
		if (roleList != null && !roleList.isEmpty()) {
			List<RoleDto> roleDtoList = new ArrayList<RoleDto>();
			for (Role role : roleList) {
					roleDtoList.add(role2Dto(role));
			}
			Collections.addAll(roleDtoList);
			Set<RoleDto> roleSet = new HashSet<RoleDto>();
			roleSet.addAll(roleDtoList);
			userDto.setRoles(roleSet);
		}
		return userDto;
	}

	//角色实体转换DTO
	private RoleDto role2Dto(Role role) {
		RoleDto dto = new RoleDto();
		BeanUtils.copyProperties(role, dto);
		return dto;
	}
}
