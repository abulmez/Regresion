package ui;

import model.Matrix;
import repository.FileRepo;
import service.EvolutionaryAlgorithmService;
import service.LeastSquaresService;

import java.util.ArrayList;
import java.util.Scanner;

public class UI {

    EvolutionaryAlgorithmService evolutionaryAlgorithmService;
    LeastSquaresService leastSquaresService;
    Scanner sc;
    public UI(){
        sc = new Scanner(System.in);
    }

    /**
     * Displays the main menu of the application
     */
    public void displayMainMenu() {

        int x=2;
        FileRepo repo = new FileRepo("parkinsons_updrs_training.txt", "parkinsons_updrs_testing.txt");
        evolutionaryAlgorithmService = new EvolutionaryAlgorithmService(repo);
        leastSquaresService = new LeastSquaresService(repo);

        if(x==1) {



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
            ArrayList<Double> result = evolutionaryAlgorithmService.solve(generationNumber, populationSize, mutatianRate, resultColumn - 1);
            System.out.println("Coeficientii determinati:");
            for (Double rez : result) {
                System.out.print(rez + " ");
            }
            System.out.println();
            System.out.println();
            System.out.println("Valoarea reala | Valoarea Determinata | Eroare");
            System.out.println();
            Matrix<Double> evaluationResult = evolutionaryAlgorithmService.test(result, resultColumn - 1);
            for (int i = 0; i < evolutionaryAlgorithmService.repo.getTestResultMatrixNumberOfRows(); i++) {
                System.out.println(evaluationResult.get(i, 0) + " " + evaluationResult.get(i, 1) + " " + Math.abs(evaluationResult.get(i, 0) - evaluationResult.get(i, 1)));
            }
        }
        else {
            System.out.println("Dati rezultatul pentru care se va efectua regresia:");
            System.out.println("1.motor_UPDRS");
            System.out.println("2.total_UPDRS");
            int resultColumn = sc.nextInt();
            ArrayList<Double> result = leastSquaresService.solve(resultColumn-1);
            System.out.println();
            System.out.println("Valoarea reala | Valoarea Determinata | Eroare");
            System.out.println();
            Matrix<Double> evaluationResult = evolutionaryAlgorithmService.test(result, resultColumn - 1);
            for (int i = 0; i < evolutionaryAlgorithmService.repo.getTestResultMatrixNumberOfRows(); i++) {
                System.out.println(evaluationResult.get(i, 0) + " " + evaluationResult.get(i, 1) + " " + Math.abs(evaluationResult.get(i, 0) - evaluationResult.get(i, 1)));
            }

        }


    }
}
