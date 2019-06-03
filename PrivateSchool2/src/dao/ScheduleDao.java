package dao;

import controller.DBAccess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import model.Persistable;

/**
 *
 * @author Sakel
 */
public class ScheduleDao implements DaoInterface {
    
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
     * @return A ResultSet of schedule per line, with column order : courseid, start, end, id
     * @throws SQLException
     * @throws Exception 
     */
    @Override
    public ResultSet getAll() throws SQLException, Exception {
        
        throw new UnsupportedOperationException("Not supported yet.");
        
    }
    
    public ResultSet getAllPerCourse(int courseId) throws SQLException, Exception {
    
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("SELECT courseid, start, end, id FROM schedule WHERE courseid = ?");
        PRESTMT.setInt(1, courseId);
        RS = PRESTMT.executeQuery();

        return RS; 
    
    }
    
    public ResultSet getAllPerStudent(int userId) throws SQLException, Exception {
    
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("SELECT sc.courseid, sc.`start`, sc.`end`, sc.id, c.title, c.stream, c.type FROM (((`schedule` AS sc INNER JOIN `courses` AS c ON sc.courseid = c.id) INNER JOIN `courses_students` AS cs ON c.id = cs.courseid) INNER JOIN `students` AS st ON cs.studentid = st.userid) WHERE st.userid = ?");
        PRESTMT.setInt(1, userId);
        RS = PRESTMT.executeQuery();

        return RS; 
    
    }
    
    public ResultSet getAllPerStudentPerCourse(int userId, int courseId) throws SQLException, Exception {
    
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("SELECT sc.courseid, sc.`start`, sc.`end`, sc.id FROM (((`schedule` AS sc INNER JOIN `courses` AS c ON sc.courseid = c.id) INNER JOIN `courses_students` AS cs ON c.id = cs.courseid) INNER JOIN `students` AS st ON cs.studentid = st.userid) WHERE st.userid = ? AND c.id = ?");
        PRESTMT.setInt(1, userId);
         PRESTMT.setInt(2, courseId);
        RS = PRESTMT.executeQuery();

        return RS; 
    
    }
    
    @Override
    public boolean insertOne(Persistable o) throws Exception {
        
        model.Schedule sch = (model.Schedule)o;
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("INSERT INTO schedule (`courseid`,`start`,`end`) VALUES (?,?,?)");
        PRESTMT.setInt(1, sch.getCourseId());
        PRESTMT.setTimestamp(2,Timestamp.valueOf(sch.getStart()));
        PRESTMT.setTimestamp(3,Timestamp.valueOf(sch.getEnd()));
        
        return PRESTMT.executeUpdate() > 0;
        
    }
    
    @Override
    public boolean deleteOne(int i) throws Exception {
        
        // makes no sense to delete them one by one
        
        return false;
        
    }
    
    public boolean deleteAllPerCourse(int courseId) throws Exception {
        
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("DELETE FROM schedule WHERE courseid = ?");
        PRESTMT.setInt(1, courseId);
        
        return PRESTMT.executeUpdate() > 0;
        
    }
    
    @Override
    public boolean updateOne(Persistable o) throws Exception {
        
        //schedule never gets updates. only deletes and inserts
        return false;
        
    }
    

}
