package two;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Parham on 06-Jun-19.
 */
public class Generic {
    private int numberOfGenerations;
    private int populationSize;
    private int tournamentSize;
    private double mutationRate;
    private double sigma;
    private ArrayList<Individual> population;
    private Random random;
    private ArrayList<ArrayList<Double>> goal = new ArrayList<>();
    private ArrayList<Double> bestFitnessInGenerations = new ArrayList<>();
    private ArrayList<Double> worstFitnessInGenerations = new ArrayList<>();
    private ArrayList<Double> averageFitnessInGenerations = new ArrayList<>();
    private Double maxValue;


    public Generic(int numberOfGenerations, int populationSize, int tournamentSize, double mutationRate, double sigma) {
        this.numberOfGenerations = numberOfGenerations;
        this.populationSize = populationSize;
        this.tournamentSize = tournamentSize;
        this.mutationRate = mutationRate;
        this.sigma = sigma;
        random = new Random();
    }

    public Generic() {
        this.numberOfGenerations = 1000;
        this.populationSize = 10000;
        this.tournamentSize = 2;
        this.mutationRate = 0.01;
        this.sigma = 0.2;
        random = new Random();
    }

    public void execute() {
        initialisation();
        ArrayList<Individual> generatedPopulation;
        for (int i = 0; i < numberOfGenerations; i++) {
            generatedPopulation = new ArrayList<Individual>();
            for (int j = 0; j < populationSize; j++) {
                Individual parent1 = tournamentSelection();
                Individual parent2 = tournamentSelection();
                Individual child = crossover(parent1, parent2);
                if (random.nextDouble() < mutationRate)
                    child = mutation(child);
                generatedPopulation.add(child);
            }
            generatedPopulation.addAll(population);
            remaining(generatedPopulation);
            bestFitnessInGenerations.add(findBestFitness());
            worstFitnessInGenerations.add(findWorstFitness());
            averageFitnessInGenerations.add(findAverageFitness());
//            System.out.println("Generation " + i + ":");
//            printPopulation();
        }
    }

    private void initialisation() {
        try (CSVReader dataReader = new CSVReader(new FileReader("input.csv"))) {
            String[] nextLine;
            ArrayList<Double> inside;
            while ((nextLine = dataReader.readNext()) != null) {
                inside = new ArrayList<>();
                double x = Double.parseDouble(nextLine[0]);
                double y = Double.parseDouble(nextLine[1]);
                inside.add(x);
                inside.add(y);
                goal.add(inside);
            }
/*
            maxValue = Double.MIN_VALUE;
            Double min = Double.MAX_VALUE;
            for (ArrayList<Double> aGoal : goal) {
                if (aGoal.get(1) > maxValue)
                    maxValue = aGoal.get(1);
                if (aGoal.get(1) < min)
                    min = aGoal.get(1);
            }
            if (min < 0 && (-1) * min > maxValue) {
                maxValue = (-1) * min;
            }
            for (ArrayList<Double> aGoal : goal) {
                aGoal.set(1, aGoal.get(1) / maxValue);
            }
*/
        } catch (IOException e) {
            e.printStackTrace();
        }

        population = new ArrayList<>();
        while (population.size() < populationSize) {
            population.add(new Individual());
        }
        System.out.println("Initial Population: ");
        printPopulation();
        System.out.println("/Initial Population");
    }

    private double fitnessFunction(double[] genes) {
        double mse = 0;
        double x;
        double y;
        for (ArrayList<Double> aGoal : goal) {
            x = (aGoal.get(0));
            y = (aGoal.get(1) - (genes[0] * x * x * x + genes[1] * x * x + genes[2] * x + genes[3]));
            mse += y * y;
        }
        mse /= goal.size();
        return (1 / (1 + mse));
    }

    private Individual tournamentSelection() {
        Individual parent, temp;
        parent = population.get(random.nextInt(populationSize));
        for (int i = 1; i < tournamentSize; i++) {
            temp = population.get(random.nextInt(populationSize));
            if (parent.fitness < temp.fitness) {
                parent = temp;
            }
        }
        return parent;
    }

    private Individual crossover(Individual parent1, Individual parent2) {
        int position1 = random.nextInt(4);
        int position2;
        do {
            position2 = random.nextInt(4);
        } while (position2 == position1);
        double[] genes = new double[4];
        for (int i = 0; i < 4; i++) {
            if (i == position1 || i == position2) {
                genes[i] = parent1.genes[i];
            } else {
                genes[i] = parent2.genes[i];
            }
        }
        return new Individual(genes);
    }

