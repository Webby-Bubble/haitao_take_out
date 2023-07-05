package org.example.haitao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
需求分析
代码开发
功能测试
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching//开启spring chche注解方式的缓存功能
public class HaitaoApplication {
    public static void main(String[] args) {
        SpringApplication.run(HaitaoApplication.class,args);
        log.info("项目启动成功...");
    }
}
