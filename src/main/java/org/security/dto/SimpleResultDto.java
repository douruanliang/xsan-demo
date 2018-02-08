package org.security.dto;


/**
 * 
 * 类名称：SimpleResultDto   
 * 类描述： 简单的数据结果类
 * 创建人：dourl   
 * 创建时间：2018年1月10日 下午2:14:55 
 * @version
 */
public class SimpleResultDto {
 
    private boolean success;
    private String msg;
	private Object obj;
	
	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
