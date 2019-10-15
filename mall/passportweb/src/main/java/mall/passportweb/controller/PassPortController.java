package mall.passportweb.controller;

import bean.Member;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import mall.util.CookieUtil;
import mall.util.HttpclientUtil;
import mall.util.JwtUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassPortController {
    @Reference
    UserService userService;
    @RequestMapping("vlogin")
    public String vlogin(String code){
        //授权码换access_token
        String s = "https://api.weibo.com/oauth2/access_token?";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","222258035");
        paramMap.put("client_secret","b9063d276cf52fa4b8f989e9bc4ace58");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://127.0.0.1:8032/vlogin");
        paramMap.put("code",code);
        String access_token_json= HttpclientUtil.doPost(s,paramMap);
        Map<String,Object> access_map = JSON.parseObject(access_token_json, Map.class);
        //accsess_token换取用户信息
        String uid = (String)access_map.get("uid");
        String access_token = (String)access_map.get("access_token");
        String show_user_url = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
        String user_json = HttpclientUtil.doGet(show_user_url);
        Map<String,Object> user_map = JSON.parseObject(user_json,Map.class);
        //用户信息存入数据库
        Member member = new Member();
        member.setSourceType("2");
        member.setAccessCode(code);
        member.setAccessToken(access_token);
        member.setSourceUid((String)user_map.get("idstr"));
        member.setCity((String)user_map.get("location"));
        member.setNickname((String)user_map.get("screen_name"));
        String g = "0";
        String gender = (String)user_map.get("gender");
        if(gender.equals("m")){
            g = "1";
        }
        member.setGender(g);
        Member check=new Member();
        check.setSourceUid(member.getSourceUid());
        Member checkMember=userService.checkMember(check);
        if (checkMember==null){
            userService.addUser(member);
        }else{
            member=checkMember;
        }
        //生成jwt的token，并重定向到首页
        Map<String,Object> userMap=new HashMap<>();
        user_map.put("memberId",member.getId());
        user_map.put("nickName",member.getNickname());
        String token = JwtUtil.encode("mall", user_map, "127.0.0.1");
        userService.addUserToken(token,member.getId());
        return "redirect:http://localhost:8032/index?token="+token;
    }
    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token){
        //通过jwt校验token真假
        Map<String,String> map=new HashMap<>();
        Map<String, Object> decode = JwtUtil.decode(token, "mall", "127.0.0.1");
        if(decode!=null){
            map.put("status","success");
            map.put("memberId",(String) decode.get("memberId"));
            map.put("nickName",(String)decode.get("nickName"));
        }else {
            map.put("status","fail");
        }
        return JSON.toJSONString(map);
    }
    @RequestMapping("login")
    @ResponseBody
    public String login(Member member, HttpServletRequest request, HttpServletResponse response){
        String token="";
        Member memberLogin=userService.login(member);
        if(memberLogin!=null){
            //登入成功
            String memberId=memberLogin.getId();
            String nickName=memberLogin.getNickname();
            Map<String,Object> userMap=new HashMap<>();
            userMap.put("memberId",memberId);
            userMap.put("nickName",nickName);
            //生成token
            token= JwtUtil.encode("mall",userMap,"127.0.0.1");
            //将token存入缓存
            CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
        }else {
            //登入失败
            token="false";
        }
        return token;
    }
    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap modelMap){
        modelMap.put("ReturnUrl",ReturnUrl);
        return "index";
    }
}