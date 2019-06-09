package com.huawei.user.service.impl;/**
 * @Auther: 25349
 * @Date: 2019/6/8 22:16
 * @Description:
 */

import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.common.utils.NumberUtils;
import com.huawei.user.config.VerifyCodeProperties;
import com.huawei.user.mapper.UserMapper;
import com.huawei.user.pojo.User;
import com.huawei.user.service.UserService;
import com.huawei.user.utils.CodecUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/8 22:16
 * @Description:用户中心实现层
 */
@Slf4j
@Service
@EnableConfigurationProperties(VerifyCodeProperties.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private VerifyCodeProperties prop;
    //验证码前缀
    private static final String KEY_PREFIX = "user:verifycode:";

    /**
     * 校验用户数据的唯一性
     * @param data
     * @param type
     * @return
     */
    public Boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
            throw  new SelfException(ExceptionEnums.INVALID_USER_DATA_TYPE);
        }
        return this.userMapper.selectCount(record) == 0;
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return 暂时返回验证码到前台，因为发送短信没有调通
     */
    public String sendVerifyCode(String phone) {
      String key = KEY_PREFIX  + phone;
      //随机生成6位数验证码
      String code = NumberUtils.generateCode(6);
      //发送短信验证码
        Map<String,String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        amqpTemplate.convertAndSend(prop.getExchange(),prop.getRoutingkey(),msg);
        //保存验证码
        redisTemplate.opsForValue().set(key,code,Long.valueOf(prop.getTimeout()), TimeUnit.MINUTES);
        return "验证码为：" +code;
    }

    /**
     * 用户注册
     * @param user
     * @param code
     * @return
     */
    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        // 从redis取出验证码
        String codeCache = this.redisTemplate.opsForValue().get(key);
        // 检查验证码是否正确
        if (!StringUtils.equals(code,codeCache)){
           throw new SelfException(ExceptionEnums.INVALID_VERIFY_CODE);
        }
        user.setCreated(new Date());
        // 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        // 对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        // 写入数据库
        boolean boo = userMapper.insertSelective(user) == 1;

        // 如果注册成功，删除redis中的code
        if (boo) {
            try {
                redisTemplate.delete(key);
            } catch (Exception e) {
                log.error("删除缓存验证码失败，code：{}", code, e);
            }
        }
        return boo;
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    public User queryUserByUserNameAndPassWord(String username, String password) {
        //查询
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        //检验用户名是否存在
        if(user == null){
            throw new SelfException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
        }
        //检验密码是否正确
        if (!StringUtils.equals(user.getPassword(), CodecUtils.md5Hex(password,user.getSalt()))) {
            throw new SelfException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
        }
        return user;
    }
}
