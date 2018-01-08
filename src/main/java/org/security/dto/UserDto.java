package org.security.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Size;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


public class UserDto  implements Serializable{
	
	private static final long serialVersionUID = 1170018455276020707L;

	private String id;
	private Integer userIndex;

	@Email(message="Email格式不正确！")
	private String email;
	
	@NotEmpty(message="登陆名不能为空！")
	@Size(min=6,max=20)
	private String loginName;

	@NotEmpty(message="密码不能为空！")
	private String password;

	private String state;
	
	private String userName;
	
	private String phoneNum;
	
	private Integer flag;

	private String remark;
	
	private byte[] userAvatar;
	
	private Set<RoleDto> roles;
	
	private Set<DeptDto> depts;
	
	
	private String token = "";


	public byte[] getUserAvatar() {
		return userAvatar;
	}


	public void setUserAvatar(byte[] userAvatar) {
		this.userAvatar = userAvatar;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Integer getUserIndex() {
		return userIndex;
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

	public void setUserIndex(Integer userIndex) {
		this.userIndex = userIndex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<RoleDto> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleDto> roles) {
		this.roles = roles;
	}

	public Set<DeptDto> getDepts() {
		return depts;
	}

	public void setDepts(Set<DeptDto> depts) {
		this.depts = depts;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
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
