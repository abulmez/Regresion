package repository;

import model.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileRepo {

    private Matrix<Double> dataMatrix = new Matrix<>(10000,16,0.0);
    private Matrix<Double> resultsMatrix = new Matrix<>(10000, 2,0.0);
    private Matrix<Double> testDataMatrix = new Matrix<>(10000,16,0.0);
    private Matrix<Double> testResultsMatrix = new Matrix<>(10000, 2,0.0);

    public FileRepo(String trainingDataFileName,String testingDataFileName){
        readDataFromFile(dataMatrix,resultsMatrix,trainingDataFileName);
        readDataFromFile(testDataMatrix,testResultsMatrix,testingDataFileName);
    }

    private void readDataFromFile(Matrix<Double> data,Matrix<Double> result,String fileName){
        try{
            Integer counter = 0;
            ClassLoader classLoader = getClass().getClassLoader();
            File file =
                    new File(classLoader.getResource(fileName).getFile());
            Scanner sc = new Scanner(file);
            sc.nextLine();
            String line;
            try {
                while ((line = sc.nextLine()) != null) {

                    String[] values = line.split(",");
                    for (int j = 6; j < 22; j++) {
                        data.set(Double.parseDouble(values[j]), counter, (j - 6));

                    }
                    result.set(Double.parseDouble(values[4]), counter, 0);
                    result.set(Double.parseDouble(values[5]), counter, 1);
                    counter += 1;

                }

            }
            catch (Exception e){
                data.setRows(counter);
                result.setRows(counter);
            }
        }
        catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        //System.out.println(dataMatrix.get(0,16)+" "+ dataMatrix.get(0,17));
    }

    public double getDataValue(Integer row, Integer column){
        return dataMatrix.get(row,column);
    }

    public int getDataMatrixNumberOfRows(){
        return dataMatrix.getRows();
    }

    public int getDataMatrixNumberOfColumns(){
        return dataMatrix.getColumns();
    }

    public double getResultValue(Integer row, Integer column){
        return resultsMatrix.get(row,column);
    }

    public int getResultMatrixNumberOfRows(){
        return resultsMatrix.getRows();
    }

    public int getResultMatrixNumberOfColumns(){
        return resultsMatrix.getColumns();
    }

    public double getTestingDataValue(Integer row, Integer column){
        return testDataMatrix.get(row,column);
    }

    public int getTestDataMatrixNumberOfRows(){
        return testDataMatrix.getRows();
    }

    public int getTestDataMatrixNumberOfColumns(){
        return testDataMatrix.getColumns();
    }

    public double getTestResultValue(Integer row, Integer column){
        return testResultsMatrix.get(row,column);
    }

    public int getTestResultMatrixNumberOfRows(){
        return testResultsMatrix.getRows();
    }

    public int getTestResultMatrixNumberOfColumns(){
        return testResultsMatrix.getColumns();
    }




}
