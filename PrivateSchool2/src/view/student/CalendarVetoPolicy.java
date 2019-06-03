package view.student;

import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * SampleDateVetoPolicy, A veto policy is a way to disallow certain dates from being selected in
 * calendar. A vetoed date cannot be selected by using the keyboard or the mouse.
 */
public class CalendarVetoPolicy implements DateVetoPolicy {
    
    private ArrayList<LocalDate> startDates = new ArrayList();
    private ArrayList<LocalDate> endDates = new ArrayList();

    /**
     * isDateAllowed, Return true if a date should be allowed, or false if a date should be
     * vetoed.
     * @param date
     * @return 
     */
    @Override
    public boolean isDateAllowed(LocalDate date) {
        
        return startDates.contains(date) || endDates.contains(date);
    }

    public void setStartDates(LocalDate startDate) {
        this.startDates.add(startDate);
    }

    public void setEndDates(LocalDate endDate) {
        this.endDates.add(endDate);
    }   
    
    
}