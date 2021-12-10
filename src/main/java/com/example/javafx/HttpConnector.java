package com.example.javafx;

import com.example.javafx.controller.PreferenceController;
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
import java.util.stream.Collectors;



/**
 * Weil das Einbinden von Libraries nicht geklappt habe, hab ich mir jetzt einen eigenen Connector geschrieben...
 * Dieser kann für Post und Get Requests verwendet oder später erweitert werden.
 */
public class HttpConnector {

    private static final String PREFIX = "http://localhost:9090/";
    private static PreferenceController preferenceController = PreferenceController.getInstance();

    //cVurl get und post
    public static Response<String> getCvurl(String urlString){
        CVurl cVurl = new CVurl();
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

    public static String postCvurl(String url, Map<String, String> params) {
        CVurl cVurl = new CVurl();

        //POST
        Result result = cVurl.post(PREFIX + url)
                .queryParams(params)
                .asObject(Result.class);

        System.out.println("CVurl POST: " + result);

        return "";
    }

// ----------------------------------------------------- alt

    public static String get(String urlString) {
        String response = "";
        try {
            HttpURLConnection http = connect(PREFIX + urlString, "GET");
            http.connect();
            response = getResponse(http);
        } catch (IOException e) {
            System.out.println("Exception!\n" + e.getMessage());
        }
        return response;
    }

    public static Map<String, String> post(String urlString, Object data) {
        Map<String, String> response = new HashMap<String, String>();

        try {
            // Verbindung vorbereiten
            HttpURLConnection http = connect(PREFIX + urlString, "POST");
            http.setDoOutput(true);

            // Daten im Body zusammenbauen
            String dataString = data.toString();
            byte[] out = dataString.getBytes(StandardCharsets.UTF_8);
            int length = out.length;
            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Timeouts
            http.setConnectTimeout(5000);
            http.setReadTimeout(5000);

            // sende Daten
            http.connect();
            OutputStream os = http.getOutputStream();
            os.write(out);

            // Erwarte Response -> könnten wir theoretisch für post auch über protokolle machen,
            // aber so können wir getReponse() für GET wiederverwenden. Evtl wollen wir ja später ohnehin
            // Fehlermeldungen werfen, dann geht das hierüber.
            String rsp = getResponse(http);
            http.disconnect();


            int responseCode = http.getResponseCode();
            response.put("code", String.valueOf(responseCode));
            response.put("msg", rsp);

        } catch (IOException e) {
            System.out.println("Exception! \n" + e.getMessage());
            response.put("code", "400");
            response.put("msg", "exception");
        }

        return response;
    }

    private static String getResponse(HttpURLConnection http) throws IOException {
        String response = new BufferedReader(
                new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        return response;
    }

    private static HttpURLConnection connect(String urlString, String method) throws IOException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) urlConnection;
        http.setRequestMethod(method);
        return http;
    }


}