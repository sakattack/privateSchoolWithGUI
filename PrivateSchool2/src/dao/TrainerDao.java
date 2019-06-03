package dao;

import controller.DBAccess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Persistable;
import model.Utils;

/**
 *
 * @author Sakel
 */
public class TrainerDao implements DaoInterface {
    
    private PreparedStatement PRESTMT = null;
    private Statement STMT = null;
    private ResultSet RS = null;
    
    @Override
    public PreparedStatement getPRESTMT() {
        return PRESTMT;
    }
    
    @Override
    public Statement getSTMT() {
        return STMT;
    }

    @Override
    public ResultSet getRS() {
        return RS;
    }

    /**
     * 
     * @return A ResultSet of trainer per line, with column order : type, username, password, key, id, firstname, lastname
     * @throws SQLException
     * @throws Exception 
     */
    @Override
    public ResultSet getAll() throws SQLException, Exception {
        
        if ( Utils.UserType.valueOf(DBAccess.singleton().getUser().getType().toUpperCase()) == Utils.UserType.HEADMASTER ) {
            STMT = DBAccess.singleton().getCon().createStatement();
            RS = STMT.executeQuery("SELECT u.`type`, u.username, "
                    + "u.`password`, u.`key`, u.id, t.firstname, "
                    + "t.lastname FROM (trainers AS t INNER JOIN users "
                    + "AS u ON t.userid = u.id )");
            return RS;
        }
        else {
            throw new Exception(Utils.UserType.HEADMASTER + " only");
        }
        
    }
    
    public ResultSet getAllPerCourse(int courseId) throws SQLException, Exception {
    
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("SELECT u.`type`, u.username, "
                    + "u.`password`, u.`key`, u.id, t.firstname, "
                    + "t.lastname FROM "
                    + "(((trainers AS t INNER JOIN users AS u ON t.userid = u.id ) INNER JOIN courses_trainers AS ct ON t.userid = ct.trainerid) INNER JOIN courses AS c ON c.id = ct.courseid) WHERE c.id = ?");
        PRESTMT.setInt(1, courseId);
        RS = PRESTMT.executeQuery();
        
        return RS;
    
    }
    
    @Override
    public boolean insertOne(Persistable o) throws Exception {
        
        int generatedKey = 0, first = 0, second = 0;
        
        try {
            
            model.Trainer std = (model.Trainer)o;

            DBAccess.singleton().getCon().setAutoCommit(false);
            PRESTMT = DBAccess.singleton().getCon().prepareStatement("INSERT INTO users (`type`,`username`,`password`,`key`) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            PRESTMT.setString(1, std.getType());
            PRESTMT.setString(2, std.getUsername());
            PRESTMT.setBytes(3, std.getPassword());
            PRESTMT.setBytes(4, std.getSaltKey());

            first = PRESTMT.executeUpdate();
            ResultSet rsUser = PRESTMT.getGeneratedKeys();
            if (rsUser != null && rsUser.next()) {
                generatedKey = rsUser.getInt(1);
                rsUser.close();
            }

            if ( generatedKey > 0 ) {
                
                PRESTMT = DBAccess.singleton().getCon().prepareStatement("INSERT INTO trainers (`userid`,`firstname`,`lastname`) VALUES (?,?,?)");
                PRESTMT.setInt(1, generatedKey);
                PRESTMT.setString(2, std.getFirstName());
                PRESTMT.setString(3, std.getLastName());

                second = PRESTMT.executeUpdate();

                DBAccess.singleton().getCon().commit();
            
            }
            else {
            
               try {
                   DBAccess.singleton().getCon().rollback();
               } catch(SQLException excep) {
                   throw excep;
               } 
            
            }
        
        } catch (SQLException e ) {
        
            try {
                DBAccess.singleton().getCon().rollback();
            } catch(SQLException excep) {
                throw excep;
            }
            
            throw e;
            
        }
        
        DBAccess.singleton().getCon().setAutoCommit(true);
        
        return (first + second) == 2;
        
    }
    
    public boolean insertOnePerCourse(int trainerid, int courseid) throws Exception {
    
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("INSERT INTO courses_trainers (`trainerid`,`courseid`) VALUES (?,?)");
        PRESTMT.setInt(1, trainerid);
        PRESTMT.setInt(2, courseid);
        
        return PRESTMT.executeUpdate() > 0;
    
    }
    
    @Override
    public boolean deleteOne(int i) throws Exception {
        
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("DELETE FROM users WHERE id = ?");
        PRESTMT.setInt(1, i);
        
        return PRESTMT.executeUpdate() > 0;
        
    }
    
    public boolean deleteAllPerCourse(int courseid) throws Exception {
        
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("DELETE FROM courses_trainers WHERE courseid = ?");
        PRESTMT.setInt(1, courseid);
        
        return PRESTMT.executeUpdate() > 0;
        
    }
    
    @Override
    public boolean updateOne(Persistable o) throws Exception {
        
        int first = 0, second = 0;
        
        try {
            
            model.Trainer std = (model.Trainer)o;

            DBAccess.singleton().getCon().setAutoCommit(false);
            PRESTMT = DBAccess.singleton().getCon().prepareStatement("UPDATE users SET username = ?, password = ? WHERE id = ?");
            PRESTMT.setString(1, std.getUsername());
            PRESTMT.setBytes(2, std.getPassword());
            PRESTMT.setInt(3, std.getId());

            first = PRESTMT.executeUpdate();

            if ( first > 0 ) {
                
                PRESTMT = DBAccess.singleton().getCon().prepareStatement("UPDATE trainers SET firstname = ?, lastname = ? WHERE userid = ?");
                PRESTMT.setString(1, std.getFirstName());
                PRESTMT.setString(2, std.getLastName());
                PRESTMT.setInt(3, std.getId());

                second = PRESTMT.executeUpdate();

                DBAccess.singleton().getCon().commit();
            
            }
            else {
            
               try {
                   DBAccess.singleton().getCon().rollback();
               } catch(SQLException excep) {
                   throw excep;
               } 
            
            }
        
        } catch (SQLException e ) {
        
            try {
                DBAccess.singleton().getCon().rollback();
            } catch(SQLException excep) {
                throw excep;
            }
            
            throw e;
            
        }
        
        DBAccess.singleton().getCon().setAutoCommit(true);
        
        return (first + second) == 2;
        
    }

}
