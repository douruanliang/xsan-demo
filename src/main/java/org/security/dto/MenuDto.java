package org.security.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.security.domain.Menu;
import org.security.domain.Role;

public class MenuDto implements Serializable {
	
	private static final long serialVersionUID = 8867257601351189506L;

	private String id;

	private String controllerClass;

	private byte[] iconPath;

	private String menuDesc;

	private Integer menuIndex;

	private String menuName;

	private String menuUrl;

	private String parentId;
	
	private String parentName;

	private byte[] smallIconPath;

	private Menu parent; 
	
	private Integer flag;

	private String remark;
	
	private String mark;

	private List<MenuDto> children = new ArrayList<MenuDto>();
	
	private List<RoleDto> roleList = new ArrayList<RoleDto>();

	private Set<Role> roles;
	
	private String token = "";



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getControllerClass() {
		return controllerClass;
	}

	public void setControllerClass(String controllerClass) {
		this.controllerClass = controllerClass;
	}

	public byte[] getIconPath() {
		return iconPath;
	}

	public void setIconPath(byte[] iconPath) {
		this.iconPath = iconPath;
	}


	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMenuDesc() {
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public Integer getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(Integer menuIndex) {
		this.menuIndex = menuIndex;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public byte[] getSmallIconPath() {
		return smallIconPath;
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


	public Set<Role> getRoles() {
		return roles;
	}

	public List<MenuDto> getChildren() {
		return children;
	}

	public void setChildren(List<MenuDto> children) {
		this.children = children;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
    public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public List<RoleDto> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleDto> roleList) {
		this.roleList = roleList;
	}

	public void generateToken(String salt) {
		if (salt == null)
			token = DigestUtils.sha1Hex(StringUtils.trimToEmpty(id)
					+ Long.toString(serialVersionUID));
		else
			token = DigestUtils.sha1Hex(StringUtils.trimToEmpty(id) + salt);

	}

	public boolean tokenKeeped(String salt) {
		if (salt == null)
			return (DigestUtils.sha1Hex(StringUtils.trimToEmpty(id)
					+ Long.toString(serialVersionUID))).equals(token);
		else
			return DigestUtils.sha1Hex(StringUtils.trimToEmpty(id) + salt).equals(
					token);
	}
 
}