package com.yiduihuan.module;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.yiduihuan.bean.OperLogs;
import com.yiduihuan.bean.User;
import com.yiduihuan.tool.SessionManger;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.service.EntityService;

import com.yiduihuan.bean.Explain;

@IocBean(fields = {"dao"})
@Ok("json:{locked:'password|salt', ignoreNull:true}")
@Fail("http:500")
public class ExplainModule extends EntityService<Explain> {

    @GET
    @At("/explain/item/?")
    @Ok("beetl:/explain/${pathargs[0]}.btl")
    @Fail("void")
    public Map<String, Object> item(@Param("id") String id) throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();

        Explain explain = dao().fetch(Explain.class, Cnd.where("id", "=", id).and("display", "=", 1));
        if (explain == null)
            Mvcs.getResp().sendRedirect("/error/404.html");
        map.put("explain", explain);

        return map;
    }

    @GET
    @At("/explain/list")
    @Ok("beetl:/explain/list.btl")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/explain/list.htm"}))
    public Map<String, Object> list() throws IOException {

        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        List<Explain> explainList = dao().query(Explain.class, Cnd.orderBy().desc("releaseTime"));
        dao().fetchLinks(explainList, "user");
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("user", user);
        map.put("explainList", explainList);
        return map;
    }

    @GET
    @At("/explain/item")
    @Ok("beetl:/explain/item.btl")
    public Map<String, Object> item() throws IOException {

        String id = Mvcs.getReq().getParameter("id");
        if (!SessionManger.checkLogin(Mvcs.getReq()))
            Mvcs.getResp().sendRedirect("/login.htm?returnUrl=/explain/item-" + id + ".htm");
        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        map.put("user", user);

        Explain explain = dao().fetch(Explain.class, Cnd.where("id", "=", id));
        if (explain == null)
            Mvcs.getResp().sendRedirect("/error/404.html");
        map.put("explain", explain);
        return map;
    }

    @GET
    @At("/explain/add")
    @Ok("beetl:/explain/add.btl")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/explain/add.htm"}))
    public Map<String, Object> add() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        map.put("user", user);
        return map;
    }

    @POST
    @At("/explain/add")
    @Ok("redirect:${obj}")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/explain/add.htm"}))
    public String add(@Param("title") String title, @Param("display") String display) {

        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(display)) {
            Explain explain = new Explain();
            explain.setDisplay(Integer.parseInt(display));
            explain.setEditor(user.getId());
            explain.setReleaseTime(Times.now());
            explain.setTitle(title);
            dao().insert(explain);
            OperLogs operLogs = new OperLogs();
            operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
            operLogs.setOperDate(Times.now());
            operLogs.setUid(user.getId());
            operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
            operLogs.setAction("添加活页（ID-" + explain.getId() + "）");
            dao().insert(operLogs);
            return "/explain/add.htm?alert=success";
        } else
            return "/explain/add.htm?alert=error";
    }

    @POST
    @At("/explain/modify")
    @Ok("redirect:${obj}")
    public String edit(@Param("title") String title, @Param("display") String display) {

        String id = Mvcs.getReq().getParameter("id");
        if (!SessionManger.checkLogin(Mvcs.getReq()))
            return "/login.htm?returnUrl=/explain/item-" + id + ".htm";
        User user = SessionManger.getCurrentUser(Mvcs.getReq());

        if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(display)
                && StrUtil.isNotBlank(id)) {

            Explain explain = dao().fetch(Explain.class, Cnd.where("id", "=", id));
            if (explain == null)
                return "/error/404.html";
            explain.setDisplay(Integer.parseInt(display));
            explain.setTitle(title);
            dao().update(explain);
            OperLogs operLogs = new OperLogs();
            operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
            operLogs.setOperDate(Times.now());
            operLogs.setUid(user.getId());
            operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
            operLogs.setAction("更新活页（ID-" + explain.getId() + "）");
            dao().insert(operLogs);
            return "/explain/item-" + id + ".htm?alert=success";
        } else
            return "/explain/item-" + id + ".htm?alert=error";
    }

    @GET
    @At("/explain/operate")
    @Ok("redirect:${obj}")
    public String operate(@Param("handle") String handle) {

        String id = Mvcs.getReq().getParameter("id");
        if (!SessionManger.checkLogin(Mvcs.getReq()))
            return "/login.htm?returnUrl=/explain/operate-" + id + "-"+ handle +".htm";
        User user = SessionManger.getCurrentUser(Mvcs.getReq());

        if (StrUtil.isNotBlank(handle) && StrUtil.isNotBlank(id)) {

            Explain explain = dao().fetch(Explain.class, Cnd.where("id", "=", id));
            if (explain == null)
                return "/error/404.html";
            OperLogs operLogs = new OperLogs();
            if (StrUtil.equals(handle, "display")){
                explain.setDisplay(1);
                dao().update(explain);
                operLogs.setAction("显示活页（ID-" + explain.getId() + "）");
            } else if(StrUtil.equals(handle, "block")){
                explain.setDisplay(0);
                dao().update(explain);
                operLogs.setAction("隐藏活页（ID-" + explain.getId() + "）");
            } else if(StrUtil.equals(handle, "del")){
                explain.setDisplay(0);
                dao().delete(explain);
                operLogs.setAction("删除活页（ID-" + explain.getId() + "）");
            } else
                return "/explain/list.htm";
            operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
            operLogs.setOperDate(Times.now());
            operLogs.setUid(user.getId());
            operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
            dao().insert(operLogs);
            return "/explain/list.htm?alert=success";
        } else
            return "/explain/list.htm?alert=error";
    }
}