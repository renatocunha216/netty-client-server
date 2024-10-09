package br.com.rbcti.server.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import br.com.rbcti.common.exceptions.DAOException;
import br.com.rbcti.common.model.User;

public class UserDAO extends SqlSessionDaoSupport {

    public List<User> selectUsers() throws DAOException {
        try {
            List<User> users = getSqlSession().selectList("User.selectUsers");
            return users;

        } catch (Exception e) {
            throw new DAOException("Error getting users. " + e.getMessage());
        }
    }

    public User selectUserById(Long id) throws DAOException {
        try {
            return getSqlSession().selectOne("User.selectById", id);

        } catch (Exception e) {
            throw new DAOException("Error getting user by id. " + e.getMessage());
        }
    }

    public User selectUserByName(String name) throws DAOException {
        try {
            return getSqlSession().selectOne("User.selectByName", name);

        } catch (Exception e) {
            throw new DAOException("Error getting user by name. " + e.getMessage());
        }
    }

}
