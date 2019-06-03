package model;

/**
 * Model of a Trainer 
 * set ONLY by the Headmaster
 * <p>
 * A Trainer has a {@link model.User#username username}, 
 * a {@link model.User#password password}, 
 * a {@link model.User#saltKey salt key}, 
 * a {@link model.User#id unique id} and 
 * a {@link model.User#type type}, 
 * inherited from {@link model.User User}
 * <p>
 * A Trainer also has a {@link model.Trainer#firstName firstName} and  
 * a {@link model.Trainer#lastName lastName}. 
 *
 * @author Sakel
 * @version 1.0
 */
public final class Trainer extends User implements Persistable {
    
    /**
     * This trainer's first name [varchar(50) not null]
     */
    private String firstName;
    
    /**
     * This trainer's last name [varchar(50) not null]
     */
    private String lastName;
    
    public Trainer(){
        super();
    }

    /**
     * Constructor for new trainers (no id yet)
     * 
     * @param type enum('headmaster','trainer','student') not null | users
     * @param username varchar(50) not null | users
     * @param password varbinary not null | users
     * @param saltKey varbinary not null | users
     * @param firstName varchar(50) not null | trainers
     * @param lastName varchar(50) not null | trainers
     */
    public Trainer(String type, String username, byte[] password, byte[] saltKey, String firstName, String lastName) {
        super(type, username, password, saltKey);
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    /**
     * Constructor for existing trainers (with id)
     * 
     * @param id int(11) auto increment PK | users
     * @param type enum('headmaster','trainer','student') not null | users
     * @param username varchar(50) not null | users
     * @param password varbinary not null | users
     * @param saltKey varbinary not null | users
     * @param firstName varchar(50) not null | trainers
     * @param lastName varchar(50) not null | trainers
     */
    public Trainer(String type, String username, byte[] password, byte[] saltKey, int id, String firstName, String lastName) {
        super(type, username, password, saltKey, id);
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    @Override
    public Trainer factory(String type, String username, byte[] password, byte[] saltKey, Object... args){
        
        Trainer t = new Trainer(type, username, password, saltKey, (String) args[0], (String) args[1]);
        
        return t;
    }
    
    @Override
    public Trainer factory(String type, String username, byte[] password, byte[] saltKey, int id, Object... args){
        
        Trainer t = new Trainer(type, username, password, saltKey, id, (String) args[0], (String) args[1]);
        
        return t;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    

}
