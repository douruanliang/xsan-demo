package org.security.dto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;



public class DeptDto  implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String deptDesc;
	private Integer flag;
	private Integer deptIndex;

	@NotEmpty(message = "机构名称不能为空")
	private String deptName;

	private String deptState;

	private String deptType;

	private String deptUrl;

	private String parentId;
	
	private String parentName;

	private List<UserDto> users;	

	private String remark;
	

	
	private List<DeptDto> children = new LinkedList<DeptDto>();
	
	private String token = "";
	


	public List<DeptDto> getChildren() {
		return children;
	}

	public void setChildren(List<DeptDto> children) {
		this.children = children;
	}

	public List<UserDto> getUsers() {
		return users;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setUsers(List<UserDto> users) {
		this.users = users;
	}


	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}


	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

	public Integer getDeptIndex() {
		return deptIndex;
	}

	public void setDeptIndex(Integer deptIndex) {
		this.deptIndex = deptIndex;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptState() {
		return deptState;
	}

	public void setDeptState(String deptState) {
		this.deptState = deptState;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public String getDeptUrl() {
		return deptUrl;
	}

	public void setDeptUrl(String deptUrl) {
		this.deptUrl = deptUrl;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

//	public CodeDto getCodeDto() {
//		return codeDto;
//	}
//
//	public void setCodeDto(CodeDto codeDto) {
//		this.codeDto = codeDto;
//	}

//	public void generateToken(String salt) {
//		if (salt == null)
//			token = DigestUtils.sha1Hex(StringUtils.trimToEmpty(deptId)
//					+ Long.toString(serialVersionUID));
//		else
//			token = DigestUtils.sha1Hex(StringUtils.trimToEmpty(deptId) + salt);
//
//	}
//
//	public boolean tokenKeeped(String salt) {
//		if (salt == null)
//			return (DigestUtils.sha1Hex(StringUtils.trimToEmpty(deptId)
//					+ Long.toString(serialVersionUID))).equals(token);
//		else
//			return DigestUtils.sha1Hex(StringUtils.trimToEmpty(deptId) + salt).equals(
//					token);
//	}
	public void generateToken(String salt) {
		if (salt == null)
			token = DigestUtils.sha1Hex(StringUtils.trimToEmpty(id)
					+ Long.toString(serialVersionUID));
		else
			token = DigestUtils.sha1Hex(StringUtils.trimToEmpty(id) + salt);

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
