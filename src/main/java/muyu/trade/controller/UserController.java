package muyu.trade.controller;

import muyu.trade.entity.Order;
import muyu.trade.entity.Type;
import muyu.trade.entity.User;
import muyu.trade.model.Response;
import muyu.trade.repository.TypeRepository;
import muyu.trade.repository.UserRepository;
import muyu.trade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository)
    {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "test")
    public void test()
    { }

    @PostMapping(value = "login")
    public Response<User> login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session)
    {
        User user = userService.login(username, password, session);
        return user == null ? new Response<User>(401, "账户密码错误", null) : new Response<User>(200, "", user);
    }

    @GetMapping(value = "user-info")
    public Response<User> isLogin(HttpSession session)
    {
        return new Response<User>(200, "", (User) session.getAttribute("user"));
    }

    @PostMapping(value = "send-captcha")
    public Response<Object> sendCaptcha(@RequestParam("phone") String phone, HttpSession session, @RequestParam("check-exist") Integer checkExist)
    {
        if(checkExist.equals(1) && userRepository.findUserByPhone(phone) == null)
            return new Response<>(400, "该手机号未注册", null);
        else
            return new Response<>(userService.sendCaptcha(phone, session) ? 200 : 500, "", null);
    }

    @PostMapping(value = "register")
    public Response<Object> register(HttpSession session, @RequestParam("phone") String phone, @RequestParam("password") String password, @RequestParam("captcha") String captcha)
    {
        String msg = userService.register(session, phone, password, captcha);
        return new Response<>(msg == null ? 200 : 400, msg == null ? "" : msg, null);
    }

    @PostMapping(value = "logout")
    public Response<Object> logout(HttpSession session)
    {
        session.invalidate();
        return new Response<>();
    }

    @PostMapping(value = "reset-password")
    public Response<Object> resetPassword(@RequestParam("phone") String phone, @RequestParam("captcha") String captcha,@RequestParam("password") String password,   HttpSession session)
    {
        Boolean rs = userService.resetPassword(session, phone, captcha, password);
        return new Response<>(rs ? 200 : 500, rs ? "" : "重置密码失败", null);
    }

    @PostMapping(value = "edit-user-info")
    public Response<Object> editUserInfo(HttpSession session, User user)
    {
        return userService.editUserInfo(session, user);
    }

    @PostMapping(value = "update-password")
    public Response<Object> updatePassword(HttpSession session, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword)
    {
        return userService.updatePassword(session, oldPassword, newPassword);
    }

    @PostMapping(value = "upload-avatar")
    public Response<Object> uploadAvatar(HttpSession session, @RequestParam("file") String file)
    {
        return userService.uploadAvatar(session, file);
    }

    @PostMapping(value = "update-setting")
    public Response<Object> updateSrtting(HttpSession session, @RequestParam("isInfoPublic") Integer isInfoPublic, @RequestParam("isHistoryPublic") Integer isHistoryPublic)
    {
        User user = userRepository.findOne(((User)session.getAttribute("user")).getId());
        user.setIsInfoPublic(isInfoPublic);
        user.setIsHistoryPublic(isHistoryPublic);
        userRepository.save(user);
        session.setAttribute("user", user);
        return new Response<>();
    }

    @GetMapping(value = "user")
    public Response<Map> user(@RequestParam("id") Integer id)
    {
        return userService.user(id);
    }

}
