import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//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

public class Choreographer {
    //properties of this class
    private ArrayList<Integer> constraintTimes = new ArrayList<Integer>();//contains the time constraints of the choreographers
    private int practiceTime;
    private String name;

    /*
     * Constructor for this class
     * constraint = time availabilities of the choreographer.
     */
    public Choreographer(int timeSlot, ArrayList<Integer> constraintTimes, String name) {
        this.practiceTime = timeSlot;
        this.constraintTimes = constraintTimes;
        this.name = name;
    }

    /*---------------------------------------------------------------------------------------------------------------------------------*/

    /*
     * Setter Methods
     */
    public void setChoreographer(ArrayList<Integer> constraint) {
        this.constraintTimes = constraint;
    }

    public void setPracticeTime(int timeSlot){
        this.practiceTime = timeSlot;
    }

    /*
     * Getter Methods
     */
    public int getTimeSlot(){
        return this.practiceTime;
    }

    public ArrayList<Integer> getConstraintTimes() {
        return this.constraintTimes;
    }

    public String getName() { return this.name; }

    /*---------------------------------------------------------------------------------------------------------------------------------*/


    public int compareTo(Choreographer otherChoreographer) {

        return Integer.compare(this.practiceTime, otherChoreographer.practiceTime);
    }

}