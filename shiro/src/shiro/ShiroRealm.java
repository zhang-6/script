package shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

public class ShiroRealm extends AuthenticatingRealm {
    //验证框架
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//        System.out.println(token.hashCode());//测试token是否是同一个值
//        System.out.println("我是ShiroRealm"+token);
        System.out.println("我是第一个realm");
        //将AuthenticationToken转换为UsernamePasswordToken
        System.out.println("我是第一个realm");
        UsernamePasswordToken tok = (UsernamePasswordToken)token;
        //获取用户名
        String username = tok.getUsername();
        //密码
        Object credentials = null;
        if("wm".equals(username)){
            throw new UnknownAccountException("用户名不存在");
        }
        if("cl".equals(username)){
            throw new LockedAccountException("用户名被锁定");
        }
        if("zhangsan".equals(username)){
            credentials = "271dad09d1a71f27b7aeaa27306d5e24";
        }else if("lisi".equals(username)){
            credentials = "0c1b64535abaa1e871009019c6bcde0e";
        }
        //用户名
        Object principal = username;
        String realmName = getName();
        //获取盐值  盐值→加密
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        SimpleAuthenticationInfo info = null;//new SimpleAuthenticationInfo(principal, credentials, realmName);
        //credentials指代盐值   realmName指代使用的框架
        info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
        return info;
    }
    public static void main(String[] args) {//MD5加密
        String hashAlgorithmName = "MD5";
        Object credentials = "123";
        Object salt = ByteSource.Util.bytes("lisi");
        int hashIterations = 1024;
        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(result);
    }
}
