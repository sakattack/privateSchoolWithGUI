package view;

import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * This is a helper class, it is not supposed to be instantiated.
 * It contains static methods for GUI manipulation
 * in case none is found
 *
 * @author Sakel
 */
public final class Actions {

    private Actions() {
    }
    
    /**
     * Writes a message into a JLabel target and then removes it after the
     * timeout has passed. no repeats
     * 
     * @param msg The message to display
     * @param millis The timeout duration in milliseconds
     * @param target The target JLabel container
     */
    public static void setLabelWithTimeoutMsg(String msg, int millis, JLabel target){
    
        Timer t;
        
        target.setText(msg);
        t = new Timer(millis, (ActionEvent ae) -> {
                target.setText("");
                target.getTopLevelAncestor().repaint();
            });
        t.setRepeats(false);
        t.setCoalesce(true);
        t.start();
    
    }
    
    public static JLabel getNewLabel(String str){
    
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(str);
        
        return jLabel1;
        
    }
    
    
    
}
