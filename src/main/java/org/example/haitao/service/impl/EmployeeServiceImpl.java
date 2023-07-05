package org.example.haitao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.haitao.entity.Employee;
import org.example.haitao.mapper.EmployeeMapper;
import org.example.haitao.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
