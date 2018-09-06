package handy.RssView;
	
import java.io.File;

import handy.rssarchive.config.GUIData;
import handy.rssarchive.config.MasterConfig;
import handy.rssarchive.file.FileUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//FXMLLoader loader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
			FXMLLoader loader = new FXMLLoader(new File("src" + FileUtil.getFileSep() + "MainLayout.fxml").toURI().toURL());
			
			Parent root = loader.load();
	        Scene scene = new Scene(root);
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
