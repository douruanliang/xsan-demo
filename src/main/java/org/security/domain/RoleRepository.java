package org.security.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface  RoleRepository extends JpaRepository<Role, String>,
		JpaSpecificationExecutor<Role>,PagingAndSortingRepository<Role, String>{
	
	/**
	 * 
	 * @Description: 根据角色名称获取菜单
	 * @param roleId
	 * @date 2018年1月8日
	 */
	@Query("SELECT u.menus FROM Role u WHERE u.roleName = :roleName")
	List<Menu> findMenuByRoleName(@Param("roleName") String roleName);
	
	/**
	 * 根据角色id查询菜单集合
	 * @param roleId
	 * @return
	 */
	@Query("SELECT u.menus FROM Role u WHERE u.id = :roleId")
	List<Menu> findMenuByRoleId(@Param("roleId") String roleId);

	/**
	 * 标记为删除
	 * @param id
	 */
	@Modifying
	@Query("update Role m set m.flag=0  where m.id=:id")
	void updateFlag(@Param("id") String id);
	
	/**
	 * 查询所有未标记为删除的角色集合
	 * @return
	 */
	@Query("select r from Role r where r.flag=1")
	List<Role> findAllRoles();
	
	/**
	 * 根据roleName查询出记录
	 * @param roleName
	 * @return
	 */
	@Query("select r from Role r where r.roleName = :roleName and r.id!= :id and r.flag=1")
	List<Role> findByRoleName(@Param("roleName") String roleName,@Param("id") String id);
	
	@Query("select r from Role r where r.roleName = :roleName and r.flag=1")
	Role findByRoleName(@Param("roleName") String roleName);
	
	Role  findByRoleNameAndFlag(@Param("roleName") String roleName,@Param("flag") int flag);
}
