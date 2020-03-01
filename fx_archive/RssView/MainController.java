package handy.RssView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;

import handy.rssarchive.config.ArticleInfo;
import handy.rssarchive.config.GUIData;
import handy.rssarchive.config.MasterConfig;
import handy.rssarchive.config.SiteConfig;
import handy.xml.ArticleAccessHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.beans.value.ObservableValue;

public class MainController {

	@FXML
	private ListView<String> authors;
	@FXML
	private ListView<String> sites;
	@FXML
	private ListView<String> articles;
	@FXML
	private TextArea article;
	@FXML
	private TextArea authorField;
	@FXML
	private TextArea urlField;
	@FXML
	private TextArea pubDateField;
	@FXML
	private TextField startDateRange;
	@FXML
	private TextField endDateRange;
	
	private final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
	public void initialize() {

		Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
		startDateRange.setText(df.format(cal.getTime()));
		
		// do initialization and configuration work...
		try {
			System.out.println("Executing");
			MasterConfig masterConfig = new MasterConfig(
					"config\\config.xml");

			GUIData gData = new GUIData(masterConfig, "C:\\Users\\The Constant Admin\\Desktop\\TestLander");
			gData.load();

			ObservableList<String> data = FXCollections.observableArrayList();
			data.addAll(gData.getAuthorsToArticles().keySet());

			authors.setItems(data);
			authors.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
						System.out.println(new_val);

						ObservableList<String> articlesList = FXCollections.observableArrayList();
						for (ArticleInfo aInfo : gData.getAuthorsToArticles().get(new_val)) {
							//articlesList.add(aInfo.title);
							addArticle(articlesList, aInfo);
						}
						articles.setItems(articlesList);
					});

			ObservableList<String> sitesList = FXCollections.observableArrayList();

			for (SiteConfig config : masterConfig.configs) {
				sitesList.add(config.name);
			}
			sites.setItems(sitesList);
			sites.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
						System.out.println(new_val);

						ObservableList<String> articlesList = FXCollections.observableArrayList();
						SiteConfig siteKey = null;
						for(SiteConfig config : masterConfig.configs){
							if(config.name.equals(new_val)){
								siteKey = config;
							}
						}
						for (ArticleInfo aInfo : gData.getAllMetaData().get(siteKey)) {
							//articlesList.add(aInfo.title);
							addArticle(articlesList, aInfo);
						}
						articles.setItems(articlesList);
					});

			articles.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
						article.setText(gData.getArticleNameToArticles().get(new_val).processedText);
						authorField.setText(gData.getArticleNameToArticles().get(new_val).author);
						urlField.setText(gData.getArticleNameToArticles().get(new_val).url);
						pubDateField.setText(gData.getArticleNameToArticles().get(new_val).pubDateStr);
					});

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void addArticle(ObservableList<String> articlesList, ArticleInfo aInfo){
		try{
			Date articleDate = ArticleAccessHelper.DATE_FORMAT.parse(aInfo.pubDateStr);
			Date startDate = null;
			Date endDate = null;
			boolean startOk = true;
			boolean endOk = true;
			try{
				startDate = df.parse(startDateRange.getText());
			}catch(Exception e){
				//No worries
			}
			try{
				endDate = df.parse(endDateRange.getText());
			}catch(Exception e){
				//No worries
			}
			
			if(startDate != null && articleDate.before(startDate)){
				startOk = false;
			}
			
			if(endDate != null && articleDate.after(endDate)){
				endOk = false;
			}
			
			if(startOk && endOk){
				articlesList.add(aInfo.title);
			}
		}catch(ParseException ex){
			articlesList.add(aInfo.title);
		}
		
	}
}
