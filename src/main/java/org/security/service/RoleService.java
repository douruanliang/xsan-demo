package org.security.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.security.domain.Menu;
import org.security.domain.MenuRepository;
import org.security.domain.Role;
import org.security.domain.RoleRepository;
import org.security.domain.User;
import org.security.domain.UserRepository;
import org.security.dto.MenuDto;
import org.security.dto.RoleDto;
import org.security.dto.UserDto;
import org.security.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class RoleService {

	private static final Logger log = LoggerFactory
			.getLogger(RoleService.class);

	@Inject
	private RoleRepository roleRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private MenuRepository menuRepository;

	@Inject
	private ObjectMapper objectMapper;
	/**
	 * 查询所有
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<Role> findAllRoles() {
		return roleRepository.findAllRoles();
	}

	/**
	 * 查询角色信息
	 * 
	 * @param searchParameters
	 *            查询参数的map集合
	 * @return 查询的结果,map类型 total:总条数 roles:查询结果list集合
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public Map getPage(final Map searchParameters, String salt) {
		Map map = new HashMap();
		int page = 0;
		int pageSize = 10;
		Page<Role> pageList;
		// Page<RoleDto> pageDtoList;
		// 页码
		if (searchParameters != null && searchParameters.size() > 0
				&& searchParameters.get("page") != null) {
			page = Integer.parseInt(searchParameters.get("page").toString()) - 1;
		}
		// 每页显示的条数
		if (searchParameters != null && searchParameters.size() > 0
				&& searchParameters.get("pageSize") != null) {
			pageSize = Integer.parseInt(searchParameters.get("pageSize")
					.toString());
		}
		if (pageSize < 1)
			pageSize = 1;
		if (pageSize > 100)
			pageSize = 100;
		List<Map> orderMaps = (List<Map>) searchParameters.get("sort");
		List<Order> orders = new ArrayList<Order>();
		// 排序字段不为空
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
			Sort s = new Sort(Direction.ASC, "roleIndex");
			pageable = new PageRequest(page, pageSize, s);
		}
		Map filter = (Map) searchParameters.get("filter");
		// 查询条件不为空
		if (filter != null) {
			// String logic = filter.get("logic").toString();
			final List<Map> filters = (List<Map>) filter.get("filters");
			Specification<Role> spec = new Specification<Role>() {
				@Override
				public Predicate toPredicate(Root<Role> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> pl = new ArrayList<Predicate>();
					for (Map f : filters) {
						// String operator = ((String)
						// f.get("operator")).trim();
						String field = f.get("field").toString().trim();
						String value = f.get("value").toString().trim();
						if (value != null && value.length() > 0&&!"".equals(value)) {
							if ("roleIndex".equalsIgnoreCase(field)) {
								pl.add(cb.equal(root.<String> get(field), value));
							} else if ("roleName".equalsIgnoreCase(field)) {
								pl.add(cb.like(root.<String> get(field), "%"
										+ value + "%"));
							} else if ("roleDesc".equalsIgnoreCase(field)) {
								pl.add(cb.like(root.<String> get(field), "%"
										+ value + "%"));
							}
						}

					}
					// 查询出未删除的
					pl.add(cb.equal(root.<Integer> get("flag"), 1));
					return cb.and(pl.toArray(new Predicate[0]));
				}
			};
			pageList = roleRepository.findAll(spec, pageable);

		}
		// 查询条件为空
		else {
			Specification<Role> spec = new Specification<Role>() {
				public Predicate toPredicate(Root<Role> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
					// 查询出未删除的
					list.add(cb.equal(root.<Integer> get("flag"), 1));
					return cb.and(list.toArray(new Predicate[0]));
				}
			};
			pageList = roleRepository.findAll(spec, pageable);

		}
		map.put("total", pageList.getTotalElements());
		map.put("roles", objList2DtoList(pageList.getContent(),salt));
		return map;
	}

	/**
	 * 添加角色信息
	 * 
	 * @param list
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(List<RoleDto> list) {
		if (list != null && list.size() > 0) {
			for (RoleDto dto : list) {
					if (!findByName(dto.getRoleName(), dto.getId())) {
						throw new BusinessException("角色名[" + dto.getRoleName()
								+ "]已存在！");
					}
					// 新增
					Role role = new Role();
					BeanUtils.copyProperties(dto, role);
					this.roleRepository.saveAndFlush(role);
			}
		}
	}



	/**
	 * 多条删除
	 * 
	 * @param list
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteRole(String[] roleIds) {
/*		if (list != null && list.size() > 0) {
			for (RoleDto d : list) {
				if (d.getUsers() != null && d.getUsers().size() > 0) {
					throw new BusinessException("该角色与用户关联，不能删除！");
				} else if (d.getMenus() != null && d.getMenus().size() > 0) {
					throw new BusinessException("该角色与菜单关联，不能删除！");
				} else {
					String id = d.getId();
					this.roleRepository.updateFlag(id);
				}
			}
		}*/
		if (roleIds != null && roleIds.length > 0) {
			for (String id : roleIds) {
				if (id != null && !"".equals(id)) {
					Role role = this.roleRepository.findOne(id);
					if(role!=null){
						if (role.getUsers() != null && role.getUsers().size() > 0) {
							throw new BusinessException("该角色与用户关联，不能删除！");
						} else if (role.getMenus() != null && role.getMenus().size() > 0) {
							throw new BusinessException("该角色与菜单关联，不能删除！");
						} else {
							// 标记为已删除-0,未删除-1
							this.roleRepository.updateFlag(id);
						}
					}
					
				}
			}

		}
	}

	/**
	 * 根据ID查询角色
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public RoleDto findById(String id,String salt) {
		Role role= this.roleRepository.findOne(id);
		return	obj2Dto(role,salt);
	}
	/**
	 * 
	 * @Description: 集合对象转换
	 * @param roleList
	 * @param salt
	 * @return List<RoleDto>  
	 * @throws
	 * @author chixue
	 * @date 2017年5月12日
	 */
	public List<RoleDto> objList2DtoList(List<Role> roleList,String salt ){
		List<RoleDto> dtos=new  ArrayList();
		for(Role r:roleList){
			dtos.add(obj2Dto(r,salt));
		}
		return dtos;
	}

	public RoleDto obj2Dto(Role role,String salt){
		RoleDto dto = new RoleDto();
		BeanUtils.copyProperties(role, dto);
		dto.generateToken(salt);
		return dto;
	}
	public RoleDto obj2Dto(Role role){
		RoleDto dto = new RoleDto();
		BeanUtils.copyProperties(role, dto);
		return dto;
	}


	/**
	 * 根据角色id查询出映射的用户集合
	 * 
	 * @param roleId
	 *            用户选择的角色id
	 * @return 映射的用户集合
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<UserDto> findUsersByRoleId(String roleId) {
		List<UserDto> users = new ArrayList<UserDto>();
		Role role = this.roleRepository.findOne(roleId);
		List<User> userList = role.getUsers();
		if (userList != null && userList.size() > 0) {
			for (User u : userList) {
				UserDto userDto = new UserDto();
				BeanUtils.copyProperties(u, userDto);
				users.add(userDto);
			}
		}
		return users;
	}
	/**
	 * 根据角色名查询
	 * 
	 * @param
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public boolean findByName(String roleName, String id) {
		List<Role> roles = new ArrayList<Role>();
		if (roleName != null && roleName.length() > 0) {
			roles = this.roleRepository.findByRoleName(roleName, id);
			if (roles.size() > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据角色名查询
	 * 
	 * @param
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public Role findByName(String roleName) {
		Role r = new Role();
		if (roleName != null && roleName.length() > 0) {
			r = this.roleRepository.findByRoleName(roleName);
		}
		return r;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
    public List<Role> findRByName(Set<String> roleNames){
    	List<Role> roleList = new ArrayList<>();
    	for(String roleName:roleNames){
	    	if(StringUtils.isNotEmpty(roleName)){
	    		Role r = findByName(roleName);
	    		if(null!=r){
	    			roleList.add(r);
	    		}
	    	}
    	}
		return roleList;
    }
	
	public List<RoleDto> findRByName(List<Role> roles){
		List<RoleDto> roleDtoList = new ArrayList<>();
		if(null!=roles && roles.size()>0){
			for(Role r:roles){
				roleDtoList.add(obj2Dto(r));
			}
		}
		return roleDtoList;
	}
}
