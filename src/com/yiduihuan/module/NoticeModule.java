package com.yiduihuan.module;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.lang.Validator;
import com.xiaoleilu.hutool.util.StrUtil;
import com.yiduihuan.bean.OperLogs;
import com.yiduihuan.bean.User;
import com.yiduihuan.tool.SessionManger;
import org.markdownj.MarkdownProcessor;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.service.EntityService;
import com.yiduihuan.bean.Notice;

@IocBean(fields = {"dao"})
@Ok("json:{locked:'password|salt', ignoreNull:true}")
@Fail("http:500")
public class NoticeModule extends EntityService<Notice> {

    @GET
    @At("/notice/list")
    @Ok("beetl:/notice/list.btl")
    @Fail("void")
    public Map<String, Object> weblist() {
        
        int page = Validator.isNumber(Mvcs.getReq().getParameter("page")) ? Integer.parseInt(Mvcs.getReq().getParameter("page")) : 1;
        Pager pager = dao().createPager(page, 10);
        List<Notice> notices = dao().query(Notice.class, Cnd.where("display", "=", 1).desc("releaseTime"), pager);
        Map<String, Object> map = new HashMap<String, Object>();
        if (pager != null) {
            pager.setRecordCount(dao().count(Notice.class));
            map.put("pager", pager);
        }
        map.put("notices", notices);
        return map;
    }

    @GET
    @At("/notice/item")
    @Ok("beetl:/notice/item.btl")
    public Map<String, Object> webitem() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();

        String id = Mvcs.getReq().getParameter("id");
        Notice notice = dao().fetch(Notice.class, Cnd.where("id", "=", id).and("display", "=", 1));
        if (notice == null)
            Mvcs.getResp().sendRedirect("/error/404.html");
        notice.setContents(new MarkdownProcessor().markdown(notice.getContents()));
        map.put("notice", notice);
        return map;
    }

    @GET
    @At("/notice/list")
    @Ok("beetl:/notice/list.btl")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/notice/list.htm"}))
    public Map<String, Object> list() throws IOException {

        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        List<Notice> noticeList = dao().query(Notice.class, Cnd.orderBy().desc("releaseTime"));
        dao().fetchLinks(noticeList, "user");
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("user", user);
        map.put("noticeList", noticeList);
        return map;
    }

    @GET
    @At("/notice/item")
    @Ok("beetl:/notice/item.btl")
    public Map<String, Object> item() throws IOException {

        String id = Mvcs.getReq().getParameter("id");
        if (!SessionManger.checkLogin(Mvcs.getReq()))
            Mvcs.getResp().sendRedirect("/login.htm?returnUrl=/notice/item-" + id + ".htm");

        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        map.put("user", user);

        Notice notice = dao().fetch(Notice.class, Cnd.where("id", "=", id));
        if (notice == null)
            Mvcs.getResp().sendRedirect("/error/404.html");
        map.put("notice", notice);
        return map;
    }

    @GET
    @At("/notice/add")
    @Ok("beetl:/notice/add.btl")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/notice/add.htm"}))
    public Map<String, Object> add() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        map.put("user", user);
        return map;
    }

    @POST
    @At("/notice/add")
    @Ok("redirect:${obj}")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/notice/add.htm"}))
    public String add(@Param("title") String title, @Param("display") String display,
                      @Param("contents") String contents, @Param("summary") String summary) {

        User user = SessionManger.getCurrentUser(Mvcs.getReq());

        if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(display)
                && StrUtil.isNotBlank(contents)) {

            Notice notice = new Notice();
            notice.setContents(contents);
            notice.setDisplay(Integer.parseInt(display));
            notice.setEditor(user.getId());
            notice.setReleaseTime(Times.now());
            notice.setSummary(summary);
            notice.setTitle(title);
            notice.setEditTime(Times.now());
            dao().insert(notice);
            OperLogs operLogs = new OperLogs();
            operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
            operLogs.setOperDate(Times.now());
            operLogs.setUid(user.getId());
            operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
            operLogs.setAction("添加公告（ID-" + notice.getId() + "）");
            dao().insert(operLogs);
            return "/notice/add.htm?alert=success";
        } else
            return "/notice/add.htm?alert=error";
    }

    @POST
    @At("/notice/modify")
    @Ok("redirect:${obj}")
    public String edit(@Param("title") String title, @Param("display") String display,
                       @Param("contents") String contents, @Param("summary") String summary) {

        String id = Mvcs.getReq().getParameter("id");
        if (!SessionManger.checkLogin(Mvcs.getReq()))
            return "/login.htm?returnUrl=/notice/item-" + id + ".htm";
        User user = SessionManger.getCurrentUser(Mvcs.getReq());

        if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(display)
                && StrUtil.isNotBlank(contents) && StrUtil.isNotBlank(id)) {

            Notice notice = dao().fetch(Notice.class, Cnd.where("id", "=", id));
            if (notice == null)
                return "/error/404.html";
            notice.setContents(contents);
            notice.setDisplay(Integer.parseInt(display));
            notice.setSummary(summary);
            notice.setTitle(title);
            notice.setEditTime(Times.now());
            dao().update(notice);
            OperLogs operLogs = new OperLogs();
            operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
            operLogs.setOperDate(Times.now());
            operLogs.setUid(user.getId());
            operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
            operLogs.setAction("更新公告（ID-" + notice.getId() + "）");
            dao().insert(operLogs);
            return "/notice/item-" + id + ".htm?alert=success";
        } else
            return "/notice/item-" + id + ".htm?alert=error";
    }

    @GET
    @At("/notice/operate")
    @Ok("redirect:${obj}")
    public String operate(@Param("handle") String handle) {

        String id = Mvcs.getReq().getParameter("id");
        if (!SessionManger.checkLogin(Mvcs.getReq()))
            return "/login.htm?returnUrl=/notice/operate-" + id + "-"+ handle +".htm";
        User user = SessionManger.getCurrentUser(Mvcs.getReq());

        if (StrUtil.isNotBlank(handle) && StrUtil.isNotBlank(id)) {

            Notice notice = dao().fetch(Notice.class, Cnd.where("id", "=", id));
            if (notice == null)
                return "/error/404.html";
            OperLogs operLogs = new OperLogs();
            if (StrUtil.equals(handle, "display")){
                notice.setDisplay(1);
                dao().update(notice);
                operLogs.setAction("显示公告（ID-" + notice.getId() + "）");
            } else if(StrUtil.equals(handle, "block")){
                notice.setDisplay(0);
                dao().update(notice);
                operLogs.setAction("隐藏公告（ID-" + notice.getId() + "）");
            } else if(StrUtil.equals(handle, "del")){
                notice.setDisplay(0);
                dao().delete(notice);
                operLogs.setAction("删除公告（ID-" + notice.getId() + "）");
            } else
                return "/notice/list.htm";
            operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
            operLogs.setOperDate(Times.now());
            operLogs.setUid(user.getId());
            operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
            dao().insert(operLogs);
            return "/notice/list.htm?alert=success";
        } else
            return "/notice/list.htm?alert=error";
    }
}