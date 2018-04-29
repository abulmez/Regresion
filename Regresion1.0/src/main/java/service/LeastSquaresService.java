package service;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import repository.FileRepo;

import java.util.ArrayList;

public class LeastSquaresService {

    FileRepo repo;

    public LeastSquaresService(FileRepo repo){
        this.repo = repo;
    }

    public double[][] fromMatrixToArray(){
        double[][] matrix = new double[repo.getDataMatrixNumberOfRows()][repo.getDataMatrixNumberOfColumns()+1];
        for(int i=0;i<repo.getDataMatrixNumberOfRows();i++){
            matrix[i][0] = 1;
            for(int j=0;j<repo.getDataMatrixNumberOfColumns();j++){
                matrix[i][j+1] = repo.getDataValue(i,j);
            }
        }
        return matrix;
    }

    public double[][] getRezultsArray(Integer resultColumn){
        double[][] matrix = new double[repo.getResultMatrixNumberOfRows()][1];
        for(int i=0;i<repo.getDataMatrixNumberOfRows();i++){
            matrix[i][0] = repo.getResultValue(i,resultColumn);
        }
        return matrix;
    }

    public ArrayList<Double> solve(Integer resultColumn){
        double[][] data = fromMatrixToArray();
        double[][] results = getRezultsArray(resultColumn);
        RealMatrix regressors =  MatrixUtils.createRealMatrix(data);
        RealMatrix transposedRegressors = regressors.transpose();
        RealMatrix responses =  MatrixUtils.createRealMatrix(results);
        RealMatrix result =  MatrixUtils.inverse(transposedRegressors.multiply(regressors)).multiply(transposedRegressors).multiply(responses);
        double[][] factor = result.getData();
        ArrayList<Double> factorAsArrayList = new ArrayList<>();
        for(int i=0;i<factor.length;i++){
            factorAsArrayList.add(factor[i][0]);
        }
        return factorAsArrayList;

    }
}
