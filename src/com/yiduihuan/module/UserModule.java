package com.yiduihuan.module;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.yiduihuan.bean.LoginLogs;
import com.yiduihuan.bean.OperLogs;
import com.yiduihuan.bean.User;
import com.yiduihuan.tool.CodeCryption;
import com.yiduihuan.tool.SessionManger;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.service.EntityService;

@IocBean
@Ok("json:{locked:'password|salt', ignoreNull:true}")
@Fail("http:500")
public class UserModule extends EntityService<User> {


    @GET
    @At("/admin/user/info")
    @Ok("beetl:/admin/user/info.btl")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/admin/user/info.htm"}))
    public Map<String, Object> info() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        map.put("user", user);

        return map;
    }

    @GET
    @At("/admin/user/operationlogs")
    @Ok("beetl:/admin/user/operationlogs.btl")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/admin/user/operationlogs.htm"}))
    public Map<String, Object> operlogs() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        map.put("user", user);
        List<OperLogs> operLogsList = dao().query(OperLogs.class, Cnd.where("uid", "=", user.getId()).desc("operDate"));
        map.put("operLogsList", operLogsList);
        return map;
    }

    @GET
    @At("/admin/user/loginlogs")
    @Ok("beetl:/admin/user/loginlogs.btl")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/admin/user/loginlogs.htm"}))
    public Map<String, Object> loginlogs() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        map.put("user", user);
        List<LoginLogs> loginLogsList = dao().query(LoginLogs.class, Cnd.where("uid", "=", user.getId()).desc("loginDate"));
        map.put("loginLogsList", loginLogsList);
        return map;
    }

    @Ok("raw")
    @At("/checkpswd")
    public String checkPswd() {

        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        String password = Mvcs.getReq().getParameter("password");

        if (StrUtil.isBlank(password))
            return "{\"data\":{\"error\":\"密码不能为空\"}}";
        else if (user == null)
            return "{\"data\":{\"error\":\"登录信息过期\"}}";
        else {
            user = dao().fetch(
                    User.class,
                    Cnd.wrap("mobile = '" + user.getMobile()
                            + "' and password = md5(concat(md5('" + password
                            + "'), salt))"));
            if (user != null)
                return "{\"data\":{\"ok\":\"成功修改密码后将自动退出系统\"}}";
            else
                return "{\"data\":{\"error\":\"密码错误\"}}";
        }
    }

    @POST
    @At("/admin/user/modify")
    @Ok("redirect:${obj}")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/admin/user/info.htm"}))
    public String modify(@Param("pswd") String pswd, @Param("qq") String qq,
                         @Param("email") String email, @Param("password") String password) {

        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        if (StrUtil.isNotBlank(password) && StrUtil.isNotBlank(pswd)) {
            if (CodeCryption.encode("MD5", CodeCryption.encode("MD5", password) + user.getSalt()).equals(user.getPassword())) {
                String salt = RandomUtil.randomString(6);
                user.setSalt(salt);
                user.setEmail(email);
                user.setQq(qq);
                user.setPassword(CodeCryption.encode("MD5", CodeCryption.encode("MD5", pswd) + salt));
                dao().update(user);
                OperLogs operLogs = new OperLogs();
                operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
                operLogs.setOperDate(Times.now());
                operLogs.setUid(user.getId());
                operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
                operLogs.setAction("修改自己密码");
                dao().insert(operLogs);
                return "/admin/user/info.htm?alert=success";
            } else
                return "/admin/user/info.htm?alert=error";
        } else {
            user.setEmail(email);
            user.setQq(qq);
            dao().update(user);
            OperLogs operLogs = new OperLogs();
            operLogs.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
            operLogs.setOperDate(Times.now());
            operLogs.setUid(user.getId());
            operLogs.setAgent(Mvcs.getReq().getHeader("User-Agent"));
            operLogs.setAction("修改自己资料");
            dao().insert(operLogs);
            return "/admin/user/info.htm?alert=success";
        }
    }
}