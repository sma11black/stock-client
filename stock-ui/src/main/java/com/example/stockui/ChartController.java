package com.example.stockui;

import com.example.stockclient.StockClient;
import com.example.stockclient.StockPrice;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static java.lang.String.valueOf;
import static javafx.collections.FXCollections.observableArrayList;

@Component
public class ChartController {

    @FXML
    public LineChart<String, Double> chart;
    private StockClient stockClient;

    public ChartController(StockClient stockClient) {
        this.stockClient = stockClient;
    }

    @FXML
    public void initialize() {
        var priceSubscriber1 = new PriceSubscriber("SYMBOL1", stockClient);
        var priceSubscriber2 = new PriceSubscriber("SYMBOL2", stockClient);

        ObservableList<Series<String, Double>> data = observableArrayList();
        data.add(priceSubscriber1.getSeries());
        data.add(priceSubscriber2.getSeries());
        chart.setData(data);

    }

    private static class PriceSubscriber implements Consumer<StockPrice> {
        private final Series<String, Double> series;
        private final ObservableList<Data<String, Double>> seriesData = observableArrayList();

        private PriceSubscriber(String symbol, StockClient stockClient) {
            series = new Series<>(symbol, seriesData);
            stockClient.pricesFor(symbol)
                    .subscribe(this);
        }

        @Override
        public void accept(StockPrice stockPrice) {
            Platform.runLater(() ->
                    seriesData.add(new Data<>(valueOf(stockPrice.getTime().getSecond()),
                            stockPrice.getPrice()))
            );
        }

        private Series<String, Double> getSeries() {
            return series;
        }
    }
}
