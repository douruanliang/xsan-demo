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


@Entity
@Table(name="xs_user_info")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User extends Audited  implements Serializable{
	private static final long serialVersionUID = -3149974916027750041L;

	@Column(name="user_name")
	private String userName;
		
	@Column(name="user_index")
	private Integer userIndex;

	@Column(name="login_name")
	private String loginName;

	private String password;

	private String email;
	
	@Column(name="phone_num")
	private String phoneNum;

	private String remark;

	private String state;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xs_role_user", joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xs_dept_user", joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "dept_id"))
	private List<Dept> depts;


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public User() {
	}


	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
	//	this.password=DigestUtils.sha256Hex(StringUtils.trimToEmpty(password));
		this.password=password;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserIndex() {
		return userIndex;
	}

	public void setUserIndex(Integer userIndex) {
		this.userIndex = userIndex;
	}

	@JsonIgnore
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Dept> getDepts() {
		return depts;
	}

	public void setDepts(List<Dept> depts) {
		this.depts = depts;
	}

	




}