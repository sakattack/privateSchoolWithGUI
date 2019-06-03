package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Sakel
 */
public class DaoUtils {
    
    /**
     * Closes resources in the supplied order
     * 
     * @param resources Things like Statement, ResultSet. Order matters
     */
    public void close(AutoCloseable... resources) throws Exception{
        
        for( AutoCloseable r : resources ){
            
            if ( r != null ) r.close();
            
        }
        
    }
    
    /**
     * Transform ResultSet to {@link model.Course Course} objects
     * 
     * @param rs A ResultSet that needs to supply this info in the exact order : String title, String stream, String type, int id
     * @return ArrayList of type {@link model.Course model.Course}
     * @throws SQLException 
     */
    public LinkedHashMap<Integer, model.Course> coursify(ResultSet rs) throws SQLException, NullPointerException{
        
        LinkedHashMap<Integer, model.Course> result = new LinkedHashMap();
        
        while( rs.next() ){

            int id = rs.getInt(4);
            model.Course course = new model.Course(
                    rs.getString(1), 
                    rs.getString(2), 
                    rs.getString(3), 
                    id
            );
            result.put(id, course);

        }
        
        return result;
        
    }
    
    /**
     * Transform ResultSet to {@link model.Trainer Trainer} objects
     * 
     * @param rs A ResultSet that needs to supply this info in the exact order : 
     * String type, String username, byte[] password, byte[] saltKey, int id, String firstName, String lastName
     * 
     * @return ArrayList of type {@link model.Trainer model.Trainer}
     * @throws SQLException 
     */
    public LinkedHashMap<String, model.Trainer> trainerify(ResultSet rs) throws SQLException, NullPointerException{
    
        LinkedHashMap<String, model.Trainer> result = new LinkedHashMap();
        
        while( rs.next() ){
        
            String username = rs.getString(2);
            model.Trainer trainer = new model.Trainer(
                    rs.getString(1), 
                    username, 
                    rs.getBytes(3), 
                    rs.getBytes(4), 
                    rs.getInt(5), 
                    rs.getString(6), 
                    rs.getString(7)
            );
            result.put(username, trainer);
        }
        
        return result;
    
    }
    
    /**
     * Transform ResultSet to {@link model.Student Student} objects
     * 
     * @param rs A ResultSet that needs to supply this info in the exact order : 
     * String type, String username, byte[] password, byte[] saltKey, int id, String firstName, String lastName, LocalDate birthDate, float tuitionFees
     * 
     * @return ArrayList of type {@link model.Student model.Student}
     * @throws SQLException 
     */
    public LinkedHashMap<String, model.Student> studentify(ResultSet rs) throws SQLException, NullPointerException{
    
        LinkedHashMap<String, model.Student> result = new LinkedHashMap();
        
        while( rs.next() ){
        
            String username = rs.getString(2);
            model.Student student = new model.Student(
                    rs.getString(1), 
                    username, 
                    rs.getBytes(3), 
                    rs.getBytes(4), 
                    rs.getInt(5), 
                    rs.getString(6), 
                    rs.getString(7), 
                    Instant.ofEpochMilli(rs.getDate(8).getTime()).atZone(ZoneId.systemDefault()).toLocalDate(), 
                    rs.getFloat(9)
            );
            result.put(username, student);
        }
        
        return result;
    
    }
    
    /**
     * Transform ResultSet to {@link model.Assignment Assignment} objects
     * 
     * @param rs A ResultSet that needs to supply this info in the exact order : int courseId, String title, LocalDate submissionDate, String description, int id
     * @return ArrayList of type {@link model.Assignment model.Assignment}
     * @throws SQLException 
     */
    public LinkedHashMap<Integer, model.Assignment> assignmentify(ResultSet rs) throws SQLException, NullPointerException{
    
        LinkedHashMap<Integer, model.Assignment> result = new LinkedHashMap();
        
        while( rs.next() ){
        
            int id = rs.getInt(5);
            model.Assignment assignment = new model.Assignment(
                    rs.getInt(1), 
                    rs.getString(2), 
                    Instant.ofEpochMilli(rs.getDate(3).getTime()).atZone(ZoneId.systemDefault()).toLocalDate(), 
                    rs.getString(4), 
                    id
            );
            result.put(id, assignment);
        
        }
        
        return result;
        
    }
    
    /**
     * Transform ResultSet to {@link model.StudentAssignment StudentAssignment} objects
     * 
     * @param rs A ResultSet that needs to supply this info in the exact order : 
     * int courseId, String title, LocalDate submissionDate, String description, int id, int studentId, LocalDateTime submittedDate, int oralPoints, int writingPoints
     * 
     * @return ArrayList of type {@link model.StudentAssignment model.StudentAssignment}
     * @throws SQLException 
     */
    public ArrayList<model.StudentAssignment> studentAssignmentify(ResultSet rs) throws SQLException, NullPointerException{
    
        ArrayList<model.StudentAssignment> result = new ArrayList();
        
        while( rs.next() ){
        
            Timestamp submitted = rs.getTimestamp(7);
            if ( rs.wasNull() ) submitted = Timestamp.from(Instant.EPOCH);
            model.StudentAssignment sa = new model.StudentAssignment(
                    rs.getInt(1), 
                    rs.getString(2), 
                    Instant.ofEpochMilli(rs.getDate(3).getTime()).atZone(ZoneId.systemDefault()).toLocalDate(), 
                    rs.getString(4), 
                    rs.getInt(5), 
                    rs.getInt(6), 
                    Instant.ofEpochMilli(submitted.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime(), 
                    rs.getInt(8), 
                    rs.getInt(9)
            );
            result.add(sa);
        
        }
        
        return result;
        
    }
    
    /**
     * Transform ResultSet to {@link model.Schedule Schedule} objects
     * 
     * @param rs A ResultSet that needs to supply this info in the exact order : int courseId, LocalDateTime start, LocalDateTime end, int id
     * @return ArrayList of type {@link model.Schedule model.Schedule}
     * @throws SQLException 
     */
    public ArrayList<model.Schedule> schedulify(ResultSet rs) throws SQLException, NullPointerException{
    
        ArrayList<model.Schedule> result = new ArrayList();
        
        while( rs.next() ){
        
            model.Schedule sch = new model.Schedule(
                    rs.getInt(1), 
                    Instant.ofEpochMilli(rs.getTimestamp(2).getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime(), 
                    Instant.ofEpochMilli(rs.getTimestamp(3).getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime(), 
                    rs.getInt(4)
            );
            result.add(sch);
        
        }
        
        return result;
        
    }

}
