package com.yiduihuan.bean;

import org.nutz.dao.entity.annotation.*;

/**
 * 操作日志
 * @author Howe(howechiang@gmail.com)
 *
 */
@Table("tb_yiduihuan_oper_logs")
public class OperLogs {

	/**
	 * ID
	 */
	@Id
	@Column("id")
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * UID
	 */
	@Column("uid")
	private Integer uid;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	/**
	 * 动作
	 */
	@Column("action")
	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * 登录设备
	 */
	@Column("agent")
	private String agent;

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * 登录IP
	 */
	@Column("ip")
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 登录时间
	 */
	@Column("operDate")
	private java.util.Date operDate;

	public java.util.Date getOperDate() {
		return operDate;
	}

	public void setOperDate(java.util.Date operDate) {
		this.operDate = operDate;
	}
}