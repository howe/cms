package com.yiduihuan.bean;

import org.nutz.dao.entity.annotation.*;

/**
 * 登录日志
 *
 * @author Howe(howechiang@gmail.com)
 */
@Table("tb_yiduihuan_login_logs")
public class LoginLogs {

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
    @Column("loginDate")
    private java.util.Date loginDate;

    public java.util.Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(java.util.Date loginDate) {
        this.loginDate = loginDate;
    }
}