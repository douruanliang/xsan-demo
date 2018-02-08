package org.security.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.security.domain.Role;
import org.security.domain.User;
import org.security.domain.UserRepository;
import org.security.dto.RoleDto;
import org.security.dto.UserDto;
import org.security.exception.UserNotExistException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public Map getPage(final Map searchParameters, String salt) {
		Map map = new HashMap();
		int page = 0;
		int pageSize = 10;
		Page<User> pageList;
		if (searchParameters != null && searchParameters.size() > 0 && searchParameters.get("page") != null) {
			page = Integer.parseInt(searchParameters.get("page").toString()) - 1;
		}
		if (searchParameters != null && searchParameters.size() > 0 && searchParameters.get("pageSize") != null) {
			pageSize = Integer.parseInt(searchParameters.get("pageSize").toString());
		}
		if (pageSize < 1)
			pageSize = 1;
		if (pageSize > 100)
			pageSize = 100;
		List<Map> orderMaps = (List<Map>) searchParameters.get("sort");
		List<Order> orders = new ArrayList<Order>();
		if (orderMaps != null) {
			for (Map m : orderMaps) {
				if (m.get("field") == null)
					continue;
				String field = m.get("field").toString();
				if (!StringUtils.isEmpty(field)) {
					String dir = m.get("dir").toString();
					if ("DESC".equalsIgnoreCase(dir)) {
						orders.add(new Order(Direction.DESC, field));
					} else {
						orders.add(new Order(Direction.ASC, field));
					}
				}
			}
		}
		PageRequest pageable;
		if (orders.size() > 0) {
			pageable = new PageRequest(page, pageSize, new Sort(orders));
		} else {
			Sort s = new Sort(Direction.ASC, "userIndex");
			pageable = new PageRequest(page, pageSize, s);
		}
		Map filter = (Map) searchParameters.get("filter");
		if (filter != null) {
			final List<Map> filters = (List<Map>) filter.get("filters");
			Specification<User> spec = new Specification<User>() {
				@Override
				public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> pl = new ArrayList<Predicate>();
					for (Map f : filters) {
						String field = f.get("field").toString().trim();
						String value = f.get("value").toString().trim();
						if (value != null && value.length() > 0) {
							if ("loginName".equalsIgnoreCase(field)) {
								pl.add(cb.equal(root.<String>get(field), value));
							} else if ("userName".equalsIgnoreCase(field)) {
								pl.add(cb.like(root.<String>get(field), value + "%"));
							} else if ("email".equalsIgnoreCase(field)) {
								pl.add(cb.like(root.<String>get(field), value +"%"));
							} else if("state".equalsIgnoreCase(field)){
								pl.add(cb.equal(root.<Integer>get("state"), value));
							}
						}
					}
					// 查询出未删除的
					pl.add(cb.equal(root.<Integer>get("flag"), 1));
					//pl.add(cb.equal(root.<Integer>get("state"), 1));
					return cb.and(pl.toArray(new Predicate[0]));
				}
			};
			pageList = userRepository.findAll(spec, pageable);

		} else {
			Specification<User> spec = new Specification<User>() {
				public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
					// 查询出未删除的
					list.add(cb.equal(root.<Integer>get("flag"), 1));
					list.add(cb.equal(root.<Integer>get("state"), 1));
					return cb.and(list.toArray(new Predicate[0]));
				}
			};
			pageList = userRepository.findAll(spec, pageable);

		}

		map.put("total", pageList.getTotalElements());
		map.put("users", userList2DtoList(pageList.getContent(), salt));
		return map;
	}

	public List<UserDto> userList2DtoList(List<User> list, String salt) {
		List<UserDto> dtos = new ArrayList<UserDto>();
		if (list != null && list.size() > 0) {
			for (User d : list) {
				dtos.add(user2Dto(d, salt));
			}
		}
		return dtos;

	}
	
	
	
	
	//登录时用
	@Transactional(propagation = Propagation.SUPPORTS)
	public UserDto findByLoginName(String loginName) {
		User user = this.userRepository.findByLoginName(loginName);
		return  (user!=null)?user2Dto(user):null;
	}
	
    //保存|修改
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(UserDto userDto) {
		if(null!=userDto){
			String loginName = userDto.getLoginName();
			String id = userDto.getId();
			if(StringUtils.isNotEmpty(loginName) && StringUtils.isNotEmpty(id)){
				if(findByLoginName(loginName, id)){
					throw new UserNotExistException("登录名[" +loginName + "]已存在！");
				}else{
					User user = new User();
					BeanUtils.copyProperties(userDto, user);
					user.setPassword(passwordEncoder.encode(userDto.getPassword()));
					this.save(user);
				}
			}
		}
		
	}
	
	
	//执行保存
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(User user) {
		this.userRepository.save(user);
	}
	
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public boolean findByLoginName(String loginName, String id) {
		List<User> users = new ArrayList<User>();
		if (StringUtils.isNotEmpty(loginName)) {
			users = this.userRepository.findByUserName(loginName.trim(), id);
			if (users.size() > 0) {
				return true;
			}
		}
		return false;
	}

	public UserDto user2Dto(User user, String salt) {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(user, userDto);
		userDto.generateToken(salt);
		return userDto;

	}
	//用户实体转换DTO(包括角色)
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
