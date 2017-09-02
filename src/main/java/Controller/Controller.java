package Controller;

import Model.Money;
import Model.Quality;
import Model.moneyclasses.Dollar;
import Model.moneyclasses.Euro;
import Model.moneyclasses.Hryvnia;
import Model.moneyclasses.Ruble;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.*;

public class Controller {
    public Text usdBuy;
    public Text usdSell;
    public Text rubBuy;
    public Text rubSell;
    public Text eurBuy;
    public Text eurSell;
    public TextField moneyField;
    public ComboBox comboBoxFrom;
    public ComboBox comboBoxTo;
    public Text result;

    private DecimalFormat decimalFormat = new DecimalFormat("#######.##");

    private ObservableList<String> countryList = FXCollections.observableArrayList(                                     //лист значений для комбо-бокса
            "UAH", "USD", "EUR", "RUB"
    );

    @FXML
    public void initialize() throws IOException {
        comboBoxFrom.setValue("...");
        comboBoxTo.setValue("...");
        comboBoxFrom.setItems(countryList);
        comboBoxTo.setItems(countryList);
        update();
    }

    public void update() throws IOException {
        final URL url;
        url = new URL("https://kharkov.obmenka.ua");
        final URLConnection connection = url.openConnection();

        Map <String, Map<String, Double>> money = new HashMap<>();
        List <Double> list = new ArrayList<>();
        List<String> countryList = new ArrayList<>();
        countryList.add("USD");
        countryList.add("EUR");
        countryList.add("RUB");
        countryList.add("GBR");
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        connection.addRequestProperty("User-Agent", "Google Chrome/36");

        final Scanner reader = new Scanner(connection.getInputStream(), "UTF-8");

        while (reader.hasNextLine()) {
            String line = reader.nextLine();

            String temp;
            if(line.contains("\"price\" : \"")){
                temp = line.replaceAll("\"", "");
                temp = temp.replaceAll("[a-z]", "");
                temp = temp.replaceAll(":", "");
                temp = temp.replaceAll("\\s", "");
                temp = temp.replaceAll(",", "");
                list.add(Double.valueOf(temp));
            }
        }
        reader.close();

        int posCountry = 0;
        for (int i = 1; i < 8; i+=2) {
            Map<String, Double> innerMap = new HashMap<>();
            innerMap.put("buy", list.get(i-1));
            innerMap.put("sell", list.get(i));
            money.put(countryList.get(posCountry), innerMap);
            posCountry++;
        }

        Platform.runLater(() -> {
            usdBuy.setText(money.get("USD").get("buy").toString());
            usdSell.setText(money.get("USD").get("sell").toString());
            rubBuy.setText(money.get("RUB").get("buy").toString());
            rubSell.setText(money.get("RUB").get("sell").toString());
            eurBuy.setText(money.get("EUR").get("buy").toString());
            eurSell.setText(money.get("EUR").get("sell").toString());
        });
    }

    public void convert(){
        if (!comboBoxFrom.getValue().equals("...") && !comboBoxTo.getValue().equals("...") && !comboBoxFrom.getValue().equals(comboBoxTo.getValue())){
            switch (comboBoxFrom.getValue().toString()){
                case "UAH": {
                    if (isDigit(moneyField.getText()))
                        switch (comboBoxTo.getValue().toString()){
                            case "USD":
                                updateResult(1/Double.valueOf(usdBuy.getText()), Dollar.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal));
                                break;
                            case "RUB":
                                updateResult(1/Double.valueOf(rubBuy.getText()), Ruble.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal));
                                break;
                            case "EUR":
                                updateResult(1/Double.valueOf(eurBuy.getText()), Euro.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal));
                                break;
                        }
                    break;
                }
                case "RUB": {
                    if (isDigit(moneyField.getText()))
                        switch (comboBoxTo.getValue().toString()){
                            case "USD":{
                                Money hryvnia = Hryvnia.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal);
                                double temp = hryvnia.exchange(Double.valueOf(rubSell.getText()));
                                updateResult(1/Double.valueOf(usdBuy.getText()), Dollar.moneyFactory.createNewMoney(temp, Quality.Normal));
                                break;
                            }
                            case "EUR":{
                                Money hryvnia = Hryvnia.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal);
                                double temp = hryvnia.exchange(Double.valueOf(rubSell.getText()));
                                updateResult(1/Double.valueOf(eurBuy.getText()), Euro.moneyFactory.createNewMoney(temp, Quality.Normal));
                                break;
                            }
                            case "UAH":
                                updateResult(Double.valueOf(rubSell.getText()), Ruble.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal));
                                break;
                        }
                    break;
                }
                case "USD": {
                    if (isDigit(moneyField.getText()))
                        switch (comboBoxTo.getValue().toString()){
                            case "RUB":{
                                Money hryvnia = Hryvnia.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal);
                                double temp = hryvnia.exchange(Double.valueOf(usdSell.getText()));
                                updateResult(1/Double.valueOf(rubBuy.getText()), Ruble.moneyFactory.createNewMoney(temp, Quality.Normal));
                                break;
                            }
                            case "EUR":{
                                Money hryvnia = Hryvnia.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal);
                                double temp = hryvnia.exchange(Double.valueOf(usdSell.getText()));
                                updateResult(1/Double.valueOf(eurBuy.getText()), Euro.moneyFactory.createNewMoney(temp, Quality.Normal));
                                break;
                            }
                            case "UAH":
                                updateResult(Double.valueOf(usdSell.getText()), Hryvnia.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal));
                                break;
                        }
                    break;
                }
                case "EUR":{
                    if (isDigit(moneyField.getText()))
                        switch (comboBoxTo.getValue().toString()){
                            case "RUB":{
                                Money hryvnia = Hryvnia.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal);
                                double temp = hryvnia.exchange(Double.valueOf(eurSell.getText()));
                                updateResult(1/Double.valueOf(eurBuy.getText()), Ruble.moneyFactory.createNewMoney(temp, Quality.Normal));
                                break;
                            }
                            case "USD":{
                                Money hryvnia = Hryvnia.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal);
                                double temp = hryvnia.exchange(Double.valueOf(eurSell.getText()));
                                updateResult(1/Double.valueOf(eurBuy.getText()), Dollar.moneyFactory.createNewMoney(temp, Quality.Normal));
                                break;
                            }
                            case "UAH":
                                updateResult(Double.valueOf(eurSell.getText()), Hryvnia.moneyFactory.createNewMoney(Double.valueOf(moneyField.getText()), Quality.Normal));
                                break;
                        }
                }
            }
        }
    }

    private void updateResult(double course, Money money){
        if (money.canExchanging())
            Platform.runLater(() -> result.setText(String.valueOf(decimalFormat.format(money.exchange(course)))));
    }

    private boolean isDigit(String s){
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e){
            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UTILITY);
            Text text = (new Text(40, 40, "Not correct value...."));
            text.setStyle("-fx-text-fill: #3c7fb1");
            text.setTextAlignment(TextAlignment.CENTER);
            Group group = new Group(text);
            group.setStyle("-fx-background-color: #1d1d1d");
            Scene scene = new Scene(group);
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.show();
            return false;
        }
    }
}
