package com.example.javafx;

import com.example.javafx.controller.PreferenceController;
import com.example.javafx.model.UserAuthDto;
import coresearch.cvurl.io.constant.HttpStatus;
import coresearch.cvurl.io.model.Response;
import coresearch.cvurl.io.request.CVurl;

import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;



/**
 * Weil das Einbinden von Libraries nicht geklappt habe, hab ich mir jetzt einen eigenen Connector geschrieben...
 * Dieser kann für Post und Get Requests verwendet oder später erweitert werden.
 */
public class HttpConnector {

    private static final String PREFIX = "http://localhost:9090/";

    public static boolean checkUserAUth(){
    Response<String> response = HttpConnector.getCvurl("user/check");

    // TODO evtl in 401 umändern falls Serverprobleme auftreten
    boolean validToken = response.status() == HttpStatus.OK;
    System.out.println(response.status());

    return validToken;
}
    //cVurl get und post
    public static Response<String> getCvurl(String urlString){
        CVurl cVurl = new CVurl();
        PreferenceController preferenceController = PreferenceController.getInstance();
        // Rausziehen des Tokens
        String token = preferenceController.getToken();

        System.out.println(token);

        preferenceController.setToken("");

        System.out.println(token);

        // GET Request
        Response<String> response = cVurl.get(PREFIX + urlString)
                // TODO map später auslagern, content-type evtl anpassen
                .headers(Map.of("Content-Type","application/x-www-form-urlencoded",
                        "Authorization",token))
                .asString()
                .orElseThrow(RuntimeException::new);

        // Responsestatus pruefen und ggf. Token entfernen
        if (response.status() == HttpStatus.UNAUTHORIZED){
            preferenceController.clearToken();
        }
        System.out.println(response);
        return response;
    }

    /** TODO: Wrapper Methode die aus einer Liste/Array eine Map macht und die in postCvurl eingibt
    // Wrapper Methode fuer die Headers damit nicht immer eine Map eingegeben werden muss
    public static String postCvurl2(String url, String... params){
        Map<String, String> map = new HashMap<>();
        return postCvurl(url,);
    }*/

    public static boolean post(String url, UserAuthDto userAuthDto) {
        CVurl cVurl = new CVurl();

        //POST
        Result result = cVurl.post(PREFIX + url)
                .queryParams(params)
                .asObject(Result.class);

        System.out.println("CVurl POST: " + result);

        return "";
    }

}