package com.yiduihuan.bean;

import org.nutz.dao.entity.annotation.*;

/**
 * 网站说明
 *
 * @author Howe(howechiang@gmail.com)
 */
@Table("tb_yiduihuan_explain")
public class Explain {

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
     * 说明标题
     */
    @Column("title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 发布时间
     */
    @Column("releaseTime")
    private java.util.Date releaseTime;

    public java.util.Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(java.util.Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    /**
     * 编辑ID
     */
    @Column("editor")
    private Integer editor;

    public Integer getEditor() {
        return editor;
    }

    public void setEditor(Integer editor) {
        this.editor = editor;
    }

    /**
     * 显示状态
     */
    @Column("display")
    private Integer display;

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

}