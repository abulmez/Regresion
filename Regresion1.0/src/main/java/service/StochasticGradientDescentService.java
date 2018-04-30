package service;

import repository.FileRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class StochasticGradientDescentService {

    FileRepo repo;

    public StochasticGradientDescentService(FileRepo repo) {
        this.repo = repo;
    }

    public Double getError(ArrayList<Double> factor,Integer currentUsedRow,Integer resultColumn){

        Double currentValue = factor.get(0);
        for(int j=1;j<factor.size();j++){
            currentValue+=repo.getDataValue(currentUsedRow,j-1)*factor.get(j);
        }
        return repo.getResultValue(currentUsedRow,resultColumn)-currentValue;
    }

    public ArrayList<Double> solve(Integer numberOfIterations,Double learningRate,Integer resultColumn){
        learningRate/=repo.getDataMatrixNumberOfRows();
        ArrayList<Integer> indexes = new ArrayList<>();
        Integer counter = 0;
        ArrayList<Double> factor = new ArrayList<>();
        for(int i=0;i<repo.getDataMatrixNumberOfRows();i++){
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        Random r = new Random();
        for(int i=0;i<repo.getDataMatrixNumberOfColumns()+1;i++){
            factor.add(Math.random());
        }

        for(int i=0;i<numberOfIterations;) {
            Integer row = indexes.get(counter);
            Double error = getError(factor, row, resultColumn);
            factor.set(0, (factor.get(0) - learningRate * error));
            for (int j = 1; j < factor.size(); j++) {
                factor.set(j, (factor.get(j) - learningRate * error * repo.getDataValue(row, j - 1)));
            }
            counter += 1;
            if(counter.equals(repo.getDataMatrixNumberOfRows())){
                counter = 0;
                i++;
                Collections.shuffle(indexes);
                //learningRate/=10;
            }
            /*
            if(counter.equals(1000)){
                counter = 0;
                learningRate/=10;
            }
            */
        }
        return factor;
    }
}
