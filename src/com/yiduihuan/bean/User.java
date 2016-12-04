package com.yiduihuan.bean;

import org.nutz.dao.entity.annotation.*;

/**
*
* @author Howe(howechiang@gmail.com)
* 
*/
@Table("tb_yiduihuan_user")
public class User {

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
	 * 名称
	 */
	@Column("name")
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 邮箱
	 */
	@Column("email")
	private String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * 手机号
	 */
	@Column("mobile")
	private String mobile;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * 加密后密码
	 */
	@Column("password")
	private String password;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * QQ
	 */
	@Column("qq")
	private String qq;
	
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	
	/**
	 * 邮箱验证
	 */
	@Column("mailVerify")
	private Integer mailVerify;
	
	public Integer getMailVerify() {
		return mailVerify;
	}
	public void setMailVerify(Integer mailVerify) {
		this.mailVerify = mailVerify;
	}
	
	/**
	 * 注册IP
	 */
	@Column("regIP")
	private String regIp;
	
	public String getRegIp() {
		return regIp;
	}
	public void setRegIp(String regIp) {
		this.regIp = regIp;
	}
	
	/**
	 * 注册时间
	 */
	@Column("regDate")
	private java.util.Date regDate;
	
	public java.util.Date getRegDate() {
		return regDate;
	}
	public void setRegDate(java.util.Date regDate) {
		this.regDate = regDate;
	}
	
	/**
	 * 加密钥
	 */
	@Column("salt")
	private String salt;
	
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	/**
	 * 登录次数
	 */
	@Column("loginTimes")
	private Integer loginTimes;
	
	public Integer getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}
	
	/**
	 * 错误次数
	 */
	@Column("errorTimes")
	private Integer errorTimes;
	
	public Integer getErrorTimes() {
		return errorTimes;
	}
	public void setErrorTimes(Integer errorTimes) {
		this.errorTimes = errorTimes;
	}
	
	/**
	 * 密保问题
	 */
	@Column("question")
	private String question;
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	/**
	 * 密保答案
	 */
	@Column("answer")
	private String answer;
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * 用户组ID
	 */
	@Column("groupId")
	private Integer groupId;

	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	/**
	 * 公司编号
	 */
	@Column("c")
	private Integer c;

	public Integer getC() {
		return c;
	}

	public void setC(Integer c) {
		this.c = c;
	}
	
}