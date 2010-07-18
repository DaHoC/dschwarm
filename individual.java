package dschwarm;

/**
 *
 * @author Jan Hendriks
 */
public class individual {

    private int id; // Some own identifier, you can also put a name here
    private int generation;
    private double[] genes = new double[2]; // The coordinates / values wanted to converge
//    private double mutationRate = 0.001; // Keep small, otherwise the child values change too much

    // Constructor, initialization of the first generation in some range
    public individual(int id) {
        this.id = id;
        this.generation = 1;
         // +- 50
        this.genes[0] = (main.prng.nextDouble() * 100 - 50);
        this.genes[1] = (main.prng.nextDouble() * 100 - 50);
    }

    // Mate two (more [>2] or less [=1] also possible) individuals together
    public individual(individual i1V, individual i2V, int id, int generation) {
//        System.out.format(" I %3d : %+3.5f%n",this.id,this.getMutationRate());
        this.id = id;
        this.generation = generation;
        // Parent genes and mutation
        this.genes[0] = ((i1V.getValueAt(0) + i2V.getValueAt(0))/2); // arithmetic mean
        this.genes[0] += (this.genes[0] * this.getMutationRate() * (main.prng.nextDouble()-0.5));
        this.genes[1] = ((i1V.getValueAt(1) + i2V.getValueAt(1))/2); // arithmetic mean
//        this.genes[1] = Math.sqrt(Math.abs(i1V.getValueAt(1) * i2V.getValueAt(1))); // geometric mean
        this.genes[1] += (this.genes[1] * this.getMutationRate() * (main.prng.nextDouble()-0.5));

//        System.out.println("Distance: " + main.euclidianDistance(ownValue, main.coordinates));
//        System.out.println("V: " + ownValue[0] + " "+ ownValue[1]);
//        System.out.println("C: " + main.coordinates[0] + " " + main.coordinates[1]);
    }

    public double getValueAt(int coord) {
        if (coord < this.genes.length)
            return this.genes[coord];
        return 0;
    }

    public double[] getValues() {
        return this.genes;
    }

    // The mutation rate should decrease in time/generation because of the more accurate approximations
    // You can also use some small const value since the exp-function is quite costly to calculate
    private double getMutationRate() {
// System.out.println("E: "+ Math.exp(-(this.generation)) + " g: "+this.generation);
//      return this.mutationRate;
        return Math.exp(-(this.generation));
    }
}
