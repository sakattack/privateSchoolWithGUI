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
public class StudentAssignmentDao implements DaoInterface {
    
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
     * @return A ResultSet of student assignments per line, with column order : 
     * courseid, title, submissiondate, description, id, studentid, submitteddate, oralpoints, writingpoints
     * 
     * @throws SQLException
     * @throws Exception 
     */
    @Override
    public ResultSet getAll() throws SQLException, Exception {
        
        throw new UnsupportedOperationException("Not supported yet.");
        
    }
    
    public ResultSet getAllPerStudent(int userId) throws SQLException, Exception {
    
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("SELECT a.courseid, a.title, a.submissiondate, a.description, a.id, sa.studentid, sa.submitteddate, sa.oralpoints, sa.writingpoints FROM ( assignments_students AS sa INNER JOIN assignments AS a ON sa.assignmentid = a.id ) WHERE sa.studentid = ?");
        PRESTMT.setInt(1, userId);
        RS = PRESTMT.executeQuery();

        return RS; 
    
    }
    
    public ResultSet getAllPerStudentPerCourse(int userId, int courseId) throws SQLException, Exception {
    
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("SELECT a.courseid, a.title, a.submissiondate, a.description, a.id, sa.studentid, sa.submitteddate, sa.oralpoints, sa.writingpoints FROM ( assignments_students AS sa INNER JOIN assignments AS a ON sa.assignmentid = a.id ) WHERE a.courseid = ? AND sa.studentid = ?");
        PRESTMT.setInt(1, courseId);
        PRESTMT.setInt(2, userId);
        RS = PRESTMT.executeQuery();

        return RS; 
    
    }
    
    @Override
    public boolean insertOne(Persistable o) throws Exception {
        
        //assignments per student are inserted along with main assignments automatically for every student who is enrolled to the assignment's course at the time of the assignment's creation
        // TODO : implement inserting one assignment per student for when a student joins a course that already has assignments
        return false;
        
    }
    
    @Override
    public boolean deleteOne(int i) throws Exception {
        // assignments_students records get deleted ONLY automatically when a student or a course gets deleted
        return false;
        
    }
    
    @Override
    public boolean updateOne(Persistable o) throws Exception {
        
        model.StudentAssignment stdAs = (model.StudentAssignment)o;
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("UPDATE assignments_students SET submitteddate = ?, oralpoints = ?, writingpoints = ? WHERE assignmentid = ? AND studentid = ?");
        PRESTMT.setTimestamp(1, Timestamp.valueOf(stdAs.getSubmittedDate()));
        PRESTMT.setInt(2, stdAs.getOralPoints());
        PRESTMT.setInt(3, stdAs.getWritingPoints());
        PRESTMT.setInt(4, stdAs.getId());
        PRESTMT.setInt(5, stdAs.getStudentId());
        
        return PRESTMT.executeUpdate() > 0;
        
    }


}
