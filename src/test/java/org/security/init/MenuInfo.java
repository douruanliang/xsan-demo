package org.security.init;

public enum MenuInfo {
	
	sys("sys","系统管理",1,null,null),
	    user("user","用户管理",1,"sys",null),
	         enableuser("enableuser","所有人员",1,"user","sys/user-list"),
	    dept("deptMsg","部门管理",2,"sys",null),
	         org("org","组织机构",1,"deptMsg","sys/dept-user"),
	    authority("authority","权限管理",3,"sys",null),
	              role("role","角色管理",1,"authority","sys/role-list"),
	    resource("resource","资源管理",4,"sys",null),
	             menu("menu","菜单管理",1,"resource","sys/menu-list"),
	    log("log","日志管理",5,"sys",null),
	       sysLog("syslog","系统日志",1,"log","sys/log-list"),
	    weixin("weixin","微信管理",6,"sys",null);
	 
	public String mark;
	public String menuName;
	public int menuIndex;
	public String parent_id;
	public String menuUrl;
	
	private MenuInfo(String mark, String menuName, int menuIndex, String parent_id, String menuUrl) {
		this.mark = mark;
		this.menuName = menuName;
		this.menuIndex = menuIndex;
		this.parent_id = parent_id;
		this.menuUrl = menuUrl;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public int getMenuIndex() {
		return menuIndex;
	}
	public void setMenuIndex(int menuIndex) {
		this.menuIndex = menuIndex;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	
	
}
