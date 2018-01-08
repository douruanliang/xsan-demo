package org.security.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

@Entity
@Table(name="xs_menu_info")
public class Menu extends Audited implements Serializable{

	private static final long serialVersionUID = 7381374907067127702L;
	@Lob
	@Column(name="icon_path")
	private byte[] iconPath;

	@Column(name="menu_desc")
	private String menuDesc;

	@Column(name="menu_index")
	private Integer menuIndex;

	@Column(name="menu_name")
	private String menuName;

	@Column(name="menu_url")
	private String menuUrl;

	private String remark;

	@Lob
	@Column(name="small_icon_path")
	private byte[] smallIconPath;

	@ManyToOne(cascade = { CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "parent_id", insertable = true, updatable = true)
	private Menu parent = null;
	
	@OrderBy("menuIndex ASC")
	@Where(clause="flag = 1")
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH,CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id",insertable = false, updatable = false)
	private List<Menu> children = new LinkedList<Menu>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "xs_role_menu", joinColumns = @JoinColumn(name = "menu_id"),
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	private String mark;//初始化菜单记号


	public String getMark() {
		return mark;
	}


	public void setMark(String mark) {
		this.mark = mark;
	}


	public Menu() {
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public byte[] getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(byte[] iconPath) {
		this.iconPath = iconPath;
	}

	public String getMenuDesc() {
		return this.menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public Integer getMenuIndex() {
		return this.menuIndex;
	}

	public void setMenuIndex(Integer menuIndex) {
		this.menuIndex = menuIndex;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return this.menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}


	public byte[] getSmallIconPath() {
		return this.smallIconPath;
	}
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void setSmallIconPath(byte[] smallIconPath) {
		this.smallIconPath = smallIconPath;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}



}