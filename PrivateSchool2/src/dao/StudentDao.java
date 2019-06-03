package dao;

import controller.DBAccess;
import java.sql.Date;
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
public class StudentDao implements DaoInterface {
    
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
     * @return A ResultSet of student per line, with column order : type, username, password, key, id, firstname, lastname, birthdate, tuitionfees
     * @throws SQLException
     * @throws Exception 
     */
    @Override
    public ResultSet getAll() throws SQLException, Exception {
        
        if ( Utils.UserType.valueOf(DBAccess.singleton().getUser().getType().toUpperCase()) == Utils.UserType.HEADMASTER ) {
            STMT = DBAccess.singleton().getCon().createStatement();
            RS = STMT.executeQuery("SELECT u.`type`, u.username, "
                    + "u.`password`, u.`key`, u.id, s.firstname, "
                    + "s.lastname, s.birthdate, s.tuitionfees FROM "
                    + "(students AS s INNER JOIN users AS u ON s.userid = u.id )");
            return RS;
        }
        else {
            throw new Exception(Utils.UserType.HEADMASTER + " only");
        }
        
    }
    
    public ResultSet getAllPerCourse(int courseId) throws SQLException, Exception {
    
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("SELECT u.`type`, u.username, "
                    + "u.`password`, u.`key`, u.id, s.firstname, "
                    + "s.lastname, s.birthdate, s.tuitionfees FROM "
                    + "(((students AS s INNER JOIN users AS u ON s.userid = u.id ) INNER JOIN courses_students AS cs ON s.userid = cs.studentid) INNER JOIN courses AS c ON c.id = cs.courseid) WHERE c.id = ?");
        PRESTMT.setInt(1, courseId);
        RS = PRESTMT.executeQuery();
        
        return RS;
    
    }
    
    @Override
    public boolean insertOne(Persistable o) throws Exception {
        
        int generatedKey = 0, first = 0, second = 0;
        
        try {
            
            model.Student std = (model.Student)o;

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
                
                PRESTMT = DBAccess.singleton().getCon().prepareStatement("INSERT INTO students (`userid`,`firstname`,`lastname`,`birthdate`,`tuitionfees`) VALUES (?,?,?,?,?)");
                PRESTMT.setInt(1, generatedKey);
                PRESTMT.setString(2, std.getFirstName());
                PRESTMT.setString(3, std.getLastName());
                PRESTMT.setDate(4, Date.valueOf(std.getBirthDate()));
                PRESTMT.setFloat(5, std.getTuitionFees());

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
    
    public boolean insertOnePerCourse(int studentid, int courseid) throws Exception {
    
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("INSERT INTO courses_students (`studentid`,`courseid`) VALUES (?,?)");
        PRESTMT.setInt(1, studentid);
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
        
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("DELETE FROM courses_students WHERE courseid = ?");
        PRESTMT.setInt(1, courseid);
        
        return PRESTMT.executeUpdate() > 0;
        
    }

    @Override
    public boolean updateOne(Persistable o) throws Exception {
        
        int first = 0, second = 0;
        
        try {
            
            model.Student std = (model.Student)o;

            DBAccess.singleton().getCon().setAutoCommit(false);
            PRESTMT = DBAccess.singleton().getCon().prepareStatement("UPDATE users SET username = ?, password = ? WHERE id = ?");
            PRESTMT.setString(1, std.getUsername());
            PRESTMT.setBytes(2, std.getPassword());
            PRESTMT.setInt(3, std.getId());

            first = PRESTMT.executeUpdate();

            if ( first > 0 ) {
                
                PRESTMT = DBAccess.singleton().getCon().prepareStatement("UPDATE students SET firstname = ?, lastname = ?, birthdate = ?, tuitionfees = ? WHERE userid = ?");
                PRESTMT.setString(1, std.getFirstName());
                PRESTMT.setString(2, std.getLastName());
                PRESTMT.setDate(3, Date.valueOf(std.getBirthDate()));
                PRESTMT.setFloat(4, std.getTuitionFees());
                PRESTMT.setInt(5, std.getId());

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
