package com.hosu.panes;

import java.util.ArrayList;
import java.util.List;

import com.hosu.application.HosuClient;
import com.hosu.css.Styling;
import com.hosu.helpers.StringBeautifier;
import com.hosu.settings.Settings;
import com.syntex.manga.queries.RequestQueryResults;
import com.syntex.manga.sources.Domain;

import customCharts.GraphMaker;
import customCharts.SmoothedChart;
import eu.hansolo.fx.charts.CircularPlot;
import eu.hansolo.fx.charts.CoxcombChart;
import eu.hansolo.fx.charts.data.ChartItem;
import eu.hansolo.fx.charts.data.PlotItem;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import io.netty.util.internal.ThreadLocalRandom;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StatsPage extends Pane {

	private final int SPACING = 15;
	
	@SuppressWarnings("unchecked")
	@Override
	public javafx.scene.layout.Pane get() {

		double offset = Settings.SIZE.getWidth() / 12;
		double width = Settings.SIZE.getWidth() - offset;
		double height = Settings.SIZE.getHeight() - offset;
		
		StackPane stats = new StackPane();
		stats.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
        stats.setMinSize(width, height);
        stats.setMaxSize(width, height);
        
        //eu.hansolo.fx.charts.

        HBox container = new HBox();
        container.setMinSize(width, height);
        container.setMaxSize(width, height);
        container.setSpacing(SPACING);
        
        //left panels
        VBox leftPanels = new VBox();
        leftPanels.setMinSize(width / 3.2, height);
        leftPanels.setMaxSize(width / 3.2, height);
        leftPanels.setSpacing(SPACING);
        
        StackPane topleft1 = new StackPane();
        topleft1.setMinSize(width / 3.2, height / 2.6);
        topleft1.setMaxSize(width / 3.2, height / 2.6);
        topleft1.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
        topleft1.setId("stats");
        topleft1.setAlignment(Pos.CENTER);
        
        StackPane topleft2 = new StackPane();
        topleft2.setMinSize(width / 3.2, height - topleft1.getMinHeight() - SPACING);
        topleft2.setMaxSize(width / 3.2, height - topleft1.getMinHeight() - SPACING);
        topleft2.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
        topleft2.setId("stats");
        topleft2.setAlignment(Pos.CENTER);

        /*
        StackPane topleft3 = new StackPane();
        topleft3.setMinSize(width / 3.2, height / 4);
        topleft3.setMaxSize(width / 3.2, height / 4);
        topleft3.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
        topleft3.setId("stats");
        */
        
        leftPanels.getChildren().add(topleft1);
        leftPanels.getChildren().add(topleft2);
        //leftPanels.getChildren().add(topleft3);
        
        //right panels
        
        double sumBetweenEdgesAndLeftNode = leftPanels.getMinWidth() + SPACING*2;
        
        VBox rightPanels = new VBox();
        rightPanels.setMinSize(width - sumBetweenEdgesAndLeftNode, height);
        rightPanels.setMaxSize(width - sumBetweenEdgesAndLeftNode, height);
        rightPanels.setSpacing(SPACING);
        
        StackPane topRight = new StackPane();
        topRight.setMinSize(rightPanels.getMinWidth(), height/6);
        topRight.setMaxSize(rightPanels.getMinWidth(), height/6);
        topRight.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
        topRight.setId("stats");
        topRight.setAlignment(Pos.CENTER);
        
        StackPane CenterRight = new StackPane();
        CenterRight.setMinSize(rightPanels.getMinWidth(), height/1.85);
        CenterRight.setMaxSize(rightPanels.getMinWidth(), height/1.85);
        CenterRight.getStylesheets().add(HosuClient.getInstance().getCssManager().getCss(Styling.HOSU));
        CenterRight.setId("stats");
        CenterRight.setAlignment(Pos.CENTER);
        
        HBox bottomRight = new HBox();
        bottomRight.setMinSize(rightPanels.getMinWidth(), height/6);
        bottomRight.setMaxSize(rightPanels.getMinWidth(), height/6);
        bottomRight.setSpacing(SPACING);

        VBox bottomRight1 = new VBox();
        bottomRight1.setMinSize(rightPanels.getMinWidth() / 3.142, height/4);
        bottomRight1.setMaxSize(rightPanels.getMinWidth() / 3.142, height/4);
        bottomRight1.setId("stats");
        
        VBox bottomRight2 = new VBox();
        bottomRight2.setMinSize(rightPanels.getMinWidth() / 3.142, height/4);
        bottomRight2.setMaxSize(rightPanels.getMinWidth() / 3.142, height/4);
        bottomRight2.setId("stats");
        
        VBox bottomRight3 = new VBox();
        bottomRight3.setMinSize(rightPanels.getMinWidth() / 3.142, height/4);
        bottomRight3.setMaxSize(rightPanels.getMinWidth() / 3.142, height/4);
        bottomRight3.setId("stats");
        
        bottomRight.getChildren().add(bottomRight1);
        bottomRight.getChildren().add(bottomRight2);
        bottomRight.getChildren().add(bottomRight3);
        
        rightPanels.getChildren().add(topRight);
        rightPanels.getChildren().add(CenterRight);
        rightPanels.getChildren().add(bottomRight);

        //container
        StackPane.setAlignment(leftPanels, Pos.TOP_LEFT);
        
        container.getChildren().add(leftPanels);
        container.getChildren().add(rightPanels);

        this.setHoverEffect(leftPanels, CenterRight, topRight, bottomRight1, bottomRight2, bottomRight3);
   
        stats.getChildren().add(container);
        stats.setAlignment(Pos.CENTER);
        
        //graphs

        //bottom last    
        List<ChartItem> queryItems = new ArrayList<>();
        
        Color queryShow = Color.web("#181c2e");
        
        for(Domain domain : Domain.values()) {
        	
        	int amount = 0;
        	
        	for(RequestQueryResults i : HosuClient.database.getCashedQueryResults()) {
        		if(i.getDomain() == domain) {
        			amount++;
        		}
        	}
        	
            ChartItem chart = new ChartItem(StringBeautifier.beautifier(domain.name()), amount);
            chart.setAnimated(true);
            chart.setFill(queryShow);
            chart.setAnimationDuration(1);
            chart.setUnit("Queries");
            queryItems.add(chart);
            queryShow = queryShow.brighter();
        }
        
        CoxcombChart test = new CoxcombChart(queryItems);
       // CenterRight.getChildren().add(test);
         topleft1.getChildren().add(test);
        
        //topleft1
        List<Integer> score = this.getRandomInts(1, 10, 30);

        final NumberAxis xAxis = new NumberAxis(1, score.size()-1, 1);
        final NumberAxis yAxis = new NumberAxis();
        
        final LineChart<Number,Number> ac = new LineChart<Number, Number>(xAxis,yAxis);
        ac.setTitle("Activity ( December )");
        ac.setVerticalGridLinesVisible(false);
        ac.setHorizontalGridLinesVisible(false);
        
		XYChart.Series<Number, Number> average = new Series<Number, Number>();
		average.setName("Usage");

        for(int x = 0; x < score.size(); x++) {
        	average.getData().add(new Data<Number, Number>(x, score.get(x)));
        }
        
        ac.setId("graph");
        
        //topleft1.getChildren().add(ac);
        CenterRight.getChildren().add(ac);
        
        ac.getData().addAll(average);
        
        //Center right
        SmoothedChart<String, Number> graph = new GraphMaker().get();
        graph.setMinSize(topRight.getMinWidth(), topRight.getMinHeight());
        graph.setMaxSize(topRight.getMinWidth(), topRight.getMinHeight());
        graph.setId("graph");
        topRight.getChildren().add(graph);
        
        //bottom three
        Gauge reading = GaugeBuilder
        .create()
	    //.title("Reading speed")
        .skinType(eu.hansolo.medusa.Gauge.SkinType.BAR)
        .prefSize(400, 400)
        .foregroundBaseColor(Color.web("#ebeefd"))
        .barBackgroundColor(Color.web("#262c49"))
        .decimals(2)
        .maxValue(100)
        .animationDuration(1000)
        .value(ThreadLocalRandom.current().nextInt(100))
        .gradientBarEnabled(true)
        .animated(true)
        .build();
        
        Gauge reading1 = GaugeBuilder
        .create()
	    //.title("Reading speed")
        .skinType(eu.hansolo.medusa.Gauge.SkinType.BAR)
        .prefSize(400, 400)
        .foregroundBaseColor(Color.web("#ebeefd"))
        .barBackgroundColor(Color.web("#262c49"))
        .decimals(2)
        .maxValue(100)
        .animationDuration(1000)
        .value(ThreadLocalRandom.current().nextInt(100))
        .gradientBarEnabled(true)
        .animated(true)
        .build();
        
        Gauge reading2 = GaugeBuilder
        .create()
	   // .title("Reading speed")
        .skinType(eu.hansolo.medusa.Gauge.SkinType.BAR)
        .prefSize(400, 400)
        .foregroundBaseColor(Color.web("#ebeefd"))
        .barBackgroundColor(Color.web("#262c49"))
        .decimals(2)
        .maxValue(100)
        .animationDuration(1000)
        .value(ThreadLocalRandom.current().nextInt(100))
        .gradientBarEnabled(true)
        .animated(true)
        .build();
        
        bottomRight1.getChildren().add(reading);
        bottomRight2.getChildren().add(reading1);
        bottomRight3.getChildren().add(reading2);
        
        eu.hansolo.fx.charts.CircularPlot plot = new CircularPlot();
        plot.addItem(new PlotItem("asd", 20, "aajskdhflkasdjhfklasdjhfklashdjfklj", Color.WHITE));
        plot.addItem(new PlotItem("asd1", 15, "aajskdhflkasdjhfklasdjhfklashdjfklj", Color.RED));
        plot.addItem(new PlotItem("asd2", 10, "aajskdhflkasdjhfklasdjhfklashdjfklj", Color.BLUE));
        plot.addItem(new PlotItem("asd3", 5, "aajskdhflkasdjhfklasdjhfklashdjfklj", Color.CYAN));
    
        //topleft2.getChildren().add(plot);
        
        /*
        FlowGridPane f = new FlowGridPane(9, 9);
        
        for(SkinType i : eu.hansolo.medusa.Gauge.SkinType.values()) {
            Gauge r = GaugeBuilder
                    .create()
            	    .title("Reading speed")
                    .skinType(i)
                    .foregroundBaseColor(Color.web("#ebeefd"))
                    .barBackgroundColor(Color.web("#262c49"))
                    .decimals(2)
                    .maxValue(100)
                    .animationDuration(1000)
                    .value(ThreadLocalRandom.current().nextInt(100))
                    .gradientBarEnabled(true)
                    .animated(true)
                    .build();
            r.setOnMousePressed((e) -> {
            	System.out.println(i.name());
            });
            f.getChildren().add(r);
        }
       */

        javafx.scene.layout.Pane settings = new SettingsPane().get();
        
        settings.setMinSize(topleft2.getMinWidth(), topleft2.getMinHeight());
        settings.setMaxSize(topleft2.getMinWidth(), topleft2.getMinHeight());
        settings.setId("");
        
        topleft2.getChildren().add(settings);
        
        
		return stats;
	}

	private void setHoverEffect(VBox leftPanels, Node... panes) {
        for(Node o : leftPanels.getChildren()) {
            o.setOnMouseEntered(i -> {
            	o.setId("stats-hover");
            	o.setCursor(Cursor.HAND);
            });
            o.setOnMouseExited(i -> {
            	o.setId("stats");
            	o.setCursor(Cursor.DEFAULT);
            });
        }
        
        for(Node i : panes.clone()) {
            i.setOnMouseEntered(o -> {
            	i.setId("stats-hover");
            	i.setCursor(Cursor.HAND);
            });
            i.setOnMouseExited(o -> {
            	i.setId("stats");
            	i.setCursor(Cursor.DEFAULT);
            });
        }
        
	}

	public List<Integer> getRandomInts(int min, int max, int amount) {
		List<Integer> values = new ArrayList<>(amount);
		for(int i = 0; i < amount; i++) {
			values.add(ThreadLocalRandom.current().nextInt(min, max));
		}
		return values;
	}
	
}
