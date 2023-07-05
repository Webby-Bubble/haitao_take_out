package org.example.haitao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.haitao.entity.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
