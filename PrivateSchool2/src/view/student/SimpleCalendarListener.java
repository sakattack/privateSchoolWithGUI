package view.student;

import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sakel
 */
public class SimpleCalendarListener implements CalendarListener {
    
    private StudentMenu menu;
    
    /**
     * Table that will hold all the Schedules of the logged in student for a selected date
     */
    private JTable studentSchedulesScrollTable;
    private DefaultTableModel studentSchedulesScrollTableModel;
    private JScrollBar studentSchedulesScrollTableBarV;

    public SimpleCalendarListener(StudentMenu menu) {
        this.menu = menu;
    }

    /**
     * selectedDateChanged, This function will be called each time that a date is selected in
     * the independent CalendarPanel. The new and old selected dates are supplied in the event
     * object. These parameters may contain null, which represents a cleared or empty date.
     *
     * By intention, this function will be called even if the user selects the same date value
     * twice in a row. This is so that the programmer can catch all events of interest.
     * Duplicate events can optionally be detected with the function
     * CalendarSelectionEvent.isDuplicate().
     * @param event
     */
    @Override
    public void selectedDateChanged(CalendarSelectionEvent event) {
        
        if ( !event.isDuplicate() ) {
        
            LocalDate newDate = event.getNewDate();
            
            if (newDate != null) {

                //prepare the table with the schedule           
                ArrayList<model.Schedule> selectedDateSchedules = new ArrayList();

                menu.getAllStudentSchedules().forEach((s) -> {

                    if ( 
                        (s.getStart().getDayOfYear() == newDate.getDayOfYear() && s.getStart().getYear() == newDate.getYear()) || 
                        (s.getEnd().getDayOfYear() == newDate.getDayOfYear() && s.getEnd().getYear() == newDate.getYear()) 
                       ) selectedDateSchedules.add(s);


                });

                int rows2 = selectedDateSchedules.size();
                String[] header = {"Start", "End", "Course id|Type|Stream|Title"};
                studentSchedulesScrollTableModel = new DefaultTableModel(null, header)
                {
                    @Override
                    public Class<?> getColumnClass(int col) {
                        return getValueAt(0, col).getClass();
                    }
                };
                studentSchedulesScrollTable = new JTable(studentSchedulesScrollTableModel)
                {

                    @Override
                    public boolean isCellEditable(int row, int column){
                        return false;
                    }
                };
                studentSchedulesScrollTable.setRowHeight(40);

                studentSchedulesScrollTable.getColumn("Start").setMaxWidth(250);
                studentSchedulesScrollTable.getColumn("Start").setMinWidth(150);

                studentSchedulesScrollTable.getColumn("End").setMaxWidth(250);
                studentSchedulesScrollTable.getColumn("End").setMinWidth(150);

                studentSchedulesScrollTable.getColumn("Course id|Type|Stream|Title").setMinWidth(250);

                //set scroll
                Dimension d2 = new Dimension(320, rows2 * studentSchedulesScrollTable.getRowHeight());
                studentSchedulesScrollTable.setPreferredScrollableViewportSize(d2);

                //add rows
                selectedDateSchedules.forEach((s) -> {

                    studentSchedulesScrollTableModel.addRow(new Object[]{

                        s.getStart().getHour() + ":" + ((s.getStart().getMinute() < 10) ? "0" + s.getStart().getMinute() : s.getStart().getMinute()),
                        s.getEnd().getHour() + ":" + ((s.getEnd().getMinute() < 10) ? "0" + s.getEnd().getMinute() : s.getEnd().getMinute()),
                        s.getCourseId() + "|" + menu.getAllCourses().get(s.getCourseId()).getType()
                                         + "|" + menu.getAllCourses().get(s.getCourseId()).getStream()
                                         + "|" + menu.getAllCourses().get(s.getCourseId()).getTitle()

                    });

                });

                //add table to scroll pane
                menu.getStudentMyCoursesResultsScrollPane().setViewportView(studentSchedulesScrollTable);
            
            }
        
        }

    }

    @Override
    public void yearMonthChanged(YearMonthChangeEvent event) {
        
    }

}