package shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Simple Quickstart application showing how to use Shiro's API.
 *
 * @since 0.9 RC2
 */
public class Quickstart {
	//以后的打印测试只能使用Logger对象  可以当做是固定公式 →用于jar包log4j
    private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);


    public static void main(String[] args) {
    	//log.info(arg0); //System.out.println();
        // The easiest way to create a Shiro SecurityManager with configured
        // realms, users, roles and permissions is to use the simple INI config.
        // We'll do that by using a factory that can ingest a .ini file and
        // return a SecurityManager instance:

        // Use the shiro.ini file at the root of the classpath
        // (file: and url: prefixes load from files and urls respectively):
    	//解析shiro.ini文件并且获取SecurityManager对象
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //通过Factory生产出securityManager
        SecurityManager securityManager = factory.getInstance();

        // for this simple example quickstart, make the SecurityManager
        // accessible as a JVM singleton.  Most applications wouldn't do this
        // and instead rely on their container configuration or web.xml for
        // webapps.  That is outside the scope of this simple quickstart, so
        // we'll just do the bare minimum so you can continue to get a feel
        // for things.
        //将获取的SecurityManager对象赋值给shiro框架中SecurityManager   SecurityUtils→安全工具类
        SecurityUtils.setSecurityManager(securityManager);

        // Now that a simple Shiro environment is set up, let's see what you can do:

        // get the currently executing user:
        //获取Subject对象
        Subject currentUser = SecurityUtils.getSubject();

        // Do some stuff with a Session (no need for a web or EJB container!!!)
        //通过Subject获取session会话并且测试session是否可用
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("-----------------Retrieved the correct value! [" + value + "]");
        }

        // let's login the current user so we can check against roles and permissions:
        //测试当前的用户是否认证，是否登录
        if (!currentUser.isAuthenticated()) {
        	//前台传入的对象必须封装为一个token对象，这样才能在realm验证框架中使用
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            //记住操作
            token.setRememberMe(true);
            try {
            	//实现登录
                currentUser.login(token);
                //用户名不对
            } catch (UnknownAccountException uae) {
                log.info("---------------There is no user with username of " + token.getPrincipal());
               //密码不匹配
            } catch (IncorrectCredentialsException ice) {
                log.info("---------------Password for account " + token.getPrincipal() + " was incorrect!");
               //用户被锁定
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            //是上面异常的父类
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            }
        }

        //say who they are:
        //print their identifying principal (in this case, a username):
        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role:
        //测试用户是否存在角色schwartz
        if (currentUser.hasRole("schwartz")) {
            log.info("-----------------May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }

        //test a typed permission (not instance-level)
        //测试用户是否具有操作lightsaber的任何权限
        if (currentUser.isPermitted("lightsaber:weild1231231231313")) {
            log.info("------------------You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        //a (very powerful) Instance Level permission:
        //同上，只是更加具体   测试用户是否有具有操作user类型的zhangsan对象的delete操作
        if (currentUser.isPermitted("user:delete:zhangsan")) {//与shiro.ini相对应
            log.info("-------------------------You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        //all done - log out!
        //currentUser.isAuthenticated()检查是否退出
        System.out.println(currentUser.isAuthenticated());
        currentUser.logout();
        System.out.println(currentUser.isAuthenticated());
        System.exit(0);
    }
}
