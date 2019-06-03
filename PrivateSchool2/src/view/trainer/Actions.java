package view.trainer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sakel
 */
public final class Actions {
    
    private javax.swing.JTextField trainerAssignmentEditOralField;
    private javax.swing.JLabel trainerAssignmentEditOralLabel;
    private javax.swing.JPanel trainerAssignmentEditPanel;
    private javax.swing.JButton trainerAssignmentEditSubmit;
    private javax.swing.JTextField trainerAssignmentEditWritingField;
    private javax.swing.JLabel trainerAssignmentEditWritingLabel;
    
    public Actions(TrainerMenu menu, model.StudentAssignment assignment, dao.StudentAssignmentDao sad) {
        editAssignmentForm(menu,assignment,sad);
    }

    /**
     * Creates a form that allows editing of a student's assignment
     * 
     * @param menu The JPanel that will contain the form
     * @param assignment
     * @param sad
     */
    public void editAssignmentForm(TrainerMenu menu, model.StudentAssignment assignment, dao.StudentAssignmentDao sad) {

        trainerAssignmentEditPanel = new javax.swing.JPanel();
        trainerAssignmentEditOralLabel = new javax.swing.JLabel();
        trainerAssignmentEditWritingLabel = new javax.swing.JLabel();
        trainerAssignmentEditOralField = new javax.swing.JTextField(assignment.getOralPoints());
        trainerAssignmentEditWritingField = new javax.swing.JTextField(assignment.getWritingPoints());
        trainerAssignmentEditSubmit = new javax.swing.JButton();
        
        trainerAssignmentEditOralField.setMaximumSize(new Dimension(100, 40));
        trainerAssignmentEditOralField.setMinimumSize(new Dimension(100, 40));
        trainerAssignmentEditOralField.setText(Integer.toString(assignment.getOralPoints()));
        
        trainerAssignmentEditWritingField.setMaximumSize(new Dimension(100, 40));
        trainerAssignmentEditWritingField.setMinimumSize(new Dimension(100, 40));
        trainerAssignmentEditWritingField.setText(Integer.toString(assignment.getWritingPoints()));
        
        trainerAssignmentEditSubmit.setMaximumSize(new Dimension(100, 40));
        trainerAssignmentEditSubmit.setMinimumSize(new Dimension(100, 40));

        trainerAssignmentEditOralLabel.setText("Oral Points");

        trainerAssignmentEditWritingLabel.setText("Writing Points");

        trainerAssignmentEditSubmit.setText("Mark");
        
        trainerAssignmentEditSubmit.addActionListener((ActionEvent event) -> {
            
            assignment.setOralPoints(Integer.parseInt(trainerAssignmentEditOralField.getText()));
            assignment.setWritingPoints(Integer.parseInt(trainerAssignmentEditWritingField.getText()));
            
            try {
                sad.updateOne(assignment);
            } catch (Exception ex) {
                Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //a quick hack that works. trigger reselection in order for the necessary events to rehappen
            int current = menu.getTrainerStudentsScrollTable().getSelectedRow();
            if ( current == 0 ) {
                menu.getTrainerStudentsScrollTable().removeRowSelectionInterval(current, current);
                menu.getTrainerStudentsScrollTable().setRowSelectionInterval(current, current);
            }
            else if ( current > 0 ) {
                menu.getTrainerStudentsScrollTable().setRowSelectionInterval(0, 0);
                menu.getTrainerStudentsScrollTable().setRowSelectionInterval(current, current);
            }
        
        });

        javax.swing.GroupLayout trainerAssignmentEditPanelLayout = new javax.swing.GroupLayout(trainerAssignmentEditPanel);
        trainerAssignmentEditPanel.setLayout(trainerAssignmentEditPanelLayout);

        trainerAssignmentEditPanelLayout.setHorizontalGroup(
                trainerAssignmentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(trainerAssignmentEditPanelLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(trainerAssignmentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(trainerAssignmentEditOralLabel)
                                        .addComponent(trainerAssignmentEditWritingLabel))
                                .addGap(18, 18, 18)
                                .addGroup(trainerAssignmentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(trainerAssignmentEditSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(trainerAssignmentEditOralField)
                                        .addComponent(trainerAssignmentEditWritingField))
                                .addGap(295, 295, 295))
        );
        trainerAssignmentEditPanelLayout.setVerticalGroup(
                trainerAssignmentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(trainerAssignmentEditPanelLayout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addGroup(trainerAssignmentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(trainerAssignmentEditOralLabel)
                                        .addComponent(trainerAssignmentEditOralField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(trainerAssignmentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(trainerAssignmentEditWritingField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(trainerAssignmentEditWritingLabel))
                                .addGap(18, 18, 18)
                                .addComponent(trainerAssignmentEditSubmit)
                                .addContainerGap(207, Short.MAX_VALUE))
        );

        trainerAssignmentEditPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[]{trainerAssignmentEditOralField, trainerAssignmentEditWritingField});

        trainerAssignmentEditPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[]{trainerAssignmentEditOralLabel, trainerAssignmentEditWritingLabel});

        menu.getTrainerAssignmentEditScrollPane().setViewportView(trainerAssignmentEditPanel);

    }
    

}
