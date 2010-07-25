package dschwarm;

import java.util.Comparator;

/**
 *
 * @author Jan Hendriks
 */
public class individualsComparator implements Comparator {

    /**
     * Compares two individuals according to their properties
     * @param i1 individuals object
     * @param i2 individuals object
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second according to some measure
     */
    public int compare (Object i1, Object i2) {
        //parameter are of type Object, so we have to downcast it to individual objects
        double[] i1Values = ( (individual)i1).getValues();
        double[] i2Values = ( (individual)i2).getValues();
        // Now we don't sort in a natural way, we sort according to some desired property tendency
        /*
        if (i1Value < i2Value) return -1;
        else if (i1Value > i2Value ) return 1;
        else return 0;
         */
        // Here, we use (PI,1) as the desired tendency (calculate distance to (PI,1) , the nearest one with the smallest euclidian distance is the best individual)
//        double distance1 = Math.abs(main.euclideanDistance(i1Values, main.coordinates) -(double)10 );
//        double distance2 = Math.abs(main.euclideanDistance(i2Values, main.coordinates) -(double)10 );

        double distance1 = main.euclideanDistance(i1Values, main.coordinates);
        double distance2 = main.euclideanDistance(i2Values, main.coordinates);

        if (distance1 < distance2)
            return -1;
        else if (distance1 > distance2)
            return 1;
        else
            return 0;
    }
    
}
