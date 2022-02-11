package jm.task.core.jdbc.service;

import jm.task.core.jdbc.model.User;

import java.util.List;
import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;

public class UserServiceImpl implements UserService {

    private final UserDao ud = new UserDaoJDBCImpl();

    @Override
    public void createUsersTable() {
        ud.createUsersTable();

    }

    @Override
    public void dropUsersTable() {
        ud.dropUsersTable();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        ud.saveUser(name, lastName, age);
    }

    @Override
    public void removeUserById(long id) {
        ud.removeUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return ud.getAllUsers();
    }

    @Override
    public void cleanUsersTable() {
        ud.cleanUsersTable();
    }
}
