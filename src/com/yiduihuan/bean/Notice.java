package com.yiduihuan.bean;

import org.nutz.dao.entity.annotation.*;

/**
 * 公告
 *
 * @author Howe(howechiang@gmail.com)
 */
@Table("tb_yiduihuan_notice")
public class Notice {

    /**
     *
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
     * 标题
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
     * 内容摘要
     */
    @Column("summary")
    private String summary;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * 内容
     */
    @Column("contents")
    private String contents;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
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
     * 最后更新时间
     */
    @Column("editTime")
    private java.util.Date editTime;

    public java.util.Date getEditTime() {
        return editTime;
    }

    public void setEditTime(java.util.Date editTime) {
        this.editTime = editTime;
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