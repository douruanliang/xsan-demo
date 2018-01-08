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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the dept database table.
 * 
 */
@Entity
@Table(name="xs_dept_info")
@NamedQuery(name="Dept.findAll", query="SELECT d FROM Dept d")
@NamedQueries({
	@NamedQuery(name = "Dept.findDeptTree", query = "select d from Dept d left join fetch d.children"),
	@NamedQuery(name = "Dept.findRoots", query = "select d from Dept d where d.parent is null")
	})
public class Dept extends Audited  implements Serializable {
	

	private static final long serialVersionUID = 5119673746393145493L;


	@Column(name="dept_desc")
	private String deptDesc;
	@Column(name="dept_VdcId")
	private String deptVdcId;
	
	
	public String getDeptVdcId() {
		return deptVdcId;
	}


	public void setDeptVdcId(String deptVdcId) {
		this.deptVdcId = deptVdcId;
	}

	@Column(name="dept_index")
	private Integer deptIndex;

	@Column(name="dept_name")
	private String deptName;

	@Column(name="dept_state")
	private String deptState;

	@Column(name="dept_type")
	private String deptType;

	@Column(name="dept_url")
	private String deptUrl;

	//uni-directional many-to-many association to User
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
		name="xs_dept_user"
		, joinColumns={
			@JoinColumn(name="dept_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="user_id")
			}
		)
	private List<User> users;
	
	@ManyToOne(cascade = { CascadeType.REFRESH }, optional = false,fetch=FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	private Dept parent = null;

	@OrderBy("deptIndex asc")
	@Where(clause="flag = 1")
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH,CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id",insertable = false, updatable = false)
	private List<Dept> children = new LinkedList<Dept>();

	
	

	private String remark;
	
	public Dept() {
	}

	
	@JsonIgnore
	public Dept getParent() {
		return parent;
	}


	public void setParent(Dept parent) {
		this.parent = parent;
	}


	@JsonIgnore
	public List<Dept> getChildren() {
		return children;
	}


	public void setChildren(List<Dept> children) {
		this.children = children;
	}







	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getDeptDesc() {
		return this.deptDesc;
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
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptState() {
		return this.deptState;
	}

	public void setDeptState(String deptState) {
		this.deptState = deptState;
	}

	public String getDeptType() {
		return this.deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public String getDeptUrl() {
		return this.deptUrl;
	}

	public void setDeptUrl(String deptUrl) {
		this.deptUrl = deptUrl;
	}

//	public String getParentId() {
//		return this.parentId;
//	}
//
//	public void setParentId(String parentId) {
//		this.parentId = parentId;
//	}

	@JsonIgnore
	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}