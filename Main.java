package two;

/**
 * Created by Parham on 06-Jun-19.
 */
public class Main {
    public static void main(String[] args) {
/*
        InputGenerator inputGenerator = new InputGenerator();
        inputGenerator.execute();
*/
        Generic generic = new Generic();
        generic.execute();
        generic.printPopulation();
        generic.printIndividual(generic.findBestCorrectedIndividual());
        PredicationPlotting.run(args, generic.findBestCorrectedIndividual().genes);
//        PredicationPlotting.run(args, generic.getBestFitnessInGenerations(), generic.getWorstFitnessInGenerations(), generic.getAverageFitnessInGenerations());
    }
}
