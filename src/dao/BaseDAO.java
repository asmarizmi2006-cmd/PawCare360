package dao;

import exception.DatabaseException;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Shared JDBC helpers
public abstract class BaseDAO
{
    // Row to object
    @FunctionalInterface
    protected interface RowMapper<T>
    {
        T map(ResultSet rs) throws SQLException;
    }

    // Insert or update
    protected int executeUpdate(String sql, String action, Object... params)
    {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            bind(ps, params);
            return ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw DatabaseException.wrap(e, action);
        }
    }

    // Insert returning key
    protected int insertReturningId(String sql, String action, Object... params)
    {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            bind(ps, params);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys())
            {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
        catch (SQLException e)
        {
            throw DatabaseException.wrap(e, action);
        }
    }

    // Select many
    protected <T> List<T> queryList(String sql, String action, RowMapper<T> mapper, Object... params)
    {
        List<T> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    list.add(mapper.map(rs));
                }
            }
            return list;
        }
        catch (SQLException e)
        {
            throw DatabaseException.wrap(e, action);
        }
    }

    // Select one
    protected <T> Optional<T> queryOne(String sql, String action, RowMapper<T> mapper, Object... params)
    {
        List<T> list = queryList(sql, action, mapper, params);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    // Work inside a transaction
    @FunctionalInterface
    protected interface TxWork<T>
    {
        T run(Connection con) throws SQLException;
    }

    // Commit or rollback
    protected <T> T inTransaction(String action, TxWork<T> work)
    {
        try (Connection con = DBConnection.getConnection())
        {
            con.setAutoCommit(false);
            try
            {
                T result = work.run(con);
                con.commit();
                return result;
            }
            catch (SQLException | RuntimeException e)
            {
                con.rollback();
                throw e;
            }
            finally
            {
                con.setAutoCommit(true);
            }
        }
        catch (SQLException e)
        {
            throw DatabaseException.wrap(e, action);
        }
    }

    // Run inside a transaction
    protected static int update(Connection con, String sql, Object... params) throws SQLException
    {
        try (PreparedStatement ps = con.prepareStatement(sql))
        {
            bind(ps, params);
            return ps.executeUpdate();
        }
    }

    // Bind parameters
    protected static void bind(PreparedStatement ps, Object... params) throws SQLException
    {
        for (int i = 0; i < params.length; i++)
        {
            ps.setObject(i + 1, params[i]);
        }
    }
}
