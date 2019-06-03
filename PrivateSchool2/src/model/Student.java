package model;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Model of a Student 
 * set ONLY by the Headmaster
 * <p>
 * A Student has a {@link model.User#username username}, 
 * a {@link model.User#password password}, 
 * a {@link model.User#saltKey salt key}, 
 * a {@link model.User#id unique id} and 
 * a {@link model.User#type type}, 
 * inherited from {@link model.User User}
 * <p>
 * A Student also has a {@link model.Student#firstName firstName}, 
 * a {@link model.Student#lastName lastName}, 
 * a {@link model.Student#birthDate birthDate} and 
 * a {@link model.Student#tuitionFees tuitionFees}. 
 *
 * @author Sakel
 * @version 1.0
 */
public final class Student extends User implements Persistable {
    
    /**
     * This student's first name [varchar(50) not null]
     */
    private String firstName;
    
    /**
     * This student's last name [varchar(50) not null]
     */
    private String lastName;
    
    /**
     * This student's birth date [date not null]
     */
    private LocalDate birthDate;
    
    /**
     * This student's tuition fees [float not null default = 0]
     */
    private float tuitionFees = 0.0f;
    
    public Student(){
        super();
    }

    /**
     * Constructor for new students (no id yet)
     * 
     * @param type enum('headmaster','trainer','student') not null | users
     * @param username varchar(50) not null | users
     * @param password varbinary not null | users
     * @param saltKey varbinary not null | users
     * @param firstName varchar(50) not null | students
     * @param lastName varchar(50) not null | students
     * @param birthDate date not null | students
     * @param tuitionFees float not null default = 0 | students
     */
    public Student(String type, String username, byte[] password, byte[] saltKey, String firstName, String lastName, LocalDate birthDate, float tuitionFees) {
        super(type, username, password, saltKey);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.tuitionFees = tuitionFees;
    }
    
    /**
     * Constructor for existing students (with id)
     * 
     * @param id int(11) auto increment PK | users
     * @param type enum('headmaster','trainer','student') not null | users
     * @param username varchar(50) not null | users
     * @param password varbinary not null | users
     * @param saltKey varbinary not null | users
     * @param firstName varchar(50) not null | students
     * @param lastName varchar(50) not null | students
     * @param birthDate date not null | students
     * @param tuitionFees float not null default = 0 | students
     */
    public Student(String type, String username, byte[] password, byte[] saltKey, int id, String firstName, String lastName, LocalDate birthDate, float tuitionFees) {
        super(type, username, password, saltKey, id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.tuitionFees = tuitionFees;
    }
    
    @Override
    public Student factory(String type, String username, byte[] password, byte[] saltKey, Object... args){
        
        Student s = new Student(type, username, password, saltKey, (String) args[0], (String) args[1], (LocalDate) args[2], (float) args[3]);
        
        return s;
    }
    
    @Override
    public Student factory(String type, String username, byte[] password, byte[] saltKey, int id, Object... args){
        
        Date bday = (Date)args[2];
        Student s = new Student(type, username, password, saltKey, id, (String) args[0], (String) args[1], Instant.ofEpochMilli(bday.getTime()).atZone(ZoneId.systemDefault()).toLocalDate(), (float) args[3]);
        
        return s;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public float getTuitionFees() {
        return tuitionFees;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setTuitionFees(float tuitionFees) {
        this.tuitionFees = tuitionFees;
    }

}
