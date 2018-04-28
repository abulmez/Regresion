import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import service.Service;
import ui.UI;


public class Main{

    public static void main(String[] args) {
        UI ui = new UI();
        ui.displayMainMenu();
    }
}
