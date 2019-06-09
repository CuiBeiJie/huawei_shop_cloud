package com.huawei.user.controller;/**
 * @Auther: 25349
 * @Date: 2019/6/8 22:27
 * @Description:
 */

import com.huawei.common.exception.SelfException;
import com.huawei.user.pojo.User;
import com.huawei.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/8 22:27
 * @Description:用户中心控制层
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable(value = "type") Integer type) {
        Boolean boo = userService.checkData(data, type);
        if (boo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(boo);
    }
    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<String> sendVerifyCode(@RequestParam("phone") String phone) {
        return ResponseEntity.ok(userService.sendVerifyCode(phone));
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, BindingResult result, @RequestParam("code") String code) {
        //参数后面加上BindingResult，异常可以自定义处理，这样spring就不会帮我们抛出了，
        // 如果想让spring自己抛出把BindingResult去掉就行了
        if(result.hasFieldErrors()){
           throw new RuntimeException(result.getFieldErrors().stream().
                   map(e -> e.getDefaultMessage()).collect(Collectors.joining("|")));
        }
        Boolean boo = userService.register(user, code);
        if (boo == null || !boo) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUserByUserNameAndPassWord(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        return ResponseEntity.ok(userService.queryUserByUserNameAndPassWord(username, password));
    }
}
