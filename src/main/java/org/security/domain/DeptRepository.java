package org.security.domain;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DeptRepository extends JpaRepository<Dept, String>,JpaSpecificationExecutor<Dept>{
	
	/**
	 * 查询机构树
	 * @return
	 */
	@Query("select d from Dept d left join fetch d.children")
	List<Dept> findDeptTree();

	/**
	 * 查询树根
	 * @return
	 */
	@Query("select d from Dept d where d.parent is null and d.flag=1 and d.deptState=1  order by d.deptIndex")
	List<Dept> findRoots();
	
	/**
	 * 标记为删除
	 * @param id
	 */
	@Modifying
	@Query("update Dept d set d.flag=0 where d.id=:id")
	void updateFlag(@Param("id") String id);
	
	/**
	 * 查询所有（未标记为删除的）
	 * @return
	 */
	@Query("select d from Dept d where d.flag=1")
	List<Dept> findAllDepts();
	
	/**
	 * 根据机构ID和角色ID查询用户集合
	 * @param deptId
	 * @param roleId
	 * @return
	 */
	@Query("select u.id from Role r JOIN r.users u  where r.id = :roleId")
	List<String> findUsersByRole(@Param("roleId") String roleId);
	/**
	 * 根据机构ID和角色ID查询用户集合
	 * @param deptId
	 * @param roleId
	 * @return
	 */
	@Query("select u.id from Dept d JOIN d.users u JOIN u.roles r  where d.id = :deptId and r.id = :roleId")
	List<String> findUsersByDeptRole(@Param("deptId") String deptId,@Param("roleId") String roleId);

	@Query("select d from Dept d where d.deptName = :deptName and d.id!= :id and d.flag=1")
	List<Dept> findByDeptName(@Param("deptName") String deptName,@Param("id") String id);
	@Query("select d from Dept d where d.deptName = :deptName and d.flag=1")
	Dept findByDeptName(@Param("deptName") String deptName);
	@Query("select d from Dept d where d.deptVdcId = :deptVdcId and d.flag=1")
	Dept findByDeptVdcId(@Param("deptVdcId") String deptVdcId);	

}
