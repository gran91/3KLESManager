/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kles.restful;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static resources.Resource.AK;
import static resources.Resource.AS;
import static resources.Resource.CK;
import static resources.Resource.DOMAIN;

/**
 *
 * @author JCHAUT
 */
public class RestFulOVH {

    private String ACCOUNT = "";
    private String METHOD = "";
    private String BODY = "";
    private String API = "";
    private StringBuffer response;
    private int responseCode;

    public RestFulOVH(String account) {
        ACCOUNT = account;
    }

    public JSONObject getAccountInformation() throws ParseException {
        METHOD = "GET";
        BODY = "";
        API = "/account/" + ACCOUNT;
        executeAPIRest();
        if (responseCode == 200) {
            System.out.println(response);
        }
        JSONObject map = (JSONObject) new JSONParser().parse(response.toString());
        return map;
    }

    public JSONObject getAccountUsage() throws ParseException {
        METHOD = "GET";
        BODY = "";
        API = "/account/" + ACCOUNT + "/usage";
        executeAPIRest();
        if (responseCode == 200) {
            System.out.println(response);
        }
        JSONObject map = (JSONObject) new JSONParser().parse(response.toString());
        return map;
    }

    public void changePassword(String pwd) {
        METHOD = "POST";
        JSONObject obj = new JSONObject();
        obj.put("password", pwd);
        BODY = obj.toJSONString();
        API = "/account/" + ACCOUNT + "/changePassword";
        executeAPIRest();
    }

    public boolean hasResponder() {
        METHOD = "GET";
        BODY = "";
        API = "/responder/" + ACCOUNT;
        executeAPIRest();
        return responseCode == 200;
    }

    public JSONObject getResponder() throws ParseException {
        METHOD = "GET";
        BODY = "";
        API = "/responder/" + ACCOUNT;
        executeAPIRest();
        JSONObject map = null;
        if (responseCode == 200) {
            System.out.println(response);
            map = (JSONObject) new JSONParser().parse(response.toString());
        }
        return map;
    }

    public JSONObject createResponder(String content, String from, String to) throws ParseException {
        JSONObject obj = new JSONObject();
        obj.put("account", ACCOUNT);
        obj.put("content", content);
        obj.put("copy", true);
        obj.put("copyTo", null);
        obj.put("from", from);
        obj.put("to", to);
        return createResponder(obj);
    }

    public JSONObject createResponder(JSONObject obj) throws ParseException {
        METHOD = "POST";
        BODY = obj.toJSONString();
        API = "/responder";
        executeAPIRest();
        JSONObject map = (JSONObject) new JSONParser().parse(response.toString());
        return map;
    }

    public JSONObject changeResponder(String content, String from, String to) throws ParseException {
        JSONObject obj = new JSONObject();
        obj.put("content", content);
        obj.put("from", from);
        obj.put("to", to);
        return changeResponder(obj);
    }

    public JSONObject changeResponder(JSONObject obj) throws ParseException {
        METHOD = "PUT";
        BODY = obj.toJSONString();
        API = "/responder/" + ACCOUNT;
        executeAPIRest();
        if (responseCode == 200) {
            return null;
        } else {
            JSONObject map = (JSONObject) new JSONParser().parse(response.toString());
            return map;
        }
    }

    public JSONObject deleteResponder() throws ParseException {
        METHOD = "DELETE";
        BODY = "";
        API = "/responder/" + ACCOUNT;
        executeAPIRest();
        return (JSONObject) new JSONParser().parse(response.toString());
    }

    private void executeAPIRest() {
        try {
            URL QUERY = new URL("https://eu.api.ovh.com/1.0/email/domain/" + DOMAIN + API);
            long TSTAMP = new Date().getTime() / 1000;

            String toSign = AS + "+" + CK + "+" + METHOD + "+" + QUERY + "+" + BODY + "+" + TSTAMP;
            String signature = "$1$" + HashSHA1(toSign);

            HttpURLConnection req = (HttpURLConnection) QUERY.openConnection();
            req.setRequestMethod(METHOD);
            req.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            req.setRequestProperty("Accept-Charset",    "utf-8");
            req.setRequestProperty("X-Ovh-Application", AK);
            req.setRequestProperty("X-Ovh-Consumer", CK);
            req.setRequestProperty("X-Ovh-Signature", signature);
            req.setRequestProperty("X-Ovh-Timestamp", "" + TSTAMP);

            if (!BODY.isEmpty()) {
                req.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(req.getOutputStream());
                wr.writeBytes(BODY);
                wr.flush();
                wr.close();
            }

            String inputLine;
            BufferedReader in;
            responseCode = req.getResponseCode();
            if (responseCode == 200) {
                in = new BufferedReader(new InputStreamReader(req.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(req.getErrorStream()));
            }

            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

/Affichage du résultat
            System.out.println(response.toString());

        } catch (MalformedURLException e) {
            final String errmsg = "MalformedURLException: " + e;
        } catch (IOException e) {
            final String errmsg = "IOException: " + e;
        }
    }

    public static String HashSHA1(String text) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (NoSuchAlgorithmException e) {
            final String errmsg = "NoSuchAlgorithmException: " + text + " " + e;
            return errmsg;
        } catch (UnsupportedEncodingException e) {
            final String errmsg = "UnsupportedEncodingException: " + text + " " + e;
            return errmsg;
        }
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public String getAccount() {
        return ACCOUNT;
    }

    public StringBuffer getResponse() {
        return response;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
