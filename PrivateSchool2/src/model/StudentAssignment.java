package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model of a Student's personal Assignment. Set automatically once a student
 * joins an Assignment's course or when an Assignment is created for a course
 * that the Student has joined
 * <p>
 * A StudentAssignment has
 * a {@link model.StudentAssignment#studentId studentId}, 
 * a {@link model.StudentAssignment#submittedDate submittedDate}, 
 * a {@link model.StudentAssignment#oralPoints oralPoints} and 
 * a {@link model.StudentAssignment#writingPoints writingPoints}. 
 * 
 * @author Sakel
 * @version 1.0
 */
public final class StudentAssignment extends Assignment implements Persistable {
    
    /**
     * identifier of the Student this StudentAssignment belongs to [int(11) not null]
     * <p>
     * If this is a new StudentAssignment then studentId must be provided explicitly
     * and has to belong to an existing Student from the database
     * <p>
     * If this is an existing StudentAssignment taken from the database then studentId
     * is provided by the database and is a <b>FK pointing to table students -> 
     * column userid</b>
     */
    private final int studentId;
    
    /**
     * Timestamp of the date and time this StudentAssignment was submitted, if ever, 
     * else null. [datetime null]
     */
    private LocalDateTime submittedDate;
    
    /**
     * Oral points of this StudentAssignment (0 is 0, null is not yet marked) [int(11) null]
     */
    private int oralPoints;
    
    /**
     * Writing points of this StudentAssignment (0 is 0, null is not yet marked) [int(11) null]
     */
    private int writingPoints;

    /**
     * 
     * 
     * @param courseId {@link model.Assignment#courseId courseId}
     * @param title {@link model.Assignment#title title}
     * @param submissionDate {@link model.Assignment#submissionDate submissionDate}
     * @param description {@link model.Assignment#description description}
     * @param id {@link model.Assignment#id assignmentId}
     * @param studentId int(11) PK. FK to students -> userid.
     * @param submittedDate datetime null
     * @param oralPoints int(11) null
     * @param writingPoints int(11) null
     */
    public StudentAssignment(int courseId, String title, LocalDate submissionDate, String description, int id, int studentId, LocalDateTime submittedDate, int oralPoints, int writingPoints) {
        super(courseId, title, submissionDate, description, id);
        this.studentId = studentId;
        this.submittedDate = submittedDate;
        this.oralPoints = oralPoints;
        this.writingPoints = writingPoints;
    }

    public int getStudentId() {
        return studentId;
    }

    public LocalDateTime getSubmittedDate() {
        return submittedDate;
    }

    public int getOralPoints() {
        return oralPoints;
    }

    public int getWritingPoints() {
        return writingPoints;
    }

    public void setSubmittedDate(LocalDateTime submittedDate) {
        this.submittedDate = submittedDate;
    }

    public void setOralPoints(int oralPoints) {
        this.oralPoints = oralPoints;
    }

    public void setWritingPoints(int writingPoints) {
        this.writingPoints = writingPoints;
    }

}
