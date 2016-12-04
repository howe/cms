package com.yiduihuan.module;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.yiduihuan.bean.LoginLogs;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.yiduihuan.tool.SessionManger.USER_SESSION_KEY;

@IocBean(fields = {"dao"})
@Ok("json:{locked:'password|salt', ignoreNull:true}")
@Fail("http:500")
public class ControlModule extends EntityService<User> {

    /**
     * 登录页
     *
     * @return
     * @throws IOException
     */
    @GET
    @At("/admin/login")
    @Ok("beetl:/admin/login.btl")
    public Map<String, Object> login() throws IOException {

        if (SessionManger.checkLogin(Mvcs.getReq()))
            Mvcs.getResp().sendRedirect("/admin/index.htm");
        Map<String, Object> map = new HashMap<String, Object>();
        String returnUrl = Mvcs.getReq().getParameter("returnUrl");
        if (StrUtil.isNotBlank(returnUrl))
            map.put("returnUrl", returnUrl);
        return map;
    }

    /**
     * 登录
     *
     * @param mobile
     * @param password
     * @return
     */
    @POST
    @At("/admin/login")
    @Ok("redirect:${obj}")
    public String login(@Param("mobile") String mobile,
                        @Param("password") String password) {

        String returnUrl = Mvcs.getReq().getParameter("returnUrl");

        if (StrUtil.isNotBlank(mobile) && StrUtil.isNotBlank(password)) {
            User user = dao().fetch(
                    User.class,
                    Cnd.wrap("mobile = '" + mobile.trim() + "' and password = md5(concat(md5('" + password
                            + "'), salt))"));
            if (user != null) {
                String salt = RandomUtil.randomString(6);

                user.setSalt(salt);
                user.setPassword(CodeCryption.encode("MD5", CodeCryption.encode("MD5", password) + salt));
                dao().update(user);
                LoginLogs log = new LoginLogs();
                log.setUid(user.getId());
                log.setIp(HttpUtil.getClientIP(Mvcs.getReq()));
                log.setLoginDate(Times.now());
                log.setAgent(Mvcs.getReq().getHeader("User-Agent"));
                dao().fastInsert(log);
                Mvcs.getReq().getSession().setAttribute(USER_SESSION_KEY, user);
                if (StrUtil.isNotBlank(returnUrl))
                    return returnUrl;
                else
                    return "/admin/index.htm";
            } else
                return "/admin/login.htm";
        } else
            return "/admin/login.htm";
    }

    @GET
    @At("/admin/index")
    @Ok("beetl:/admin/index.btl")
    @Filters(@By(type = CheckSession.class, args = {SessionManger.USER_SESSION_KEY, "/login.htm?returnUrl=/admin/index.htm"}))
    public Map<String, Object> index() throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        User user = SessionManger.getCurrentUser(Mvcs.getReq());
        LoginLogs logs = dao().fetch(LoginLogs.class, Cnd.where("uid", "=", user != null ? user.getId() : null).orderBy("loginDate", "desc"));
        map.put("user", user);
        map.put("logs", logs);
        return map;
    }

    @At("/admin/logout")
    @Ok("redirect:/admin/login.htm")
    public void logout(HttpServletRequest req) throws IOException {
        req.getSession().invalidate();
    }

    @Ok("raw")
    @At("/checkpasswd")
    public String checkPasswd() {

        String mobile = Mvcs.getReq().getParameter("mobile").trim();
        String password = Mvcs.getReq().getParameter("password");

        if (StrUtil.isBlank(password))
            return "{\"data\":{\"error\":\"密码不能为空\"}}";
        else {
            User user = dao().fetch(
                    User.class,
                    Cnd.wrap("mobile = '" + mobile
                            + "' and password = md5(concat(md5('" + password + "'), salt))"));
            if (user != null)
                return "{\"data\":{\"ok\":\"\"}}";
            else
                return "{\"data\":{\"error\":\"密码错误\"}}";
        }
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
                    Cnd.wrap("email = '" + user.getEmail()
                            + "' and password = md5(concat(md5('" + password + "'), salt))"));
            if (user != null)
                return "{\"data\":{\"ok\":\"成功修改密码后将自动退出系统\"}}";
            else
                return "{\"data\":{\"error\":\"密码错误\"}}";
        }
    }

    @Ok("raw")
    @At("/checkemail")
    public String checkEmail() {

        String email = Mvcs.getReq().getParameter("email").toLowerCase();

        if (StrUtil.isBlank(email))
            return "{\"data\":{\"error\":\"邮箱不能为空\"}}";
        else {
            User user = dao().fetch(User.class,
                    Cnd.where("email", "=", email.trim()));
            if (user != null)
                return "{\"data\":{\"ok\":\"\"}}";
            else
                return "{\"data\":{\"error\":\"邮箱不存在\"}}";
        }
    }

    @Ok("raw")
    @At("/checkmobile")
    public String checkMobile() {

        String mobile = Mvcs.getReq().getParameter("mobile").trim();

        if (StrUtil.isBlank(mobile))
            return "{\"data\":{\"error\":\"手机号码不能为空\"}}";
        else {
            User user = dao().fetch(User.class, Cnd.where("mobile", "=", mobile));
            if (user != null)
                return "{\"data\":{\"ok\":\"\"}}";
            else
                return "{\"data\":{\"error\":\"手机号码不存在\"}}";
        }
    }
}
