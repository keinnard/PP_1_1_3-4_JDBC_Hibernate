package jm.task.core.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jm.task.core.jdbc.model.User;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection conn;

    public UserDaoJDBCImpl() {
        Connection tC = null;
        try {
            tC = Util.getLocalConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conn = tC;
        }

    }

    private PreparedStatement checkIfExistsPS, createUTablePS, dropUTablePS, saveUPS, removeUByIdPS, getAllUsPS, cleanUTablePS;

    @Override
    public void createUsersTable() {
        try {
            if (!checkIfTableExists()) {
                getCreateUTablePS().execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {

        try {
            if (checkIfTableExists()) {
                getDropUTablePS().execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            getSaveUPS().clearParameters();
            getSaveUPS().setString(1, name);
            getSaveUPS().setString(2, lastName);
            getSaveUPS().setByte(3, age);
            getSaveUPS().executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            getRemoveUByIdPS().clearParameters();
            getRemoveUByIdPS().setLong(1, id);
            getRemoveUByIdPS().executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public List<User> getAllUsers() {
        ArrayList<User> aUs = new ArrayList<>();
        try {
            ResultSet rs = getGetAllUsPS().executeQuery();

            while (rs.next()) {
                aUs.add(new User(rs.getString("name"), rs.getString("lastName"), rs.getByte("age")));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return aUs;
    }

    @Override
    public void cleanUsersTable() {
        try {
            getCleanUTablePS().executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the createUTablePS
     */
    private PreparedStatement getCreateUTablePS() throws SQLException {
        if (createUTablePS == null) {
            createUTablePS = conn.prepareStatement("create table User (\n"
                    + "id bigint auto_increment primary key,\n"
                    + "name varchar(128),\n"
                    + "lastName varchar(128),\n"
                    + "age tinyint\n"
                    + ");");

        }
        return createUTablePS;
    }

    /**
     * @return the dropUTablePS
     */
    private PreparedStatement getDropUTablePS() throws SQLException {
        if (dropUTablePS == null) {
            dropUTablePS = conn.prepareStatement("DROP table User");
        }
        return dropUTablePS;
    }

    /**
     * @return the saveUPS
     */
    private PreparedStatement getSaveUPS() throws SQLException {
        if (saveUPS == null) {
            saveUPS = conn.prepareStatement("INSERT INTO User (name, lastName, age) values (?,?,?)");
        }
        return saveUPS;
    }

    /**
     * @return the removeUByIdPS
     */
    private PreparedStatement getRemoveUByIdPS() throws SQLException {
        if (removeUByIdPS == null) {
            removeUByIdPS = conn.prepareStatement("DELETE from User WHERE id = ?");
        }
        return removeUByIdPS;
    }

    /**
     * @return the getAllUsPS
     */
    private PreparedStatement getGetAllUsPS() throws SQLException {
        if (getAllUsPS == null) {
            getAllUsPS = conn.prepareStatement("SELECT id, name, lastName, age FROM User");
        }
        return getAllUsPS;
    }

    /**
     * @return the cleanUTablePS
     */
    private PreparedStatement getCleanUTablePS() throws SQLException {
        if (cleanUTablePS == null) {
            cleanUTablePS = conn.prepareStatement("TRUNCATE User");
        }
        return cleanUTablePS;
    }

    /**
     * @return the checkIfExists
     */
    private PreparedStatement getCheckIfTableExistsPS() throws SQLException {

        if (checkIfExistsPS == null) {

            checkIfExistsPS = conn.prepareStatement("SELECT table_name, table_schema\n"
                    + "FROM information_schema.tables\n"
                    + "WHERE table_schema = 'Kata'\n"
                    + "AND table_name = 'User';");

        }
        return checkIfExistsPS;
    }

    private boolean checkIfTableExists() throws SQLException {
        ResultSet rs = getCheckIfTableExistsPS().executeQuery();
        return rs.next();
    }
}
