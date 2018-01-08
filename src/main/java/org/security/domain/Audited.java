package org.security.domain;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@MappedSuperclass
public class Audited {

	@Id
	@Column(name = "id")
	private String id;
	@CreatedBy
	protected String createdBy;
	@CreatedDate
	protected Calendar createdDate;
	@LastModifiedBy
	protected String modifiedBy;
	@LastModifiedDate
	protected Calendar modifiedDate;

	private final int del_flag = 1;

	public Integer flag;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

    @PrePersist
	public void createAuditInfo(){
		SecurityContext context = SecurityContextHolder.getContext();
		if (null!=context.getAuthentication()&&!context.getAuthentication().isAuthenticated()) {
			UserDetails userDetails = (UserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			setCreatedBy(null == userDetails ? "" : userDetails.getUsername());
		}
		setCreatedDate(Calendar.getInstance());
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		this.setId(id);
		this.setFlag(del_flag);// 默认初始化
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@PreUpdate
	public void  updateAuditInfo() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (null!=context.getAuthentication()&&!context.getAuthentication().isAuthenticated()) {
			UserDetails userDetails = (UserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			setModifiedBy(null == userDetails ? "" : userDetails.getUsername());
		}
		setModifiedDate(Calendar.getInstance());
	}
}
