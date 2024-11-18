package com.example.board.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

@Configuration
@MapperScan("com.example.board.mapper")
public class MyBatisConfig {
        // SqlSessionFactory를 생성하는 Bean 설정
        @Bean
        public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
            // SqlSessionFactoryBean을 사용하여 SqlSessionFactory를 생성
            SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
            // 데이터소스를 SqlSessionFactoryBean에 설정
            sessionFactoryBean.setDataSource(dataSource);
            // SqlSessionFactory 반환
            return sessionFactoryBean.getObject();
        }

        // SqlSessionTemplate을 생성하는 Bean 설정
        @Bean
        public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
            // SqlSessionTemplate을 사용하여 SqlSessionFactory와 함께 작업
            return new SqlSessionTemplate(sqlSessionFactory);
        }

        // 트랜잭션 관리자를 설정하는 Bean 설정
        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
}
