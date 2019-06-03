package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import model.Persistable;

/**
 *
 * @author Sakel
 */
public interface DaoInterface {
    
    public PreparedStatement getPRESTMT();
    public Statement getSTMT();
    public ResultSet getRS();
    
    public ResultSet getAll() throws Exception;
    
    public boolean insertOne(Persistable o) throws Exception;
    
    public boolean deleteOne(int i) throws Exception;
    
    public boolean updateOne(Persistable o) throws Exception;

}
