package org.fbi.hmfsjz.repository;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Mybatis DB Factory
 */
public enum MybatisFactory {
    ORACLE;
    private SqlSessionFactory sessionFactory;

    MybatisFactory(){
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("MYBATIS �����ļ���ȡ����",e);
        }
        sessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }
    public SqlSessionFactory getInstance(){
        return  sessionFactory;
    }
}
