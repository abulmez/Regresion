package service;

import DeepCopyUtil.DeepCopy;
import model.Chromosome;
import model.Matrix;
import model.Population;
import repository.FileRepo;

import java.util.*;

public class EvolutionaryAlgorithmService {

    public FileRepo repo;
    private ArrayList<Double> mutationVector;
    public static ArrayList<Double> minHistory;

    public EvolutionaryAlgorithmService(FileRepo repo){
        this.repo = repo;
        minHistory = new ArrayList<>();
    }

    /**
     *  The function initializes the mutation vector with less/higher chance of mutation depending on the mutatianRate parameter
        mutatiaRate - Integer between 0 and 10
     **/
    private void initMutationVector(Integer mutatianRate){
        mutationVector = new ArrayList<>();
        mutationVector.add(0.1*mutatianRate);
        Double remainingMutatianRate = (Double)(1-0.1*mutatianRate);
        mutationVector.add(0.3*remainingMutatianRate);
        mutationVector.add(0.2*remainingMutatianRate);
        mutationVector.add(0.2*remainingMutatianRate);
        mutationVector.add(0.1*remainingMutatianRate);
        mutationVector.add(0.1*remainingMutatianRate);
        mutationVector.add(0.05*remainingMutatianRate);
        mutationVector.add(0.05*remainingMutatianRate);
        cumsum(mutationVector);
    }


    /**
     *  The function determines the value of a given probability using a provided simple random variable
     * @param list  The distribution of the random variable
     * @param random Double (probability between 0 and 1)
     * @return  Returns the corresponding value for the given probability
     */
    private Integer vasInv(List<Double> list, Double random){
        for(int i=0;i<list.size();i++){
            if(random<list.get(i)){
                return i;
            }
        }
        return -1;
    }

    /**
     * The function determines the value associated with a chromosome
     * @param c Chromosome
     * @return  Returns the values of the provided Chromosome
     */
    private double fitness(Chromosome c,Integer resultColumn){
        Double totalError = 0.0;
        for(int i=0;i<repo.getDataMatrixNumberOfRows();i++) {
            Double determinedValue = c.getValue(0);
            for (int j = 0; j < repo.getDataMatrixNumberOfColumns(); j++) {
                determinedValue += repo.getDataValue(i,j)*c.getValue(j+1);
            }
            totalError += Math.abs(repo.getResultValue(i,resultColumn)-determinedValue);
        }
        return totalError;
    }

    /**
     * The function determines the cumulative sum of a list of values
     * @param list List of Double values
     */
    private void cumsum(List<Double> list){
        for(int j=1;j<list.size();j++){
            list.set(j,list.get(j)+list.get(j-1));
        }
    }

    /**
     * The function determines the minimum value in a population
     * @param p Population
     * @return Returns the minimum found value
     */
    private ArrayList<Double> evaluatePopulation(Population p,Integer resultColumn){
        ArrayList<Double> errorList = new ArrayList<>();
        for(Chromosome c:p.getChromosomesList()){
            errorList.add(fitness(c,resultColumn));
        }
        return errorList;
    }




    /**
     * The function mutates a Chromosome
     * @param offspring The Chromosome to be mutated
     */
    private  void mutate(Chromosome offspring){
        for(int i=0;i<offspring.getValues().size();i++){
            Double mutationOffset =  (double) (vasInv(mutationVector, Math.random()));
            mutationOffset/=1000;
            Boolean mutationDirection = Math.random()<0.5;
            Double mutatedValue;
            if(mutationDirection)
                mutatedValue =  offspring.getValue(i)+mutationOffset;
            else
                mutatedValue = offspring.getValue(i)-mutationOffset;
            offspring.setValue(i,mutatedValue);
        }
    }

