package org.example.haitao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.haitao.entity.User;
import org.example.haitao.mapper.UserMapper;
import org.example.haitao.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
