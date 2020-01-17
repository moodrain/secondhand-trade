package muyu.trade.service;

import muyu.trade.entity.User;
import muyu.trade.model.Response;
import muyu.trade.repository.OrderRepository;
import muyu.trade.repository.UserRepository;
import muyu.trade.util.HttpClient;
import muyu.trade.util.PhoneValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private HttpClient httpClient;
    private PhoneValidator phoneValidator;
    private OrderRepository orderRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, HttpClient httpClient, PhoneValidator phoneValidator, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.httpClient = httpClient;
        this.phoneValidator = phoneValidator;
        this.orderRepository = orderRepository;
    }
    public User login(String username, String password, HttpSession session) {
        User user = userRepository.findUserByPhone(username);
        if(user != null && passwordEncoder.matches(password, user.getPassword()))
        {
            session.setAttribute("user", user);
            user.setPassword("");
            return user;
        }
        return null;
    }
    public boolean sendCaptcha(String phone, HttpSession session) {
        if(!phoneValidator.validate(phone))
            return false;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("token", "");
        try {
            String SMSApi = "";
            String captcha = httpClient.post(SMSApi, map);
            session.setAttribute("captchaPhone", phone);
            session.setAttribute("captcha", captcha);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public String register(HttpSession session, String phone, String password, String captcha) {
        if(!phoneValidator.validate(phone))
            return "手机号格式不正确";
        if(!phone.equals(session.getAttribute("captchaPhone")) || !captcha.equals(session.getAttribute("captcha")))
            return "验证码错误";
        if(userRepository.findUserByPhone(phone) != null)
            return "该手机号已经注册过了";
        User user = new User();
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setIsHistoryPublic(1);
        user.setIsInfoPublic(1);
        userRepository.save(user);
        user.setNick("用户" + user.getId());
        userRepository.save(user);
        session.setAttribute("user", user);
        return null;
    }
    public Boolean resetPassword(HttpSession session, String phone, String captcha, String password) {
        if(phone.equals(session.getAttribute("captchaPhone")) && captcha.equals(session.getAttribute("captcha")) && !password.isEmpty()) {
            User user = userRepository.findUserByPhone(phone);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public Response<Object> editUserInfo(HttpSession session, User user) {
        User oldUser = userRepository.findOne(((User)session.getAttribute("user")).getId());
        if(user.getNick() == null || user.getNick().isEmpty())
            return new Response<>(400, "昵称不能为空", null);
        user.setPhone(oldUser.getPhone());
        user.setId(oldUser.getId());
        user.setPassword(oldUser.getPassword());
        user.setAvatar(oldUser.getAvatar());
        user.setIsInfoPublic(oldUser.getIsInfoPublic());
        user.setIsHistoryPublic(oldUser.getIsHistoryPublic());
        userRepository.save(user);
        session.setAttribute("user", user);
        return new Response<>();
    }
    public Response<Object> uploadAvatar(HttpSession session, String file) {
        User user = userRepository.findOne(((User)session.getAttribute("user")).getId());
        user.setAvatar(file);
        userRepository.save(user);
        session.setAttribute("user", user);
        return new Response<>();
    }
    public Response<Object> updatePassword(HttpSession session, String oldPassword, String newPassword) {
        User user = userRepository.findOne(((User)session.getAttribute("user")).getId());
        if(!passwordEncoder.matches(oldPassword, user.getPassword()))
            return new Response<>(400, "原密码错误", null);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new Response<>();
    }
    public Response<Map> user(Integer id) {
        User user = userRepository.findOne(id);
        Map map = new HashMap();
        if(user == null)
            return new Response<Map>(200, "", null);
        else if(user.getIsHistoryPublic().equals(1))
            map.put("historyOrder", orderRepository.findOrdersByBuyerOrSeller(user, user).size());
        else if(!user.getIsInfoPublic().equals(1)) {
            User newUser = new User();
            newUser.setId(user.getId());
            newUser.setAvatar(user.getAvatar());
            newUser.setNick(user.getNick());
            newUser.setIsHistoryPublic(user.getIsHistoryPublic());
            newUser.setIsInfoPublic(user.getIsInfoPublic());
            user = newUser;
        }
        map.put("userInfo", user);
        return new Response<Map>(200, "", map);
    }
    public Response<List<User>> allUser(Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return new Response<List<User>>(200, "", userRepository.findAll(pageable).getContent());
    }
    public Integer count()
    {
        return Math.toIntExact(userRepository.count());
    }
}
