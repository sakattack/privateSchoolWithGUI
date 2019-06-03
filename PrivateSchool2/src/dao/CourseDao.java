package dao;

import controller.DBAccess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Persistable;

/**
 *
 * @author Sakel
 */
public class CourseDao implements DaoInterface {
    
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
     * @return A ResultSet of course per line, with column order : title, stream, type, id
     * @throws SQLException
     */
    @Override
    public ResultSet getAll() throws SQLException {
        
            STMT = DBAccess.singleton().getCon().createStatement();
            RS = STMT.executeQuery("SELECT title, stream, type, id FROM courses");
            return RS;
        
    }
    
    /**
     * 
     * @param userId The trainer id
     * @return A ResultSet of course per line of trainer with userId, with column order : title, stream, type, id
     * @throws SQLException
     * @throws Exception 
     */
    public ResultSet getAllPerTrainer(int userId) throws SQLException, Exception {
        
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("SELECT c.title, "
             + "c.stream, c.type, c.id FROM ((courses AS c INNER JOIN"
             + " courses_trainers AS ct ON c.id = ct.courseid)"
             + " INNER JOIN trainers AS t ON ct.trainerid = t.userid)"
             + " WHERE t.userid = ?");
         PRESTMT.setInt(1, userId);
         RS = PRESTMT.executeQuery();

         return RS; 
        
    }
    
    /**
     * 
     * @param userId The trainer id
     * @return A ResultSet of course per line of trainer with userId, with column order : title, stream, type, id
     * @throws SQLException
     * @throws Exception 
     */
    public ResultSet getAllPerStudent(int userId) throws SQLException, Exception {
        
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("SELECT c.title, "
             + "c.stream, c.type, c.id FROM ((courses AS c INNER JOIN"
             + " courses_students AS cs ON c.id = cs.courseid)"
             + " INNER JOIN students AS s ON cs.studentid = s.userid)"
             + " WHERE s.userid = ?");
         PRESTMT.setInt(1, userId);
         RS = PRESTMT.executeQuery();

         return RS; 
        
    }

    @Override
    public boolean insertOne(Persistable o) throws Exception {//it should be returning the id of the newly created course
        
        model.Course course = (model.Course)o;
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("INSERT INTO courses (`title`,`stream`,`type`) VALUES (?,?,?)");
        PRESTMT.setString(1, course.getTitle());
        PRESTMT.setString(2, course.getStream());
        PRESTMT.setString(3, course.getType());
        
        return PRESTMT.executeUpdate() > 0;
        
    }

    @Override
    public boolean deleteOne(int i) throws Exception {
        
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("DELETE FROM courses WHERE id = ?");
        PRESTMT.setInt(1, i);
        
        return PRESTMT.executeUpdate() > 0;
        
    }
    
    @Override
    public boolean updateOne(Persistable o) throws Exception {
        
        model.Course course = (model.Course)o;
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("UPDATE courses SET title = ?, stream = ?, type = ? WHERE id = ?");
        PRESTMT.setString(1, course.getTitle());
        PRESTMT.setString(2, course.getStream());
        PRESTMT.setString(3, course.getType());
        PRESTMT.setInt(4, course.getId());
        
        return PRESTMT.executeUpdate() > 0;
        
    }
    
    public void updateCascade(StudentDao dao, int courseId, ArrayList<Integer> ids) throws Exception {
    
        dao.deleteAllPerCourse(courseId);
        
        for( Integer i : ids ){
        
            dao.insertOnePerCourse(i, courseId);
        
        }
    
    }
    
    public void updateCascade(TrainerDao dao, int courseId, ArrayList<Integer> ids) throws Exception {
    
        dao.deleteAllPerCourse(courseId);
        
        for( Integer i : ids ){
        
            dao.insertOnePerCourse(i, courseId);
        
        }
    
    }
    
    public void updateCascade(ScheduleDao dao, int courseId, ArrayList<model.Schedule> scheduleEntries) throws Exception {
    
        dao.deleteAllPerCourse(courseId);
        
        for( model.Schedule sch : scheduleEntries ){
        
            dao.insertOne(sch);
        
        }
    
    }
    

}
