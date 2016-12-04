package com.yiduihuan.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 帮助类别
 *
 * @author Howe(howechiang@gmail.com)
 */
@Table("tb_yiduihuan_help_category")
public class HelpCategory {

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
     * 类别名称
     */
    @Column("categoryName")
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 父级ID
     */
    @Column("pid")
    private Integer pid;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}