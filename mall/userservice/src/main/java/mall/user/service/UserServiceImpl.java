package mall.user.service;

import bean.Member;
import bean.MemberReceiveAddress;
import com.alibaba.fastjson.JSON;
import mall.user.mapper.UserAddressMapper;
import mall.user.mapper.UserMapper;
import mall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserAddressMapper userAddressMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public Member login(Member member) {
        Member memberFromDb=null;
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            if (jedis!=null){
                //有缓存
                String memberStr=jedis.get("user:"+member.getPassword()+"info");
                if(StringUtils.isNotBlank(memberStr)){
                    //密码正确
                    Member memberFromCache= JSON.parseObject(memberStr,Member.class);
                    return memberFromCache;
                }
            }
            //缓存中没有，从数据库查
            memberFromDb=userMapper.selectOne(member);
            if(memberFromDb!=null){
                jedis.setex("user:"+member.getPassword()+"info",60*60*2, JSON.toJSONString(memberFromDb));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            jedis.close();
        }
        return memberFromDb;
    }

    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis=redisUtil.getJedis();
        jedis.setex("user:"+memberId+":token",60*60*2,token);
        jedis.close();
    }

    @Override
    //检查社交用户是否存在
    public Member checkMember(Member check) {
        Member member=userMapper.selectOne(check);
        return member;
    }

    @Override
    //添加社交用户
    public void addUser(Member member) {
        userMapper.insertSelective(member);
    }

    @Override
    public List<MemberReceiveAddress> getReceiveAddress(String memberId) {
        MemberReceiveAddress memberReceiveAddress=new MemberReceiveAddress();
        memberReceiveAddress.setMemberId(memberId);
        List<MemberReceiveAddress> memberReceiveAddressList=userAddressMapper.select(memberReceiveAddress);
        return memberReceiveAddressList;
    }

    @Override
    public MemberReceiveAddress getReceiveAddressById(String receiveAddressId) {
        MemberReceiveAddress memberReceiveAddress=new MemberReceiveAddress();
        memberReceiveAddress.setId(receiveAddressId);
        MemberReceiveAddress memberReceiveAddress1=userAddressMapper.selectOne(memberReceiveAddress);
        return memberReceiveAddress1;
    }
}
