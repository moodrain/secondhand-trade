package muyu.trade.aspect;

import muyu.trade.model.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpSession;

@Aspect
@Component
public class CommonAspect {

    @Pointcut("execution(public * muyu.trade.controller..*.*(..)) " +
            "&& !execution(* muyu.trade.controller.AdminController.*(..)) " +
            "&& !execution(* muyu.trade.controller.UserController.login(..)) " +
            "&& !execution(* muyu.trade.controller.UserController.register(..)) " +
            "&& !execution(* muyu.trade.controller.UserController.resetPassword(..)) " +
            "&& !execution(* muyu.trade.controller.UserController.sendCaptcha(..)) " +
            "&& !execution(* muyu.trade.controller.MessageController.notice(..)) " +
            "&& !execution(* muyu.trade.controller.GoodsController.types(..))" +
            "&& !execution(* muyu.trade.controller.GoodsController.goods(..))" +
            "&& !execution(* muyu.trade.controller.GoodsController.goodsItem(..))")
    public void loginCheck() {}

    @Around("loginCheck()")
    public Object loginCheckAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        if(session.getAttribute("user") == null)
            return new Response<Object>(401, "未登录", null);
        else
            return (Object) proceedingJoinPoint.proceed();
    }

    @Pointcut("execution(* muyu.trade.controller.AdminController.*(..))")
    public void adminCheck(){};

    @Around("adminCheck()")
    public Object adminCheckAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getParameter("token");
        if(token == null || !token.equals("muyuchengfeng"))
            return null;
        else
            return (Object) proceedingJoinPoint.proceed();
    }
}
