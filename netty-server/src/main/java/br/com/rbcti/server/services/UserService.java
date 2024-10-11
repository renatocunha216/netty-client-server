package br.com.rbcti.server.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.rbcti.common.model.User;
import br.com.rbcti.server.dao.UserDAO;

// @Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserDAO userDAO;

    // @Transactional
    public List<User> selectUsers() throws Exception {
        LOGGER.trace("Selecting users");
        List<User> users = userDAO.selectUsers();
        return users;
    }

    public User selectUser(String name) throws Exception {
        LOGGER.trace("Selecting user by name");
        User user = userDAO.selectUserByName(name);
        return user;
    }

    public User selectUser(Long id) throws Exception {
        LOGGER.trace("Selecting user by name");
        User user = userDAO.selectUserById(id);
        return user;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
