package com.example.javaservice.Invoke;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.javaservice.Adapter.StudentAdapter;
import com.example.javaservice.Model.Student;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SendingData {
    private ReceivingData receivingData = new ReceivingData();

    private String UrlPostConnection(String Post_Url, JSONObject jsonObject) throws IOException {
        String response;
        URL url = new URL(Post_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(4 * 60 * 1000);
        conn.setConnectTimeout(4 * 60 * 1000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line).append("\n");
            }
            response = responseBuilder.toString();
        } else response = "";
        return response;
    }

    private String UrlPostConnection2(String Post_Url, HashMap<String, String> datamap) throws IOException {
        String response;
        URL url = new URL(Post_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(4 * 60 * 1000);
        conn.setConnectTimeout(4 * 60 * 1000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        writer.write(getPostDataString(datamap));
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line).append("\n");
            }
            response = responseBuilder.toString();
        } else response = "";
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    private String UrlGetConnection(String getUrl) throws IOException {
        String response;
        URL url = new URL(getUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(4 * 60 * 1000);
        conn.setConnectTimeout(4 * 60 * 1000);
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line);
            }
            response = responseBuilder.toString();
        } else response = "";
        return response;
    }

    // ------------------------------Insert--------------------------------------------------------------------------------------
    @SuppressLint("StaticFieldLeak")
    public class Insert_Records extends AsyncTask<String, String, String> {
        String record;
        Handler handler;
        JSONObject jsonObject;

        public Insert_Records(Handler handler, JSONObject jsonObject) {
            this.handler = handler;
            this.jsonObject = jsonObject;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                record = UrlPostConnection("http://192.168.100.48:8081/WebServiceExample/saveStudent", jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return record;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.getInsertResult(result, handler);
        }
    }

    // ------------------------------Delete--------------------------------------------------------------------------------------
    @SuppressLint("StaticFieldLeak")
    public class Delete_Records extends AsyncTask<String, String, String> {
        String record;
        Handler handler;
        JSONObject jsonObject;

        public Delete_Records(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("id", params[0]);
            try {
                record = UrlPostConnection2("http://192.168.100.48:8081/WebServiceExample/delete", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return record;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.getDeleteResult(result, handler);
        }
    }

    // ------------------------------Update--------------------------------------------------------------------------------------
    @SuppressLint("StaticFieldLeak")
    public class Update_Records extends AsyncTask<String, String, String> {
        String record;
        Handler handler;
        JSONObject jsonObject;

        public Update_Records(Handler handler, JSONObject jsonObject) {
            this.handler = handler;
            this.jsonObject = jsonObject;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                record = UrlPostConnection("http://192.168.100.48:8081/WebServiceExample/updateStudent", jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return record;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.getUpdateResult(result, handler);
        }
    }

    //-----------------------------------------------Fetch------------------------------------------------------------------------------
    @SuppressLint("StaticFieldLeak")
    public class Student_Details extends AsyncTask<String, String, String> {
        String response = "";
        ArrayList<Student> arrayList;
        StudentAdapter studentAdapter;
        Handler handler;

        public Student_Details(Handler handler, ArrayList<Student> arrayList, StudentAdapter studentAdapter) {
            this.handler = handler;
            this.arrayList = arrayList;
            this.studentAdapter = studentAdapter;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                response = UrlGetConnection("http://192.168.100.48:8081/WebServiceExample/all");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.getStudentDetails(result, handler, arrayList, studentAdapter);
        }
    }

}
