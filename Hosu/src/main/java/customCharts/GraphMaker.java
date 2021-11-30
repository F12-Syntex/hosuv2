package customCharts;


import java.util.ArrayList;
import java.util.List;

import io.netty.util.internal.ThreadLocalRandom;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;

public class GraphMaker {

    private XYChart.Series<String, Number> tweakedSeries1;
    private XYChart.Series<String, Number> tweakedSeries2;
    private SmoothedChart<String, Number>  tweakedChart;
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SmoothedChart<String, Number> get() {

        // Tweaked Chart data
        tweakedSeries1 = new XYChart.Series();
        tweakedSeries1.setName("Reading (%)");

        tweakedSeries2 = new XYChart.Series();
        tweakedSeries2.setName("Typing (%)");
        
        //0, 120, 125, 140, 130, 150, 253, 243, 394, 400
        
		List<Integer> score = this.getRandomInts(1, 10, 20);
		List<Integer> score2 = this.getRandomInts(1, 10, 20);
		
        for(int x = 0; x < score.size(); x++) {
        	int value = score.get(x);
        	tweakedSeries1.getData().add(new XYChart.Data(""+x, value));
        }
        
        for(int x = 0; x < score2.size(); x++) {
        	int value = score2.get(x);
        	tweakedSeries2.getData().add(new XYChart.Data(""+x, value));
        }
        // Tweaked Chart
        tweakedChart = new SmoothedChart<>(new CategoryAxis(), new NumberAxis());
        tweakedChart.getData().addAll(tweakedSeries1, tweakedSeries2);
        tweakedChart.setInteractive(true);

        // Set the chart type (AREA or LINE);
        tweakedChart.setChartType(SmoothedChart.ChartType.LINE);

        // Tweak the chart background
        RadialGradient gradient = new RadialGradient(0, 0, 0.5, 0.25, 0.5, true, CycleMethod.NO_CYCLE,
                                                     new Stop(0, Color.TRANSPARENT),
                                                     new Stop(1, Color.TRANSPARENT));
        tweakedChart.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // Tweak the chart plot background
        tweakedChart.getChartPlotBackground().setBackground(SmoothedChart.TRANSPARENT_BACKGROUND);

        // Tweak the legend
        tweakedChart.setLegendBackground(SmoothedChart.TRANSPARENT_BACKGROUND);
        tweakedChart.setLegendTextFill(Color.WHITE);

        // Tweak the axis
        tweakedChart.setXAxisTickLabelFill(Color.web("#7A808D"));
        tweakedChart.setYAxisTickLabelFill(Color.web("#7A808D"));
        tweakedChart.setAxisTickMarkFill(Color.TRANSPARENT);
        tweakedChart.setXAxisBorderColor(Color.TRANSPARENT);
        tweakedChart.setYAxisBorderColor(Color.TRANSPARENT);

        // Tweak the grid lines
        tweakedChart.getHorizontalGridLines().setStroke(Color.TRANSPARENT);
        LinearGradient verticalGridLineGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                                                                     new Stop(0, Color.TRANSPARENT),
                                                                     new Stop(0.35, Color.TRANSPARENT),
                                                                     new Stop(1, Color.web("#7A808D")));

        tweakedChart.getVerticalGridLines().setStroke(verticalGridLineGradient);
        tweakedChart.setHorizontalZeroLineVisible(false);
        tweakedChart.setSymbolsVisible(false);

        // Tweak series colors
        tweakedChart.setSeriesColor(tweakedSeries1, new LinearGradient(0, 0, 1, 0,
                                                                       true, CycleMethod.NO_CYCLE,
                                                                       new Stop(0, Color.web("#54D1FF")),
                                                                       new Stop(1, Color.web("#016AED"))),
                                    Color.TRANSPARENT);
        tweakedChart.setSeriesColor(tweakedSeries2, new LinearGradient(0, 0, 1, 0,
                                                                       true, CycleMethod.NO_CYCLE,
                                                                       new Stop(0, Color.web("#F9348A")),
                                                                       new Stop(1, Color.web("#EB123A"))),
                                    Color.TRANSPARENT);

        // Tweak series strokes
        Path tweakedSeries1Path = tweakedChart.getStrokePath(tweakedSeries1);
        Path tweakedSeries2Path = tweakedChart.getStrokePath(tweakedSeries2);

        tweakedSeries1Path.setStrokeWidth(4);
        tweakedSeries2Path.setStrokeWidth(4);

        tweakedSeries1Path.setStrokeLineCap(StrokeLineCap.ROUND);
        tweakedSeries2Path.setStrokeLineCap(StrokeLineCap.ROUND);
        InnerShadow lineLight  = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(255, 255, 255, 0.65), 3, 0, 0, 2);
        DropShadow  lineShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.45), 10, 0.0, 0.0, 15.0);
        lineShadow.setInput(lineLight);
        tweakedSeries1Path.setEffect(lineShadow);
        tweakedSeries2Path.setEffect(lineShadow);


        return tweakedChart;
	}

	public List<Integer> getRandomInts(int min, int max, int amount) {
		List<Integer> values = new ArrayList<>(amount);
		for(int i = 0; i < amount; i++) {
			values.add(ThreadLocalRandom.current().nextInt(min, max));
		}
		return values;
	}

}
