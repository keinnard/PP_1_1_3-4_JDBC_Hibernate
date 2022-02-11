package jm.task.core.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import jm.task.core.jdbc.model.User;

import java.util.List;
import jm.task.core.jdbc.model.UserOperationException;
import jm.task.core.jdbc.util.Util;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection conn;

    public UserDaoJDBCImpl() {
        Connection tC = null;
        try {
            tC = Util.getLocalConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new UserOperationException("Init", ex);
        } finally {
            conn = tC;
        }

    }

    //private PreparedStatement checkIfExistsPS, createUTablePS, dropUTablePS, saveUPS, removeUByIdPS, getAllUsPS, cleanUTablePS;
    @Override
    public void createUsersTable() {
        if (!checkIfTableExists()) {
            try ( Statement s = conn.createStatement()) {

                s.execute("create table User (\n"
                        + "id bigint auto_increment primary key,\n"
                        + "name varchar(128),\n"
                        + "lastName varchar(128),\n"
                        + "age tinyint\n"
                        + ");");
                commit();

            } catch (SQLException ex) {
                try {
                    rollback();
                } catch (SQLException ex1) {
                    ex1.printStackTrace();
                    throw new UserOperationException("Rollback", ex1);
                }
                ex.printStackTrace();
                throw new UserOperationException("Create Table", ex);
            }
        }
    }

    @Override
    public void dropUsersTable() {

        if (checkIfTableExists()) {
            try ( Statement s = conn.createStatement()) {
                s.execute("DROP table User;");
                commit();
            } catch (SQLException ex) {
                try {
                    rollback();
                } catch (SQLException ex1) {
                    ex1.printStackTrace();
                    throw new UserOperationException("Rollback", ex1);
                }
                ex.printStackTrace();
                throw new UserOperationException("Drop Table", ex);
            }
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try ( PreparedStatement ps = getSaveUPS()) {

            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.executeUpdate();
            commit();

        } catch (SQLException ex) {
            try {
                rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace();
                throw new UserOperationException("Rollback", ex1);
            }
            ex.printStackTrace();
            throw new UserOperationException("Save", ex);
        }

    }

    @Override
    public void removeUserById(long id) {
        try ( PreparedStatement ps = getRemoveUByIdPS()) {

            ps.setLong(1, id);
            ps.executeUpdate();
            commit();

        } catch (SQLException ex) {
            try {
                rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace();
                throw new UserOperationException("Rollback", ex1);
            }
            ex.printStackTrace();
            throw new UserOperationException("Remove", ex);
        }

    }

    @Override
    public List<User> getAllUsers() {
        ArrayList<User> aUs = new ArrayList<>();
        try ( PreparedStatement ps = getGetAllUsPS()) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                aUs.add(new User(rs.getString("name"), rs.getString("lastName"), rs.getByte("age")));
            }
            rs.close();
            commit();
        } catch (SQLException ex) {
            try {
                rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace();
                throw new UserOperationException("Rollback", ex1);
            }
            ex.printStackTrace();
            throw new UserOperationException("get All", ex);
        }
        return aUs;
    }

    @Override
    public void cleanUsersTable() {
        try ( Statement s = conn.createStatement()) {
            s.execute("TRUNCATE User");
            commit();
        } catch (SQLException ex) {
            try {
                rollback();
            } catch (SQLException ex1) {
                ex.printStackTrace();
                throw new UserOperationException("Rollback", ex1);
            }
            ex.printStackTrace();
            throw new UserOperationException("Clean Table", ex);
        }
    }

    /**
     * @return the saveUPS
     */
    private PreparedStatement getSaveUPS() throws SQLException {
        return conn.prepareStatement("INSERT INTO User (name, lastName, age) values (?,?,?)");
    }

    /**
     * @return the removeUByIdPS
     */
    private PreparedStatement getRemoveUByIdPS() throws SQLException {
        return conn.prepareStatement("DELETE from User WHERE id = ?");
    }

    /**
     * @return the getAllUsPS
     */
    private PreparedStatement getGetAllUsPS() throws SQLException {
        return conn.prepareStatement("SELECT id, name, lastName, age FROM User");
    }

    /**
     * @return the checkIfExists
     */
    private PreparedStatement getCheckIfTableExistsPS() throws SQLException {

        return conn.prepareStatement("SELECT table_name, table_schema\n"
                + "FROM information_schema.tables\n"
                + "WHERE table_schema = 'Kata'\n"
                + "AND table_name = 'User';");

    }

    private boolean checkIfTableExists() {
        boolean ret = false;
        try ( PreparedStatement ps = getCheckIfTableExistsPS()) {

            ResultSet rs = ps.executeQuery();
            ret = rs.next();
            rs.close();
            commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace();
                throw new UserOperationException("Rollback", ex1);
            }
            throw new UserOperationException("Check Table", ex);
        }
        return ret;
    }

    private void commit() throws SQLException {
        conn.commit();

    }

    private void rollback() throws SQLException {
        conn.rollback();
    }
}
