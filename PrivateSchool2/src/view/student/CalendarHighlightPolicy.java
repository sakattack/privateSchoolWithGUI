package view.student;

import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * SampleHighlightPolicy, A highlight policy is a way to visually highlight certain dates in the
 * calendar. These may be holidays, or weekends, or other significant dates.
 */
public class CalendarHighlightPolicy implements DateHighlightPolicy {

    private ArrayList<LocalDate> startDates = new ArrayList();
    private ArrayList<LocalDate> endDates = new ArrayList();
    
    
    /**
     * getHighlightInformationOrNull, Implement this function to indicate if a date should be
     * highlighted, and what highlighting details should be used for the highlighted date.
     *
     * If a date should be highlighted, then return an instance of HighlightInformation. If the
     * date should not be highlighted, then return null.
     *
     * You may (optionally) fill out the fields in the HighlightInformation class to give any
     * particular highlighted day a unique foreground color, background color, or tooltip text.
     * If the color fields are null, then the default highlighting colors will be used. If the
     * tooltip field is null (or empty), then no tooltip will be displayed.
     *
     * Dates that are passed to this function will never be null.
     * @param date
     * @return 
     */
    @Override
    public HighlightInformation getHighlightInformationOrNull(LocalDate date) {
        
        if ( startDates.contains(date) || endDates.contains(date) ) {
        
            return new HighlightInformation(Color.GREEN, null, null);
        
        }
        
        return null;
    }

    public void setStartDates(LocalDate startDate) {
        this.startDates.add(startDate);
    }

    public void setEndDates(LocalDate endDate) {
        this.endDates.add(endDate);
    }
    
    

}
