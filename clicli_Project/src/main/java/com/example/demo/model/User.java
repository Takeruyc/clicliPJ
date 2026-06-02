package com.example.demo.model;

import java.util.Date;

public class User {
    // 用户ID（主键）
    private String id;
    // 昵称
    private String nickname;
    // 邮箱
    private String email;
    // 密码
    private String password;
    // 角色（如：管理员、普通用户等）
    private String role;
    // 创建时间
    private Date createdAt;
    // 预留字段
    private String reserved1;
    private String reserved2;
    private String reserved3;
}