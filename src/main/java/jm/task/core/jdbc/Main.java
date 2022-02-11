package jm.task.core.jdbc;

import java.util.ArrayList;
import java.util.stream.Stream;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {

    public static void main(String[] args) {
        UserService us = new UserServiceImpl();

        us.createUsersTable();

        Stream.of(
                new User("Василий", "Говорков", (byte) 32),
                new User("Joanne", "Ark", (byte) 120),
                new User("Николай", "Smith", (byte) 54),
                new User("Jose", "Rivera", (byte) 8)
        ).forEach(u -> {
            us.saveUser(u.getName(), u.getLastName(), u.getAge());
            System.out.println(String.format("User с именем – %s добавлен в базу данных", u.getName()));
        });
        
        us.removeUserById(2);
        us.getAllUsers().stream().forEach(u -> System.out.println(u));
        us.cleanUsersTable();
        us.dropUsersTable();

    }
}
