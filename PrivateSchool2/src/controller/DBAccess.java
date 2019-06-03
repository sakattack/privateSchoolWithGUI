package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import model.User;
import model.Utils;

/**
 * Singleton class that keeps the connection to the database and provides 
 * authorization
 * 
 * @author Sakel
 */
public final class DBAccess {

    /**
     * The singleton instance of DBAccess. Instantiated through the use of the 
     * {@link controller.DBAccess#singleton singleton} method
     */
    private static DBAccess DBAccessInstance = null;
    
    /**
     * The connection to the database. Instantiated through the use of the 
     * {@link controller.DBAccess#singleton singleton} method. Stays open, and 
     * is closed {@link view.MainWindow#formWindowClosed(java.awt.event.WindowEvent) on MainWindow close}
     */
    private Connection con;
    
    /**
     * The connected user. Instantiated Instantiated through the use of the 
     * {@link controller.DBAccess#connectUser connectUser} method
     */
    private model.User user = null;
    
    private DBAccess() throws SQLException {
        
        //the database name. if you change this, change the name in the sql script as well
        String databaseName = "privateschool2";

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
        
        if ( con != null ) {
        
            ResultSet resultSet = con.getMetaData().getCatalogs();
            ArrayList<String> databases = new ArrayList();
            
            while (resultSet.next()) {
                databases.add(resultSet.getString(1));
            }
            
            if ( !databases.contains(databaseName) ) {
                
                try {
                    createHeadmaster(databaseName);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException ex) {
                    Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            else {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, "root", "");
            }
        
        }

    }

    public static DBAccess singleton() throws SQLException {
        if (DBAccessInstance == null)
            DBAccessInstance = new DBAccess();
        return DBAccessInstance;
    }
    
    public static DBAccess reset() throws SQLException {
    
        DBAccessInstance = new DBAccess();
        return DBAccessInstance;
    
    }

    public Connection getCon() {
        return con;
    }

    public User getUser() {
        return user;
    }
    
    
    /**
     * Instantiates the {@link controller.DBAccess#user user}, only if user == null,
     * providing authentication along the way, obviously.
     * 
     * @param enteredUsername
     * @param enteredPassword
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public void connectUser(String enteredUsername, char[] enteredPassword) 
            throws SQLException, 
            ClassNotFoundException, 
            NoSuchMethodException, 
            InstantiationException, 
            IllegalAccessException, 
            IllegalArgumentException, 
            InvocationTargetException,
            NoSuchAlgorithmException,
            InvalidKeySpecException
    {
            
        Utils.UserType accessLevel = null;
        ResultSet possibleUser_rs = null, rs = null;

        //Select user with entered username, if any (username column has a unique index)
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM users "
                + "WHERE username = ?");
        stmt.setString(1, enteredUsername);
        possibleUser_rs = stmt.executeQuery();

        //if user with that username exists
        if (possibleUser_rs.next()) {

            int user_id = possibleUser_rs.getInt(1);
            String user_type = possibleUser_rs.getString(2).toLowerCase();
            String user_username = possibleUser_rs.getString(3);
            byte[] user_password = possibleUser_rs.getBytes(4);
            byte[] user_key = possibleUser_rs.getBytes(5);
            accessLevel = Utils.UserType.valueOf(user_type.toUpperCase()); //make sure user type(access level) is in the Utils.UserType Enum

            byte[] hashedEnteredPassword = hashPassword(enteredPassword, user_key, 65536, 512);//hash entered password using the hash key of the matched user, from the database

            //if entered password matches stored password
            if ( accessLevel != null && Arrays.equals(user_password, hashedEnteredPassword) ) {

                //load appropriate User Class
                String classByUserType = user_type.substring(0, 1).toUpperCase() + user_type.substring(1);
                Class<?> classToLoad = Class.forName("model." + classByUserType);
                Constructor<?> ctor = classToLoad.getConstructor();
                User obj = (User) ctor.newInstance();

                Object[] args = null;

                //get additional user info if it is anything else other than a headmaster, who does not have any.
                if ( accessLevel != Utils.UserType.HEADMASTER ) {

                    stmt = con.prepareStatement("SELECT * FROM " + user_type + "s "
                    + "WHERE userid = ?");
                    stmt.setInt(1, user_id);
                    rs = stmt.executeQuery();

                    rs.next();

                    int columnCount = rs.getMetaData().getColumnCount() - 1;
                    args = new Object[columnCount];
                    Map dataTypeMap = new TreeMap();
                    dataTypeMap.put(java.sql.Types.VARCHAR, Class.forName("java.lang.String"));
                    dataTypeMap.put(java.sql.Types.DATE, Class.forName("java.sql.Date"));
                    dataTypeMap.put(java.sql.Types.FLOAT, Class.forName("java.lang.Float"));

                    /**
                     * @param args[] contains possible extra User info.
                     * Passed as arguments to specific user constructors,
                     * who are called only by the factory method of their class.
                     * Extra care is taken to avoid the first column, which is
                     * the id that we don't need since we have it from the user
                     * table
                     */
                    for( int i = 0; i < columnCount; i++ ){
                        args[i] = rs.getObject(i + 2, dataTypeMap);
                    }

                }

                user = (User) obj.factory(user_type, user_username, user_password, user_key, user_id, args);

            }

        }

        if (possibleUser_rs != null) {
            possibleUser_rs.close();
        }

        if (rs != null) {
            rs.close();
        }

        if (stmt != null) {
            stmt.close();
        }
    
    }
    
    /**
     * 
     * @param password 
     * @param salt get a salt key with DBAccess instance method {@link controller.DBAccess#randomSaltKeyFactory() randomSaltKeyFactory()}
     * @param iterations Use this to slow down the algorithm
     * @param keyLength max 512
     * @return a secure salted hash of the password
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
        PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
        SecretKey key = skf.generateSecret( spec );
        byte[] res = key.getEncoded( );
        
        return res;

    }
    
    /**
     * 
     * @return random 16 byte salt key
     */
    public byte[] randomSaltKeyFactory(){
        
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        return salt;
    }
    
    /**
     * Creates a headmaster, if none is existent in the database.
     * This method obviously wouldn't exist in a RL application
     * 
     * @param databaseName
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws SQLException 
     * @throws java.io.FileNotFoundException 
     */
    public void createHeadmaster(String databaseName) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException, FileNotFoundException, IOException{

        ScriptRunner runner = new ScriptRunner(con, false, false);
        String file = new File("").getAbsolutePath() + "/src/db_script_with_data.sql";
        runner.runScript(new BufferedReader(new FileReader(file)));
        
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, "root", "");

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id FROM users WHERE type = 'headmaster'");

        if ( !rs.next() ) {

            byte[] key = randomSaltKeyFactory();
            byte[] hashedPassword = hashPassword("ILoveJava".toCharArray(), key, 65536, 512);
            PreparedStatement stmt2 = con.prepareStatement("INSERT INTO users (`type`,`username`,`password`,`key`) VALUES ('headmaster','admin',?,?)");
            stmt2.setBytes(1, hashedPassword);
            stmt2.setBytes(2, key);
            stmt2.executeUpdate();

            if (stmt2 != null) {
                stmt2.close();
            }

        }

        if (rs != null) {
            rs.close();
        }

        if (stmt != null) {
            stmt.close();
        }
        
    }


}
