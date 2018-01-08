package org.security.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Table(name="xs_role_info")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role extends Audited implements Serializable {

	private static final long serialVersionUID = 4641095039785783252L;

	@Column(name="role_description")
	private String roleDesc;

	@Column(name="role_index")
	private Integer roleIndex;


	@Column(name="role_name")
	private String roleName;


	//uni-directional many-to-many association to Menu
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="xs_role_menu", joinColumns={@JoinColumn(name="role_id")}
		, inverseJoinColumns={@JoinColumn(name="menu_id")}
		)
	private List<Menu> menus;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xs_role_user", joinColumns = {@JoinColumn(name = "role_id")},
			inverseJoinColumns = {@JoinColumn(name = "user_id")})
	private List<User> users;

	private String remark;
	
	
	@JsonIgnore
	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	
	public Role() {
	}


	public String getRoleDesc() {
		return this.roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}


	public Integer getRoleIndex() {
		return roleIndex;
	}

	public void setRoleIndex(Integer roleIndex) {
		this.roleIndex = roleIndex;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}



	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}



	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}


}