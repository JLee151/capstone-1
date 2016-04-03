package application;

import java.util.*;
import java.sql.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.collections.*;

public class Main extends Application {
	ArrayList<day> month = new ArrayList<day>();
	@Override
	public void start(Stage primaryStage) {
		try {
			PLU();
			primaryStage.setTitle("Product Code Optimization");
			BorderPane root = new BorderPane();
			root.setTop(Menu_Bar(primaryStage, root));
			root.setBottom(new Pane());
			root.setCenter(new Pane());
			root.getBottom().minHeight(200);
			Scene scene = new Scene(root,1000,500);
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			primaryStage.setX(primaryScreenBounds.getMinX());
			primaryStage.setY(primaryScreenBounds.getMinY());
			primaryStage.setWidth(primaryScreenBounds.getWidth());
			primaryStage.setHeight(primaryScreenBounds.getHeight());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public MenuBar Menu_Bar(Stage menuStage, BorderPane content) {
		MenuBar menu = new MenuBar();
		Menu menuFile = new Menu("File");
		menuFile.getItems().add(new MenuItem("New"));
		menuFile.getItems().add(new MenuItem("Open"));
		menuFile.getItems().add(new MenuItem("Save"));
		menuFile.getItems().add(new SeparatorMenuItem());
		MenuItem Exit = new MenuItem("Exit");
		menuFile.getItems().add(Exit);
		Exit.setOnAction(actionEvent -> Platform.exit());
		Menu menuEdit = new Menu("Edit");
		Menu menuView = new Menu("View");
		MenuItem Data = new MenuItem("Data");
		MenuItem Fullscreen = new MenuItem("Toggle Fullscreen");
		menuView.getItems().add(Data);
		GridPane centerLayout = new GridPane();
		centerLayout.addRow(0, makeChart());
		centerLayout.addRow(0, makeChart());
		Data.setOnAction(actionEvent -> {content.setCenter(centerLayout);((GridPane) content.getCenter()).setAlignment(Pos.CENTER);});
		menuView.getItems().add(Fullscreen);
		Fullscreen.setOnAction(actionEvent -> menuStage.setFullScreen(!menuStage.isFullScreen()));
		menu.getMenus().addAll(menuFile, menuEdit, menuView);
		return menu;
	}

	public Pane makeChart(/*File chartData*/) {
		Pane MyChart = new Pane();
		ObservableList<PieChart.Data> pieChartData =
				FXCollections.observableArrayList(
						new PieChart.Data("Grapefruit", 13),
						new PieChart.Data("Oranges", 25),
						new PieChart.Data("Plums", 10),
						new PieChart.Data("Pears", 22),
						new PieChart.Data("Apples", 30));
		final PieChart chart = new PieChart(pieChartData);
		chart.setTitle("Product Codes");
		MyChart.getChildren().add(chart);
		return MyChart;
	}

	public void PLU() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		ArrayList<String> dates = new ArrayList<String>();
		ArrayList<Integer> pluCodesPerDay = new ArrayList<Integer>();
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/JLee/Desktop/Product");
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT DISTINCT substr(creationTime,1,10) AS date FROM PLUtimes ORDER BY date ASC");
			while(rs.next()) {
				//System.out.println("PLU = " + rs.getString("PLU") + "\tTime = " + rs.getString("TimeDiff"));
				String date = rs.getString("date");
				dates.add(date);
			}
			for(int i = 0; i < dates.size(); i++) {
				rs = statement.executeQuery("SELECT DISTINCT PLU FROM PLUtimes WHERE substr(creationTime,1,10) = '" + dates.get(i) + "' ORDER BY PLU ASC" );
				while(rs.next()) {
					String plu = rs.getString("PLU");
					int pluI = Integer.parseInt(plu);
					pluCodesPerDay.add(pluI);
				}
				day currentDay = new day(pluCodesPerDay);
				currentDay.setDate(dates.get(i));
				month.add(currentDay);
				pluCodesPerDay.removeAll(pluCodesPerDay);
			}

			for(int k = 0; k < dates.size(); k++) {
				rs = statement.executeQuery("SELECT PLU, strftime('%m/%d/%Y', creationTime) AS date, " + 
						"strftime('%s', creationTime) - strftime('%s', substr(creationTime,1,10)) AS TimeDiff " +
						"FROM PLUtimes WHERE substr(creationTime,1,10) = '" + dates.get(k) + "' ORDER BY TimeDiff ASC");
				while(rs.next()) {
					String plu = rs.getString("PLU");
					int pluI = Integer.parseInt(plu);
					String seconds = rs.getString("TimeDiff");
					int secondsI = Integer.parseInt(seconds);
					month.get(k).updatePLU(pluI, secondsI);
				}
			}
			rs.close();
			statement.close();
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("TEST");
		for(int victor = 0; victor < month.size(); victor++) {
			//System.out.print("day "+month.get(victor).getDate()+": ");
			for(int i = 0; i < month.get(victor).pluCodes.size(); i++) {
				//System.out.print("Plu: "+month.get(victor).pluCodes.get(i).getPlu()+" ");
				for(int j = 0; j < month.get(victor).pluCodes.get(i).intervals.size(); j++) {
					if(month.get(victor).pluCodes.get(i).intervals.get(j).getCount() < 8)
					{
						month.get(victor).pluCodes.get(i).intervals.remove(j);
						j--;
						continue;
					}
					//System.out.print(" intervals: " + month.get(victor).pluCodes.get(i).intervals.get(j).getCount()+" ");
					//System.out.print(" average: " + month.get(victor).pluCodes.get(i).intervals.get(j).getAverage()+" ");
				}
				if(month.get(victor).pluCodes.get(i).intervals.isEmpty())
				{
					month.get(victor).pluCodes.remove(i);
					i--;
				}
			}
		//	System.out.println();
		}
		
		
		for(int victor = 0; victor < month.size(); victor++) {
			System.out.print("day "+month.get(victor).getDate()+": ");
			for(int i = 0; i < month.get(victor).pluCodes.size(); i++) {
				System.out.print("Plu: "+month.get(victor).pluCodes.get(i).getPlu()+" ");
				for(int j = 0; j < month.get(victor).pluCodes.get(i).intervals.size(); j++) {
					System.out.print(" intervals: " + month.get(victor).pluCodes.get(i).intervals.get(j).getCount()+" ");
					System.out.print(" average: " + month.get(victor).pluCodes.get(i).intervals.get(j).getAverage()+" ");
					System.out.print(" start: " + month.get(victor).pluCodes.get(i).intervals.get(j).getStart()+" ");
					System.out.print(" end: " + month.get(victor).pluCodes.get(i).intervals.get(j).getEnd()+" ");
				}
				System.out.println();
			}
			System.out.println();
		}
		
		
		/*try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/JLee/Desktop/Product");
			System.out.println("Connection successful");
			Statement statement = connection.createStatement();
			System.out.println("Statement created");
			ResultSet rs = statement.executeQuery("SELECT PLU, strftime('%m/%d/%Y', creationTime) AS date, " +
													"strftime('%s', creationTime) - strftime('%s', substr(creationTime,1,10)) AS TimeDiff " +
													"FROM PLUtimes ORDER BY TimeDiff ASC");
			System.out.println("ResultSet created");
			//ResultSet rs1 = statement.executeQuery("SELECT DISTINCT PLU, strftime('%Y-%d-%m', creationTime) as y FROM PLUtimes GROUP BY PLU, y");
			while(rs.next()) {
				//System.out.println("PLU = " + rs.getString("PLU") + "\tTime = " + rs.getString("TimeDiff"));
			}
			rs.close();
			statement.close();
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}*/
	}

	public static void main(String[] args) {
		launch(args);
	}
}