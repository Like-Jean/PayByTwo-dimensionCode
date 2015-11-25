package com.logic.util;

/**
 * Created by zoson on 3/15/15.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private String Website = "http://120.25.206.58";
    private String Required = "";
    //protected static HttpRequest instance;
    private HttpListener listener;
    private HttpImgListener httpImgListener;
    private String message;
    private String data;
    private String api;
    private String param;
    private Bitmap bit;
    private int tag;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            System.out.println("handleinvoke");
            switch (msg.what){
                case 0x0:listener.succToRequire(message, data);break;
                case 0x1:listener.failToRequire(message,data);break;
                case 0x2:listener.netWorkError(message,data);break;
                case 0x3:httpImgListener.succToImg(bit, tag); break;
                default:listener.netWorkError(message,data);
            }
        }
    };
//    public static HttpRequest getInstance(){
//        if(instance == null){
//            instance = new HttpRequest("http://120.25.206.58");
//        }
//        return instance;
//    }
//    protected HttpRequest(String website){
//        this.Website = website;
//    }

    public void sendGet(String api, String param) {
        this.api = api;
        this.param = param;
        System.out.println("sendGet");
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                BufferedReader in = null;
                Required = "";
                try {

                    String urlNameString = getUrl(HttpRequest.this.api, HttpRequest.this.param);
                    URL realUrl = new URL(urlNameString);
                    // �򿪺�URL֮�������
                    URLConnection connection = realUrl.openConnection();
                    // ����ͨ�õ���������
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("connection", "Keep-Alive");

                    // ����ʵ�ʵ�����
                    connection.connect();
                    // ��ȡ������Ӧͷ�ֶ�
                    Map<String, List<String>> map = connection.getHeaderFields();
                    // �������е���Ӧͷ�ֶ�
                    for (String key : map.keySet()) {
                        System.out.println(key + "--->" + map.get(key));
                    }
                    // ���� BufferedReader����������ȡURL����Ӧ
                    in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));

                    String line;
                    while ((line = in.readLine()) != null) {
                        Required += line;
                    }
                    System.out.println(Required);
                    JsonClass js = new JsonClass(Required);
                    message = js.getString("message");
                    data = js.getString("data");
                    Message msg = Message.obtain();

                    switch (js.getInt("status")){
                        case 0:msg.what = 0x0;break;
                        case 1:msg.what = 0x1;break;
                        case 2:msg.what = 0x2;break;
                        default:msg.what = 0x2;break;
                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = 0x2;
                    handler.sendMessage(msg);
                    System.out.println("����GET��������쳣��" + e);
                    e.printStackTrace();
                }
                // ʹ��finally�����ر�������
                finally {

                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }

            }
        }.start();

    }


    public void sendPost(String api, String param) {
        this.param = param;
        this.api = api;
        new Thread(){
            @Override
            public void run() {

                PrintWriter out = null;
                BufferedReader in = null;
                Required = "";
                try {
                    Looper.prepare();
                    URL realUrl = new URL(getUrl(HttpRequest.this.api,""));
                    // �򿪺�URL֮�������
                    URLConnection conn = realUrl.openConnection();
                    // ����ͨ�õ���������
                    conn.setRequestProperty("accept", "*/*");
                    conn.setRequestProperty("connection", "Keep-Alive");

                    // ����POST�������������������
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    // ��ȡURLConnection�����Ӧ�������
                    out = new PrintWriter(conn.getOutputStream());
                    // �����������
                    out.print(HttpRequest.this.param);
                    // flush������Ļ���
                    out.flush();
                    System.out.println("conn="+conn.getHeaderFields());
                    // ����BufferedReader����������ȡURL����Ӧ
                    in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        Required += line;
                    }
                    System.out.println(Required);
                    JsonClass js = new JsonClass(Required);
                    message = js.getString("message");
                    data = js.getString("data");
                    //PersonalInfo personalInfo = PersonalInfo.getInstance();
                    //personalInfo.setUid(Integer.parseInt(data));
                    Message msg = Message.obtain();
                    switch (js.getInt("status")){
                        case 0:msg.what = 0x0;break;
                        case 401:msg.what = 0x1;break;
                        case 404:msg.what = 0x2;break;
                        default:msg.what = 0x2;break;
                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = 0x2;
                    handler.sendMessage(msg);
                    System.out.println("���� POST ��������쳣��"+e);
                    e.printStackTrace();

                }
                //ʹ��finally�����ر��������������
                finally{

                    try{
                        if(out!=null){
                            out.close();
                        }
                        if(in!=null){
                            in.close();
                        }
                    }
                    catch(IOException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }.start();


    }
    protected String getUrl(String api,String param){
        String url;
        if(api.equals("")){
            return Website;
        }else{
            if(param.equals("")){
                return  Website + "/" + api + "/";
            }else {
                return Website + "/" + api + "/?" +param;
            }
        }
    }
    public String getRequired(){
        return Required;
    }

    public void setListener(HttpListener listener){
        this.listener = listener;

    }
    public void setImgListener(HttpImgListener httpImgListener) {
        this.httpImgListener = httpImgListener;
    }

    public void getPhoto(String urlString, int _tag) {

        final String url = urlString;
        final int pos = _tag;
        new Thread() {
            @Override
            public void run() {
                try {
                    URL imgUrl = new URL(url);
                    // ʹ��HttpURLConnection������
                    HttpURLConnection urlConn = (HttpURLConnection) imgUrl
                            .openConnection();
                    urlConn.setDoInput(true);
                    urlConn.connect();
                    // ���õ�������ת����InputStream
                    InputStream is = urlConn.getInputStream();
                    // ��InputStreamת����Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Message msg = Message.obtain();
                    msg.what = 0x3;
                    bit = bitmap;
                    tag = pos;
                    handler.sendMessage(msg);
                    is.close();
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("[getNetWorkBitmap->]MalformedURLException");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("[getNetWorkBitmap->]IOException");
                    e.printStackTrace();
                }
            }
        }.start();



    }
}