package view.headmaster;

import dao.DaoInterface;
import dao.DaoUtils;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author Sakel
 */
public final class Actions {
    
    private Actions() {
    }

    /**
     * A method that removes rows from the database by calling the 
     * {@link dao.DaoInterface#deleteOne(int)  deleteOne} method of the appropriate dao 
     * that implements {@link dao.DaoInterface DaoInterface}
     * 
     * @param exIDLabel the JLable holding the id of the course/trainer/student/assignment to be deleted
     * @param dao the appropriate dao for what you want to delete
     * @return success or not
     */
    public static boolean deleteOne(javax.swing.JLabel exIDLabel, DaoInterface dao) {
        
        int id = Integer.parseInt(exIDLabel.getText());
        boolean success = false;
        
        if ( id > 0 ) {

            try {
                success = dao.deleteOne(id);
                new DaoUtils().close(dao.getSTMT(), dao.getPRESTMT());
            } catch (Exception ex) {
                Logger.getLogger(HeadmasterMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        
        return success;
        
    }
    
    /**
     * A method that transports list elements from one JList object to another
     * 
     * @param source 
     * @param target 
     */
    public static <T> void transport(JList<T> source, JList<T> target){
    
        int[] array = source.getSelectedIndices();
        ArrayList<Integer> selectedRowIndices = new ArrayList();
        for( int i : array ){ selectedRowIndices.add(i); }
        
        DefaultListModel<T> sourceModel = (DefaultListModel<T>) source.getModel();
        DefaultListModel<T> targetModel = (DefaultListModel<T>) target.getModel();
        DefaultListModel<T> tmpModel = new DefaultListModel();
        
        int a = sourceModel.size();
        int count = 0;
        for( int i = 0; i < a; i++ ){
        
            if ( selectedRowIndices.contains(i) ) {
                tmpModel.add(count++, sourceModel.getElementAt(i));
            }
        
        }
        
        for( int i = a -1; i >= 0; i-- ){
        
            if ( selectedRowIndices.contains(i) ) {
                sourceModel.removeElementAt(i);
            }
        
        }
        
        int b = tmpModel.size();
        int c = targetModel.size();
        for( int i = 0; i < b; i++ ){
        
            targetModel.add(c + i, tmpModel.getElementAt(i));
        
        }
        
        source.setModel(sourceModel);
        target.setModel(targetModel);
    
    }

    
    
}