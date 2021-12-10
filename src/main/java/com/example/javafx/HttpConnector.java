package com.example.javafx;

import com.example.javafx.controller.PreferenceController;
import com.example.javafx.model.UserAuthDto;
import coresearch.cvurl.io.constant.HttpStatus;
import coresearch.cvurl.io.model.Response;
import coresearch.cvurl.io.request.CVurl;
import org.json.JSONObject;

import java.util.Map;


/**
 * Weil das Einbinden von Libraries nicht geklappt habe, hab ich mir jetzt einen eigenen Connector geschrieben...
 * Dieser kann für Post und Get Requests verwendet oder später erweitert werden.
 */
public class HttpConnector {

    private static final String PREFIX = "http://localhost:9090/";

    public static boolean checkUserAUth(){
    Response<String> response = HttpConnector.get("user/check");

    // TODO evtl in 401 umändern falls Serverprobleme auftreten
    return response.status() == HttpStatus.OK;
    }

    //cVurl get und post
    public static Response<String> get(String urlString){
        CVurl cVurl = new CVurl();
        PreferenceController preferenceController = PreferenceController.getInstance();
        // Rausziehen des Tokens
        String token = preferenceController.getToken();
        // GET Request
        Response<String> response = cVurl.get(PREFIX + urlString)
                // TODO map später auslagern, content-type evtl anpassen
                .headers(Map.of(
                        "Content-Type","application/x-www-form-urlencoded",
                        "Authorization",token))
                .asString()
                .orElseThrow(RuntimeException::new);

        // Responsestatus pruefen und ggf. Token entfernen
        if (response.status() == HttpStatus.UNAUTHORIZED){
            preferenceController.clearToken();
        }

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
        PreferenceController preferenceController = PreferenceController.getInstance();
        String existingJwt = preferenceController.getToken();

        // POST
        // Wir uebergen das Token mit, auch wenn es erstmal leer ist, der Server macht damit nichts
        // Da der Endpunkt ohnehin gewhitelistet ist
        Response<String> result = cVurl.post(PREFIX + url)
                .headers(Map.of(
                        "Content-Type","application/json",
                        "Authorization", existingJwt))
                .body(userAuthDto)
                .asString().orElseThrow(RuntimeException::new);

        boolean isOk = result.status() == HttpStatus.OK;

        // token speichern, wenn User erfolgreich angelegt werden konnte
        if (isOk){
            JSONObject jsonObject = new JSONObject(result.getBody());
            String responseJWt = jsonObject.getString("jwttoken");
            preferenceController.setToken(responseJWt);
        }

        return isOk;
    }

}