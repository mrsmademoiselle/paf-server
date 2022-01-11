package com.example.javafx.controller;

import com.example.javafx.service.GameService;
import com.example.javafx.service.helper.HttpConnector;
import coresearch.cvurl.io.model.Response;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.json.JSONObject;

public class HistoryController extends PapaController{

    @FXML
    Text wins;

    @FXML
    Text looses;

    @FXML
    Text avg;

    @FXML
    Text sum;

    @FXML
    protected void initialize(){
        Response<String> res = HttpConnector.get("user/history");
        JSONObject jo = new JSONObject(res.getBody().toString());
        sum.setText("Anzahl Spiele: " + jo.get("totalGames").toString());
        wins.setText("Davon gewonnen: " + jo.get("wins").toString());
        looses.setText("Davon verloren: " + jo.get("losses").toString());
        avg.setText("Durschnittliche Punktzahl: " + jo.get("averageMoves").toString());
    }
}
