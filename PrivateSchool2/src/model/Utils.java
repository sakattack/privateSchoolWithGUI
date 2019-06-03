package model;

/**
 * Utilities class
 * 
 * @author Sakel
 * @version 1.0
 */
public final class Utils {

    /**
     * Empty private constructor. This is a utility class, it is 
     * not supposed to be instantiated
     */
    private Utils() {
    }
    
    
    
    /**
     * Permitted Course Streams
     * JAVA, CSHARP
     * 
     * @param value JAVA = java, CSHARP = c#
     */
    public enum CourseStream {
        JAVA("java"), CSHARP("csharp");
        
        final String value;
        
        private CourseStream( String stream ){
            value = stream;
        }

        public static String[] toStringArray() {
            String[] array = {"java","csharp"};
            return array;
        }
        
        
    }
    
    /**
     * Permitted Course Types
     * PART, FULL
     * 
     * @param value PART = part, FULL = full
     */
    public enum CourseType {
        PART("part"), FULL("full");
        
        final String value;
        
        private CourseType( String type ){
            value = type;
        }
        
        public static String[] toStringArray() {
            String[] array = {"part","full"};
            return array;
        }
        
    }
    
    /**
     * Permitted User Types
     * HEADMASTER, TRAINER, STUDENT
     * 
     * @param value HEADMASTER=headmaster, TRAINER=trainer, STUDENT=student
     */
    public enum UserType {
        HEADMASTER("headmaster"), TRAINER("trainer"), STUDENT("student");
        
        final String value;
        
        private UserType( String type ){
            value = type;
        }
        
        public static String[] toStringArray() {
            String[] array = {"headmaster","trainer","student"};
            return array;
        }
        
    }


}
