package com.example.demo.netty_user.mapper;

import com.example.demo.netty_user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表 t_user 的 MyBatis Mapper（原生写法，SQL 放 XML）。
 */
@Mapper
public interface UserMapper {

    /** 按用户名查询 */
    User selectByUsername(@Param("username") String username);

    /** 按 token 查询 */
    User selectByToken(@Param("token") String token);

    /** 插入新用户，回填自增主键 */
    int insert(User user);

    /** 更新指定用户的 token（登录刷新） */
    int updateToken(@Param("username") String username, @Param("token") String token);

    /** 用户总数 */
    int countAll();
}
