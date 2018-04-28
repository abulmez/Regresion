package ui;

import model.Matrix;
import repository.FileRepo;
import service.Service;

import java.util.ArrayList;
import java.util.Scanner;

public class UI {

    Service service;
    Scanner sc;
    public UI(){
        sc = new Scanner(System.in);
    }

    /**
     * Displays the main menu of the application
     */
    public void displayMainMenu() {
        service = new Service(new FileRepo("parkinsons_updrs_training.txt","parkinsons_updrs_testing.txt"));
       // System.out.print("done");
        System.out.println("Dati numarul de generatii:");
        int generationNumber = sc.nextInt();
        System.out.println("Dati marimea populatiei:");
        int populationSize = sc.nextInt();
        System.out.println("Dati factorul de mutatie: (valoare intre 0 si 10)");
        int mutatianRate = sc.nextInt();
        System.out.println("Dati rezultatul pentru care se va efectua regresia:");
        System.out.println("1.motor_UPDRS");
        System.out.println("2.total_UPDRS");
        int resultColumn = sc.nextInt();
        ArrayList<Double> result = service.solve(generationNumber,populationSize,mutatianRate,resultColumn-1);
        System.out.println("Coeficientii determinati:");
        for(Double rez:result){
            System.out.print(rez+" ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Valoarea reala | Valoarea Determinata | Eroare");
        System.out.println();
        Matrix<Double> evaluationResult = service.test(result,resultColumn-1);
        for(int i=0;i<service.repo.getTestResultMatrixNumberOfRows();i++){
            System.out.println(evaluationResult.get(i,0)+ " " + evaluationResult.get(i,1) + " " + Math.abs(evaluationResult.get(i,0)-evaluationResult.get(i,1)));
        }


    }
}
