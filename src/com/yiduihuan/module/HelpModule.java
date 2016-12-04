package com.yiduihuan.module;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.yiduihuan.bean.HelpCategory;
import com.yiduihuan.bean.OperLogs;
import com.yiduihuan.bean.User;
import com.yiduihuan.tool.SessionManger;
import org.markdownj.MarkdownProcessor;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.service.EntityService;
import com.yiduihuan.bean.Help;

@IocBean
@Ok("json:{locked:'password|salt', ignoreNull:true}")
@Fail("http:500")
public class HelpModule extends EntityService<Help> {

    @GET
    @At("/help/list")
    @Ok("beetl:/help/list.btl")
    @Fail("void")
    public Map<String, Object> weblist() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();

        String hcId = Mvcs.getReq().getParameter("hcid");
        HelpCategory helpCategory = dao().fetch(HelpCategory.class, Cnd.where("id", "=", hcId));
        if (helpCategory == null)
            Mvcs.getResp().sendRedirect("/error/404.html");
        map.put("helpCategory", helpCategory);

        List<Help> helps = dao().query(Help.class, Cnd.where("hcid", "=", hcId).and("display", "=", 1).orderBy("releaseTime", "desc"));
        map.put("helps", helps);
        return map;
    }

    @GET
    @At("/help/item")
    @Ok("beetl:/help/item.btl")
    public Map<String, Object> webitem() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();

        String id = Mvcs.getReq().getParameter("id");
        Help help = dao().fetch(Help.class, Cnd.where("id", "=", id).and("display", "=", 1));
        if (help == null)
            Mvcs.getResp().sendRedirect("/error/404.html");
        help.setContents(new MarkdownProcessor().markdown(help.getContents()));
        map.put("help", help);
        HelpCategory helpCategory = dao().fetch(HelpCategory.class, Cnd.where("id", "=", help.getHcId()));
        map.put("helpCategory", helpCategory);
        return map;
    }

    @GET
    @At("/help/list")
    @Ok("beetl:/help/list.btl")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/help/list.htm"}))
    public Map<String, Object> list() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        map.put("user", user);
        List<Help> helpList = Daos.queryWithLinks(dao(),  Help.class, Cnd.orderBy().desc("releaseTime"), null, "helpCategory");
        dao().fetchLinks(helpList, "user");
        map.put("helpList", helpList);
        return map;
    }

    @GET
    @At("/help/item")
    @Ok("beetl:/help/item.btl")
    public Map<String, Object> item() throws IOException {

        String id = Mvcs.getReq().getParameter("id");
        if (!SessionManger.checkLogin(Mvcs.getReq()))
            Mvcs.getResp().sendRedirect("/login.htm?returnUrl=/help/item-" + id + ".htm");
        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        map.put("user", user);
        Help help = dao().fetch(Help.class, Cnd.where("id", "=", id));
        if (help == null)
            Mvcs.getResp().sendRedirect("/error/404.html");
        map.put("help", help);
        List<HelpCategory> helpCategoryList = dao().query(HelpCategory.class, Cnd.orderBy().desc("id"));
        map.put("helpCategoryList", helpCategoryList);
        return map;
    }

    @GET
    @At("/help/add")
    @Ok("beetl:/help/add.btl")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/help/add.htm"}))
    public Map<String, Object> add() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        map.put("user", user);
        List<HelpCategory> helpCategoryList = dao().query(HelpCategory.class, Cnd.orderBy().desc("id"));
        map.put("helpCategoryList", helpCategoryList);
        return map;
    }

    @POST
    @At("/help/add")
    @Ok("redirect:${obj}")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/notice/add.htm"}))
    public String add(@Param("title") String title, @Param("display") String display,
                      @Param("contents") String contents, @Param("summary") String summary,
                      @Param("hcId") String hcId) {

        User user = SessionManger.getCurrentUser(Mvcs.getReq());

        if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(display)
                && StrUtil.isNotBlank(contents) && StrUtil.isNotBlank(hcId)) {
            Help help = new Help();
            help.setContents(contents);
            help.setDisplay(Integer.parseInt(display));
            help.setEditor(user.getId());
            help.setReleaseTime(Times.now());
            help.setSummary(summary);
            help.setTitle(title);
            help.setHcId(Integer.parseInt(hcId));
            help.setEditTime(Times.now());
            dao().insert(help);
            OperLogs operLogs = new OperLogs();
            operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
            operLogs.setOperDate(Times.now());
            operLogs.setUid(user.getId());
            operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
            operLogs.setAction("添加帮助（ID-" + help.getId() + "）");
            dao().insert(operLogs);
            return "/help/add.htm?alert=success";
        } else
            return "/help/add.htm?alert=error";
    }

    @POST
    @At("/help/modify")
    @Ok("redirect:${obj}")
    public String edit(@Param("title") String title, @Param("display") String display,
                       @Param("contents") String contents, @Param("summary") String summary,
                       @Param("hcId") String hcId) {

        String id = Mvcs.getReq().getParameter("id");
        if (!SessionManger.checkLogin(Mvcs.getReq()))
            return "/login.htm?returnUrl=/help/item-" + id + ".htm";
        User user = SessionManger.getCurrentUser(Mvcs.getReq());

        if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(display)
                && StrUtil.isNotBlank(contents) && StrUtil.isNotBlank(id)) {
            Help help = dao().fetch(Help.class, Cnd.where("id", "=", id));
            if (help == null)
                return "/error/404.html";
            help.setContents(contents);
            help.setDisplay(Integer.parseInt(display));
            help.setSummary(summary);
            help.setTitle(title);
            help.setHcId(Integer.parseInt(hcId));
            help.setEditTime(Times.now());
            dao().update(help);
            OperLogs operLogs = new OperLogs();
            operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
            operLogs.setOperDate(Times.now());
            operLogs.setUid(user.getId());
            operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
            operLogs.setAction("更新帮助（ID-" + help.getId() + "）");
            dao().insert(operLogs);
            return "/help/item-" + id + ".htm?alert=success";
        } else
            return "/help/item-" + id + ".htm?alert=error";
    }

    @GET
    @At("/help/operate")
    @Ok("redirect:${obj}")
    public String operate(@Param("handle") String handle) {

        String id = Mvcs.getReq().getParameter("id");
        if (!SessionManger.checkLogin(Mvcs.getReq()))
            return "/login.htm?returnUrl=/help/operate-" + id + "-"+ handle +".htm";
        User user = SessionManger.getCurrentUser(Mvcs.getReq());

        if (StrUtil.isNotBlank(handle) && StrUtil.isNotBlank(id)) {
            Help help = dao().fetch(Help.class, Cnd.where("id", "=", id));
            if (help == null)
                return "/error/404.html";
            OperLogs operLogs = new OperLogs();
            if (StrUtil.equals(handle, "display")){
                help.setDisplay(1);
                dao().update(help);
                operLogs.setAction("显示帮助（ID-" + help.getId() + "）");
            } else if(StrUtil.equals(handle, "block")){
                help.setDisplay(0);
                dao().update(help);
                operLogs.setAction("隐藏帮助（ID-" + help.getId() + "）");
            } else if(StrUtil.equals(handle, "del")){
                help.setDisplay(0);
                dao().delete(help);
                operLogs.setAction("删除帮助（ID-" + help.getId() + "）");
            } else
                return "/help/list.htm";
            operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
            operLogs.setOperDate(Times.now());
            operLogs.setUid(user.getId());
            operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
            dao().insert(operLogs);
            return "/help/list.htm?alert=success";
        } else
            return "/help/list.htm?alert=error";
    }
}