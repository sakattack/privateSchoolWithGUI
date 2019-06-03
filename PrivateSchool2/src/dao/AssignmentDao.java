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
public class AssignmentDao implements DaoInterface {
    
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
     * @return A ResultSet of assignment per line, with column order courseid, title, submissiondate, description, id
     * @throws SQLException
     * @throws Exception 
     */
    @Override
    public ResultSet getAll() throws SQLException, Exception {
        
        if ( Utils.UserType.valueOf(DBAccess.singleton().getUser().getType().toUpperCase()) == Utils.UserType.HEADMASTER ) {
            STMT = DBAccess.singleton().getCon().createStatement();
            RS = STMT.executeQuery("SELECT courseid, title, submissiondate, description, id FROM assignments");
            return RS;
        }
        else {
            throw new Exception(Utils.UserType.HEADMASTER + " only");
        }
        
    }
    
    public ResultSet getAllPerCourse(int courseId) throws SQLException, Exception {
    
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("SELECT courseid, title, submissiondate, description, id FROM assignments WHERE courseid = ?");
        PRESTMT.setInt(1, courseId);
        RS = PRESTMT.executeQuery();
        
        return RS;
    
    }
    
    @Override
    public boolean insertOne(Persistable o) throws Exception {
        
        int count = 0, generatedKey = 0, first = 0, second = 0;
        
        try{
            model.Assignment assignment = (model.Assignment)o;

            DBAccess.singleton().getCon().setAutoCommit(false);
            PRESTMT = DBAccess.singleton().getCon().prepareStatement("INSERT INTO assignments (`courseid`,`title`,`submissiondate`,`description`) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            PRESTMT.setInt(1, assignment.getCourseId());
            PRESTMT.setString(2, assignment.getTitle());
            PRESTMT.setDate(3, Date.valueOf(assignment.getSubmissionDate()));
            PRESTMT.setString(4, assignment.getDescription());

            first = PRESTMT.executeUpdate();

            ResultSet rsAssignment = PRESTMT.getGeneratedKeys();
            if (rsAssignment != null && rsAssignment.next()) {

                generatedKey = rsAssignment.getInt(1);
                rsAssignment.close();

            }

            if ( generatedKey > 0 ) {

                RS = new StudentDao().getAllPerCourse(assignment.getCourseId());

                while( RS.next() ){

                    PRESTMT = DBAccess.singleton().getCon().prepareStatement("INSERT INTO assignments_students (`assignmentid`,`studentid`) VALUES (?,?)");
                    PRESTMT.setInt(1, generatedKey);
                    PRESTMT.setInt(2, RS.getInt(5));
                    
                    second += PRESTMT.executeUpdate();
                    count++;

                }
                
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
        
        return (first + second) == count + 1;
        
    }
    
    @Override
    public boolean deleteOne(int i) throws Exception {
        
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("DELETE FROM assignments WHERE id = ?");
        PRESTMT.setInt(1, i);
        
        return PRESTMT.executeUpdate() > 0;
        
    }

    @Override
    public boolean updateOne(Persistable o) throws Exception {
        
        model.Assignment assignment = (model.Assignment)o;
        PRESTMT = DBAccess.singleton().getCon().prepareStatement("UPDATE assignments SET courseid = ?, title = ?, submissiondate = ?, description = ? WHERE id = ?");
        PRESTMT.setInt(1, assignment.getCourseId());
        PRESTMT.setString(2, assignment.getTitle());
        PRESTMT.setDate(3, Date.valueOf(assignment.getSubmissionDate()));
        PRESTMT.setString(4, assignment.getDescription());
        PRESTMT.setInt(5, assignment.getId());
        
        return PRESTMT.executeUpdate() > 0;
        
    }
    
    

}