    private Individual mutation(Individual child) {
        java.util.Random r = new java.util.Random();
        double[] noise = new double[]{r.nextGaussian() * Math.sqrt(sigma) + 0, r.nextGaussian() * Math.sqrt(sigma) + 0,
                r.nextGaussian() * Math.sqrt(sigma) + 0, r.nextGaussian() * Math.sqrt(sigma) + 0};
        return new Individual(new double[]{child.genes[0] * noise[0], child.genes[1] * noise[1], child.genes[2] * noise[2], child.genes[3] * noise[3],});
    }

    private void remaining(ArrayList<Individual> generatedPopulation) {
        ArrayList<Individual> tempPopulation;
        Collections.sort(generatedPopulation);
        while (generatedPopulation.size() > populationSize) {
            generatedPopulation.remove(0);
        }
        population = generatedPopulation;
    }

    public class Individual implements Comparable<Individual> {
        public double[] genes;
        public Double fitness;

        public Individual(double[] coefficients) {
            this.genes = coefficients;
            fitness = fitnessFunction(this.genes);
        }

        public Individual() {
            this.genes = new double[]{(random.nextDouble() * 10000) - 5000, (random.nextDouble() * 10000) - 5000,
                    (random.nextDouble() * 10000) - 5000, (random.nextDouble() * 10000) - 5000};
            fitness = fitnessFunction(this.genes);
        }

        public boolean equals(Individual individual) {
            for (int i = 0; i < 4; i++) {
                if (this.genes[i] != individual.genes[i])
                    return false;
            }
            return true;
        }

        @Override
        public int compareTo(Individual o) {
            return this.fitness.compareTo(o.fitness);
        }
    }

    public void printPopulation() {
        for (int i = 0; i < populationSize; i++) {
            System.out.print(i + 1 + ":   ");
            printIndividual(population.get(i));
        }
    }

    public void printIndividual(Individual individual) {
        System.out.println("c1: " + individual.genes[0] + " c2: " + individual.genes[1] + " c3: " + individual.genes[2]
                + " c4: " + individual.genes[3] + " F: " + individual.fitness);
    }

    private boolean contains(Individual individual) {
        for (Individual i : population) {
            if (i.equals(individual))
                return true;
        }
        return false;
    }

    public ArrayList<Individual> getCorrectedPopulation() {
        ArrayList<Individual> pop = new ArrayList<>();
/*
        for (Individual individual : population) {
            pop.add(new Individual(new double[]{individual.genes[0] * maxValue, individual.genes[1] * maxValue,
                    individual.genes[2] * maxValue, individual.genes[3] * maxValue}));
        }
        System.out.println("MaxValue = " + maxValue);
        return pop;
*/
        return population;
    }

    public Individual findBestCorrectedIndividual() {
        double bestFitness = 0;
        int index = 0;
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).fitness > bestFitness) {
                bestFitness = population.get(i).fitness;
                index = i;
            }
        }
/*
        return new Individual(new double[]{population.get(index).genes[0] * maxValue, population.get(index).genes[1] *
                maxValue, population.get(index).genes[2] * maxValue, population.get(index).genes[3] * maxValue});
*/
        return population.get(index);
    }

    public double findBestFitness() {
        double bestFitness = 0;
        for (Individual aPopulation : population) {
            if (aPopulation.fitness > bestFitness) {
                bestFitness = aPopulation.fitness;
            }
        }
        return bestFitness;
    }

    public double findWorstFitness() {
        double worstFitness = 100;
        for (Individual aPopulation : population) {
            if (aPopulation.fitness < worstFitness) {
                worstFitness = aPopulation.fitness;
            }
        }
        return worstFitness;
    }

    public double findAverageFitness() {
        double averageFitness = 0;
        for (Individual aPopulation : population) {
            averageFitness += aPopulation.fitness;
        }
        return (averageFitness / population.size());
    }

    public ArrayList<Double> getBestFitnessInGenerations() {
        return bestFitnessInGenerations;
    }

    public ArrayList<Double> getWorstFitnessInGenerations() {
        return worstFitnessInGenerations;
    }

    public ArrayList<Double> getAverageFitnessInGenerations() {
        return averageFitnessInGenerations;
    }
}