    /**
     * The function creates a new Chromosome according to two other Chromosomes
     * @param dad  One of the parent Chromosome
     * @param mom  The other parent Chromosome
     * @return  Return the offspring of the 2 provided chromosomes
     */
    private Chromosome offspring(Chromosome dad,Chromosome mom){
        Vector<Double> newValues = new Vector<>();
        for(int i=0;i<dad.getValues().size();i++){
            newValues.add((dad.getValue(i)+mom.getValue(i))/2);
        }
        return new Chromosome(newValues.toArray(new Double[0]));
    }

    private Integer getBestChromosomeInPopulation(Population p,ArrayList<Double> errorList){
        Double min=Double.MAX_VALUE;
        Integer minPosition = 0;
        for(int i=0;i<errorList.size();i++){
            if(errorList.get(i)<min){
                minPosition = i;
                min = errorList.get(i);
            }
        }
        return minPosition;
    }


    /**
     * The function determines the minimum value in an 1 or 2 dimensional array using an evolutionary algorithm
     * @param numberOfGenerations
     * @param populationSize    The size of every population in each generation
     * @param mutatianRate     The mutation rate of the population
     * @return  Return the found minimum
     */
    public ArrayList<Double> solve(Integer numberOfGenerations, Integer populationSize, Integer mutatianRate, Integer resultColumn){
        initMutationVector(10-mutatianRate);
        Population p = new Population();
        for(int i=0;i<populationSize;i++) {
            ArrayList<Double> chromosomeValues = new ArrayList<>();
            for(int j=0;j<repo.getDataMatrixNumberOfColumns()+1;j++){
                Double d = Math.random();
                chromosomeValues.add(d);
            }
            p.addChromosome(new Chromosome(chromosomeValues.toArray(new Double[0])));
        }
        //Double min = evaluatePopulation(p,resultColumn);
        for(int i=0;i<numberOfGenerations;i++){
            Population offsprings = new Population();

            //Double sum = normalizePopulation(p);
            LinkedList<Double> chance = new LinkedList<>();
            ArrayList<Double> errorList = evaluatePopulation(p,resultColumn);
            Double errorSum = errorList.stream().mapToDouble(Double::doubleValue).sum();
            for(Double elem:errorList){
                chance.addFirst(elem/errorSum);
            }
            //resetNormalization(p);
            cumsum(chance);
            for(int j=0;j<populationSize-1;j++) {
                Chromosome dad = p.getChromosome(vasInv(chance, Math.random()));
                Chromosome mom = p.getChromosome(vasInv(chance, Math.random()));
                Chromosome offspring = offspring(dad,mom);
                mutate(offspring);
                offsprings.addChromosome(offspring);
            }
            Integer bestChromosomePosition = getBestChromosomeInPopulation(p,errorList);
            Chromosome bestFromLastPopulation = (Chromosome) DeepCopy.copy(p.getChromosome(bestChromosomePosition));
            offsprings.addChromosome(bestFromLastPopulation);
            p = offsprings;
        }
        ArrayList<Double> errorList = evaluatePopulation(p,resultColumn);
        Integer bestChromosomePosition = getBestChromosomeInPopulation(p,errorList);
        System.out.println("Eroarea totala pentru coeficientii determinati: "+errorList.get(bestChromosomePosition));
        return p.getChromosome(bestChromosomePosition).getValues();
    }

    public Matrix<Double> test(ArrayList<Double> factor,Integer resultColumn){
        Matrix<Double> result = new Matrix<>(10000,2,0.0);
        for(int i=0;i<repo.getTestDataMatrixNumberOfRows();i++){
            Double resultValue = factor.get(0);
            for(int j=1;j<factor.size();j++){
                resultValue += factor.get(j)*repo.getTestingDataValue(i,j-1);
            }
            result.set(repo.getTestResultValue(i,resultColumn),i,0);
            result.set(resultValue,i,1);
        }
        return result;
    }

    /**
     * The function determines the minimum value in an 1 or 2 dimensional array by browsing the array
     * @return Return the minimum
     */


}
