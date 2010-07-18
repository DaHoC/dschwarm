package dschwarm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Jan Hendriks
 */
public class main {

    public static java.util.Random prng = new Random();
    public static double eta = 0.000001;
    public static int dimensionCount = 2;
    public static double[] coordinates = new double[dimensionCount];
    public static Vector history = new Vector();
    public static int individualNumber;

    // Main program entry point
    public static void main(String[] args) {
        main.prng.setSeed(3015);
        coordinates[0] = (double)Math.PI;
        coordinates[1] = (double)20;
        individualNumber = 0;

        individual fittestIndividual;
        individual[] population = main.generatePopulation(500);
        history.addElement(population);

        int generation = 0;
        do {
            Arrays.sort(population, new individualsComparator()); // Order individuals
            individual[] fittestIndividuals = main.getBestIndividuals(250, population); // Get 10 best individuals ("Selection")
            fittestIndividual = fittestIndividuals[0];
            individual[] nextGeneration = new individual[fittestIndividuals.length];

            System.out.format(" Best individuals (Generation %3d):%n", (++generation));
            for (int n = 0; n < fittestIndividuals.length; n++) {
                System.out.format(" (%3.5f - %3.5f) ", fittestIndividuals[n].getValueAt(0), fittestIndividuals[n].getValueAt(1));
            }
            System.out.println();

            // Mating process
            int g = 0;
            for (int n = 0; n < Math.floor(fittestIndividuals.length/2); n++) {
                // Every good being has two children now
                for (int children = 0; children < 2; children++) {
                    nextGeneration[g++] = new individual(fittestIndividuals[n],fittestIndividuals[n+1],main.individualNumber++,g);
                }
            }
            population = nextGeneration;
            history.addElement(population);
//            System.out.println("Winner value: " + fittestIndividual.getValueAt(0));
        } while ( (main.euclideanDistance(fittestIndividual.getValues(), main.coordinates) > eta) ); // break condition, distance is less than some threshold (alternatively, the break condition could be the distance change from one generation to the next below eta)
        main.writeGnuPlotFile("history.dat", history);
    }

    private static individual[] generatePopulation (int count) {
        individual[] population = new individual[count];
        for (int i = 0; i < count; i++) {
            population[i] = new individual(main.individualNumber++);
        }
        return population;
    }

    private static individual[] getBestIndividuals (int count, individual[] population) {
        individual[] topIndividuals = new individual[count];
        /// @TODO: Dummy function, this only returns the first (smallest) individuals
        for (int p = 0; p < count; p++) {
            topIndividuals[p] = population[p];
        }
        return topIndividuals;
    }

    /*
     * Calculates the euclidian distance between the specified parameters
     */
    public static double euclideanDistance(double[] n1, double[] n2) {
        if (n1.length != n2.length) { // Vectors must have same dimension
            return (double)0;
        }
        // d(x,y) = \sqrt (sum_i (x_i - y_i)^2 )
        double accumulator = (double)0.0;
        for (int dimension = 0; dimension < n1.length; dimension++) {
            accumulator += ((double)Math.pow( (double)(n1[dimension] - n2[dimension]), 2));
        }
//        System.out.println("Distance: "+ Math.sqrt(accumulator));
        return (double)(Math.sqrt(accumulator));
    }

    // This writes the coordinates to a specified output file for use with gnuplot, e.g.
    // gnuplot> unset log x; unset log y; set xrange [0: 1]; set yrange [0: 1]; set format x "%3.5f"; set format x "%3.5f"; set xlabel "x"; set ylabel "y"; plot "SOM.net" title 'Grid' with vectors nohead, "SOM.net" using 1:2 title 'Centers';
    public static boolean writeGnuPlotFile(String FileName, Vector history) {
        System.out.println("Writing gnuplot file '" + FileName + "'");
        // Create file
        try {
            FileWriter fstream = new FileWriter(FileName);
            BufferedWriter out = new BufferedWriter(fstream);
            String comment ="# Usage example:\n# gnuplot> unset log x; unset log y; set xrange [0: 1]; set yrange [0: 1]; set format x \"%3.5f\"; set format x \"%3.5f\"; set xlabel \"x\"; set ylabel \"y\"; plot \""+FileName+"\" title 'Grid' with vectors nohead, \""+FileName+"\" using 1:2 title 'Centers';\n";
            // Write the built strings to the file
            out.write(comment);
            for (int c = 0; c < history.size(); c++) { // Walk through history
                out.write("# New generation, new frame (" + c + ")\n"); // replot / reread
                for (int j = 0; j < ((individual[])history.elementAt(c)).length; j++) { // Walk through population
                    double[] dimValue = ((individual[])history.elementAt(c))[j].getValues();
                    for (int k = 0; k < dimensionCount; k++) { // Walk through components/dimensions
//                        System.out.format("Writing history %3d, population %3d, component %3d%n",c,j,k);
                        // This avoids the automatic scientific notation (e.g. 3.726653172078671E-6)
                        out.write((new java.math.BigDecimal(Double.toString(dimValue[k]))).toPlainString() + "\t");
                    }
                    out.write("\n");
                }
//                positions += "repaint";
            }
//            out.write(positions);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
