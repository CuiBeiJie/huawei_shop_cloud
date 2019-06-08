package com.huawei.user.controller;/**
 * @Auther: 25349
 * @Date: 2019/6/8 22:27
 * @Description:
 */

import com.huawei.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
