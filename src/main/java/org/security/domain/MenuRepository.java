package org.security.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface  MenuRepository extends JpaRepository<Menu, String>,JpaSpecificationExecutor<Menu>{

	/**
	 * 查询菜单树
	 * @return
	 */
	@Query("select o from Menu o left  join fetch  o.children c  where c.flag=1")
	public List<Menu> findMenuTree();
	
	/**
	 * 
	 * @Description: 标示，暂时用于区别初始化菜单来源
	 * @param mark
	 * @return Menu  
	 * @throws
	 * @author chixue
	 * @date 2016年5月9日
	 */
     Menu findByMark(@Param("mark") String mark);
	
	/** 查询出树根*/
	List<Menu>	findByFlagAndParentIsNullOrderByMenuIndexAsc(@Param("flag") int flag);

	/**
	 * 标记为删除
	 * @param id
	 */
	@Modifying
	@Query("update Menu m set m.flag=0 where m.id=:id")
	void updateFlag(@Param("id") String id);
	
	/**
	 * 根据描述查询出树根
	 * @param menuDesc
	 * @return
	 */
	@Query("select o from Menu o where o.parent.id=0 and o.menuDesc=:menuDesc and o.flag=1")
	List<Menu> findRoots(@Param("menuDesc") String menuDesc);
	
	/**
	 * 查询所有菜单（未标记未删除的）
	 * @return
	 */
	@Query("select m from Menu m where m.flag=1")
	List<Menu> findAllMenus();
	/**
	 * 按角色查询认证授权的菜单
	 * @param roles
	 * @return
	 */
	@Query("select o from Menu o  left JOIN o.roles r where  o.parent is null and o.flag=1 and (r in (:roles))  order by o.menuIndex")
 List<Menu> findRootByAuthorization(@Param("roles")List<Role> roles);
	
	
	
	/**
	 * 按节点查询认证授权的菜单
	 * @param roles
	 * @return
	 */
	@Query("select o from Menu o  left JOIN o.roles r where  o.parent.id=:id and  o.flag=1 and (r in (:roles))  order by o.menuIndex")
 List<Menu> findMenuByRootAndAuthorization(@Param("roles")List<Role> roles,@Param("id")String id);
	
	
	@Query("select o from Menu o  where  o.parent.id=:id and  o.flag=1  order by o.menuIndex")
 List<Menu> findMenuByRootId(@Param("id")String id);
/**
 * 
 * @Description: 描述信息查询，根据描述信息查询出菜单的相关信息
 * @param mark
 * @return List<Menu>  
 * @throws
 * @author chixue
 * @date 2017年2月20日
 */
	@Query("select o from Menu o  where  o.parent.mark=:mark and  o.flag=1  order by o.menuIndex")
	List<Menu>  findMenuByMark(@Param("mark")String mark);
}
