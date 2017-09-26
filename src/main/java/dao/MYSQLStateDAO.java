package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.State;
import util.exceptions.DataSourceException;

public class MYSQLStateDAO implements StateDAO {

    public MYSQLStateDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<State> getAllStates() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM states ORDER BY state_name");

            ResultSet rs = pstmt.executeQuery();

            ArrayList<State> states = new ArrayList<State>();

            while (rs.next()) {
                State state = new State();

                state.setId(rs.getString("id"));
                state.setState_name(rs.getString("state_name"));
                
                states.add(state);
            }

            return states;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<State> getState(String state_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM states WHERE id=? ORDER BY state_name");

            pstmt.setString(1, state_id);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<State> states = new ArrayList<State>();

            while (rs.next()) {
                State state = new State();

                state.setId(rs.getString("id"));
                state.setState_name(rs.getString("state_name"));
                
                states.add(state);
            }

            return states;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }

    private final Connection db_conn;

}