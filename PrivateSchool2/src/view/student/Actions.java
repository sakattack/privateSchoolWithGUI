package view.student;

import com.github.lgooddatepicker.components.DatePickerSettings;
import controller.DBAccess;
import dao.DaoUtils;
import dao.StudentAssignmentDao;
import dao.StudentDao;
import dao.TrainerDao;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sakel
 */
public final class Actions {
    
    /**
     * Table that will hold all the Courses not yet joined by the logged in student
     */
    private JTable studentAVCoursesScrollTable;
    private DefaultTableModel studentAVCoursesScrollTableModel;
    private JScrollBar studentAVCoursesScrollTableBarV;
    
    /**
     * Table that will hold all the Assignments of the logged in student
     */
    private JTable studentAssignmentsScrollTable;
    private DefaultTableModel studentAssignmentsScrollTableModel;
    private JScrollBar studentAssignmentsScrollTableBarV;
    
    private StudentMenu menu;
    private int rows = 0;
    private String trainersString;

    public Actions(StudentMenu menu) {
        this.menu = menu;
        
        AVCourses();
        MyAssignments();
        MyCourses();     
    }
    
    public void AVCourses() {
        
        if ( !menu.getAllCourses().isEmpty() ) {
        
            //prepare the table with the courses
            DefaultTableCellRenderer centerRendererAVCourses = new DefaultTableCellRenderer();
            centerRendererAVCourses.setHorizontalAlignment( JLabel.CENTER );
            String[] header = {"ID", "Title", "Stream", "Type", "Trainers"};
            
            studentAVCoursesScrollTableModel = new DefaultTableModel(null, header)
            {
                @Override
                public Class<?> getColumnClass(int col) {
                    return getValueAt(0, col).getClass();
                }
            };
            studentAVCoursesScrollTable = new JTable(studentAVCoursesScrollTableModel)
            {

                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };

            studentAVCoursesScrollTable.setRowHeight(50);

            studentAVCoursesScrollTable.getColumn("ID").setMaxWidth(60);
            studentAVCoursesScrollTable.getColumn("ID").setMinWidth(60);

            studentAVCoursesScrollTable.getColumn("Title").setMinWidth(150);

            studentAVCoursesScrollTable.getColumn("Stream").setMaxWidth(100);
            studentAVCoursesScrollTable.getColumn("Stream").setMinWidth(100);

            studentAVCoursesScrollTable.getColumn("Type").setMaxWidth(100);
            studentAVCoursesScrollTable.getColumn("Type").setMinWidth(100);

            studentAVCoursesScrollTable.getColumn("Trainers").setMaxWidth(200);
            studentAVCoursesScrollTable.getColumn("Trainers").setMinWidth(200);
            
            // add rows
            menu.getAllCourses().entrySet().forEach((me) -> {

                if ( !menu.getAllStudentCourses().containsKey(me.getKey()) ) {
                    
                    //get course's trainers
                    TrainerDao td = new TrainerDao();
                    ResultSet rs = null;
                    LinkedHashMap<String, model.Trainer> allCourseTrainers = new LinkedHashMap();
                    
                    try {
                        rs = td.getAllPerCourse(me.getKey());
                        allCourseTrainers = new DaoUtils().trainerify(rs);
                    } catch (Exception ex) {
                        Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    trainersString = "<html>";
                    allCourseTrainers.entrySet().forEach((ct) -> {
                    
                        trainersString += ct.getValue().getFirstName() + " " + ct.getValue().getLastName() + "<br>";
                    
                    });
                    trainersString += "</html>";
                    
                    try {
                        new DaoUtils().close(rs, td.getSTMT(), td.getPRESTMT());
                    } catch (Exception ex) {
                        Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    //add row
                    rows++; 
                    studentAVCoursesScrollTableModel.addRow(new Object[]{

                        me.getKey(), me.getValue().getTitle(), me.getValue().getStream(), me.getValue().getType(), trainersString

                    });
                    
                }

            });

            //set scroll
            Dimension d2 = new Dimension(320, rows * studentAVCoursesScrollTable.getRowHeight());
            studentAVCoursesScrollTable.setPreferredScrollableViewportSize(d2);

            //when user selects a row then the selected course's join button is shown
            studentAVCoursesScrollTable.getSelectionModel().addListSelectionListener((ListSelectionEvent ev) -> {

                if (!ev.getValueIsAdjusting()) {
                    
                    menu.getStudentAVCoursesJoinPanelInner().removeAll();
                    
                    JLabel selectedCourseDetails = menu.getStudentAVCoursesJoinPanelLabel();
                    selectedCourseDetails.setText(
                            studentAVCoursesScrollTable.getValueAt(studentAVCoursesScrollTable.getSelectedRow(), 0).toString() + " | " + 
                            studentAVCoursesScrollTable.getValueAt(studentAVCoursesScrollTable.getSelectedRow(), 1).toString() + " | " +  
                            studentAVCoursesScrollTable.getValueAt(studentAVCoursesScrollTable.getSelectedRow(), 2).toString() + " | " + 
                            studentAVCoursesScrollTable.getValueAt(studentAVCoursesScrollTable.getSelectedRow(), 3).toString()
                    );
                    
                    javax.swing.JButton submit = new JButton("Join");

                    submit.addActionListener((ActionEvent ev2) -> {

                        StudentDao sd = new StudentDao();
                        try {
                            
                            if ( sd.insertOnePerCourse(DBAccess.singleton().getUser().getId(), Integer.parseInt(studentAVCoursesScrollTable.getValueAt(studentAVCoursesScrollTable.getSelectedRow(), 0).toString())) ) { 
                                submit.setVisible(false);
                                selectedCourseDetails.setText("");
                                menu.revalidate();
                                menu.initUserMenu();
                                // TODO : implement inserting one assignment per student for when a student joins a course that already has assignments
                            }
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        try {
                            new DaoUtils().close(sd.getSTMT(), sd.getPRESTMT());
                        } catch (Exception ex) {
                            Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
                        }


                    });
                    
                    menu.getStudentAVCoursesJoinPanelInner().add(submit);
                    menu.revalidate();
                
                }
            });

            //add table to scroll pane
            menu.getStudentAVCoursesScrollPane().setViewportView(studentAVCoursesScrollTable);
        
        }
    
    }
    
    public void MyAssignments(){
        
        if ( !menu.getAllStudentAssignments().isEmpty() ) {

            //clear course dropdown filter
            menu.getStudentMyAssignmentsCourseFilter().removeAllItems();
            menu.getStudentMyAssignmentsCourseFilter().addItem("Filter by one of your courses");

            menu.setAllStudentCoursesWithAssignments(new LinkedHashSet());

            //add student's courses to dropdown filter
            menu.getAllStudentAssignments().forEach((me) -> {

                menu.getAllStudentCoursesWithAssignments().add(me.getCourseId());

            });

            menu.getAllStudentCoursesWithAssignments().forEach((me) -> {

                menu.getStudentMyAssignmentsCourseFilter().addItem(
                        me + " | " + 
                        menu.getAllStudentCourses().get(me).getType() + " | " + 
                        menu.getAllStudentCourses().get(me).getStream() + " | " + 
                        menu.getAllStudentCourses().get(me).getTitle()
                );

            });

            populateMyAssignmentsTable();

        }
        else {

            // TODO : add message that student has no assignments yet

        }
    
    }
    
    public void populateMyAssignmentsTable(){
    
        //prepare the table with the assignments
        DefaultTableCellRenderer centerRendererMyAssignments = new DefaultTableCellRenderer();
        centerRendererMyAssignments.setHorizontalAlignment( JLabel.CENTER );
        int rows2 = menu.getAllStudentAssignments().size();
        String[] header = {"ID", "Title", "Deadline","Description","Course id|Type|Stream|Title", "Submitted","Oral Points","Writing Points"};
        studentAssignmentsScrollTableModel = new DefaultTableModel(null, header)
        {
            @Override
            public Class<?> getColumnClass(int col) {
                return getValueAt(0, col).getClass();
            }
        };
        studentAssignmentsScrollTable = new JTable(studentAssignmentsScrollTableModel)
        {

            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        studentAssignmentsScrollTable.setRowHeight(40);

        studentAssignmentsScrollTable.getColumn("ID").setMaxWidth(60);
        studentAssignmentsScrollTable.getColumn("ID").setMinWidth(60);
        studentAssignmentsScrollTable.getColumn("ID").setCellRenderer(centerRendererMyAssignments );

        studentAssignmentsScrollTable.getColumn("Title").setMinWidth(100);

        studentAssignmentsScrollTable.getColumn("Deadline").setMaxWidth(100);
        studentAssignmentsScrollTable.getColumn("Deadline").setMinWidth(100);

        studentAssignmentsScrollTable.getColumn("Description").setMinWidth(100);

        studentAssignmentsScrollTable.getColumn("Course id|Type|Stream|Title").setMaxWidth(350);
        studentAssignmentsScrollTable.getColumn("Course id|Type|Stream|Title").setMinWidth(250);

        studentAssignmentsScrollTable.getColumn("Submitted").setMaxWidth(150);
        studentAssignmentsScrollTable.getColumn("Submitted").setMinWidth(150);

        studentAssignmentsScrollTable.getColumn("Oral Points").setMaxWidth(100);
        studentAssignmentsScrollTable.getColumn("Oral Points").setMinWidth(70);
        studentAssignmentsScrollTable.getColumn("Oral Points").setCellRenderer(centerRendererMyAssignments );

        studentAssignmentsScrollTable.getColumn("Writing Points").setMaxWidth(150);
        studentAssignmentsScrollTable.getColumn("Writing Points").setMinWidth(100);
        studentAssignmentsScrollTable.getColumn("Writing Points").setCellRenderer(centerRendererMyAssignments );

        //set scroll
        Dimension d2 = new Dimension(320, rows2 * studentAssignmentsScrollTable.getRowHeight());
        studentAssignmentsScrollTable.setPreferredScrollableViewportSize(d2);

        // add rows
        menu.getAllStudentAssignments().forEach((me) -> {

            LocalDateTime submitted = me.getSubmittedDate();
            String submittedString = ( submitted.getYear() == 1970 ) ? "" : submitted.toString();

            studentAssignmentsScrollTableModel.addRow(new Object[]{

                me.getId(), 
                me.getTitle(), 
                me.getSubmissionDate(), 
                me.getDescription(), 
                me.getCourseId() + "|" + menu.getAllCourses().get(me.getCourseId()).getType()
                                 + "|" + menu.getAllCourses().get(me.getCourseId()).getStream()
                                 + "|" + menu.getAllCourses().get(me.getCourseId()).getTitle(), 
                submittedString, 
                me.getOralPoints(), 
                me.getWritingPoints()

            });

        });

        //when user selects a row then the selected assignment is open for marking
        studentAssignmentsScrollTable.getSelectionModel().addListSelectionListener((ListSelectionEvent ev) -> {
            if (!ev.getValueIsAdjusting()) {

                menu.getStudentMyAssignmentsSubmitPanelInner().setVisible(true);
                menu.getStudentMyAssignmentsSubmitPanelInner().removeAll();

                javax.swing.JButton submit = new JButton();

                if ( studentAssignmentsScrollTable.getValueAt(studentAssignmentsScrollTable.getSelectedRow(), 5).toString().equals("") ) {
                    submit.setText("Submit");
                }
                else {
                    submit.setText("Re-Submit");
                }

                model.StudentAssignment updatedAssignment = menu.getAllStudentAssignments().get(
                        studentAssignmentsScrollTable.getSelectedRow()
                );

                menu.getStudentMyAssignmentsSubmitPanelLabel().setText(
                        updatedAssignment.getId() + " | " + 
                        updatedAssignment.getTitle() + " | " +  
                        updatedAssignment.getSubmissionDate()
                );

                submit.addActionListener((ActionEvent ev2) -> {

                    updatedAssignment.setSubmittedDate(LocalDateTime.now());

                    StudentAssignmentDao sad = new StudentAssignmentDao();
                    try {

                        if ( sad.updateOne(updatedAssignment) ) { 
                            //a quick hack that works. trigger reselection in order for the necessary events to rehappen
                            int current = menu.getStudentMyAssignmentsCourseFilter().getSelectedIndex();
                            menu.getStudentMyAssignmentsCourseFilter().setSelectedIndex(current);
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        new DaoUtils().close(sad.getSTMT(), sad.getPRESTMT());
                    } catch (Exception ex) {
                        Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
                    }


                });

                menu.getStudentMyAssignmentsSubmitPanelInner().add(submit);
                menu.revalidate();

            }
        });

        //add table to scroll pane
        menu.getStudentMyAssignmentsScrollPane().setViewportView(studentAssignmentsScrollTable);
    
    }
    
    
    public void MyCourses(){
    
        if ( !menu.getAllStudentCourses().isEmpty() ) {
        
            //clear course dropdown filter
            menu.getStudentMyCoursesCourseFilter().removeAllItems();
            
            //repopulate it
            menu.getStudentMyCoursesCourseFilter().addItem("Filter by one of your courses");
            menu.getAllStudentCourses().entrySet().forEach((me) -> {
            
                menu.getStudentMyCoursesCourseFilter().addItem(
                        me.getKey() + " | " + 
                        me.getValue().getType() + " | " + 
                        me.getValue().getStream() + " | " + 
                        me.getValue().getTitle()
                );
            
            });
            
            initializeCalendar();
        
        }
    
    }
    
    public void initializeCalendar(){
    
        //highlight days with scheduled course sessions and disable all rest in the calendar
        DatePickerSettings settings = new DatePickerSettings();
        menu.getStudentMyCoursesCalendarPanel().setSettings(settings);
        menu.getStudentMyCoursesCalendarPanel().addCalendarListener(new SimpleCalendarListener(menu));
        CalendarHighlightPolicy highlightPolicy = new CalendarHighlightPolicy();
        CalendarVetoPolicy vetoPolicy = new CalendarVetoPolicy();
        menu.getAllStudentSchedules().forEach((me) -> {

            highlightPolicy.setStartDates(me.getStart().toLocalDate());
            highlightPolicy.setEndDates(me.getEnd().toLocalDate());
            vetoPolicy.setStartDates(me.getStart().toLocalDate());
            vetoPolicy.setEndDates(me.getEnd().toLocalDate());

        });
        settings.setHighlightPolicy(highlightPolicy);
        settings.setVetoPolicy(vetoPolicy);
        settings.setVisibleClearButton(false);
        LocalDate tmpSelected = menu.getStudentMyCoursesCalendarPanel().getSelectedDate();
        menu.getStudentMyCoursesCalendarPanel().setSelectedDate(null);
        if (tmpSelected != null) menu.getStudentMyCoursesCalendarPanel().setSelectedDate(tmpSelected);
        
    }

}
