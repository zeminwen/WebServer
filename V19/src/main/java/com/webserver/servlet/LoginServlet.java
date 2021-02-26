package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;

public class LoginServlet extends HttpServlet {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {

    }

    public void doPost(HttpRequest request, HttpResponse response){
        System.out.println("LoginServlet:开始处理用户登录...");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        System.out.println(username+","+password);
        if (username==null||password==null){
            File file=new File("webapps/myweb/login_fail.html");
            response.setEntity(file);
            return;
        }
        try (
                RandomAccessFile raf=new RandomAccessFile("user.dat","r");
        ){
            for (int i=0;i<raf.length()/100;i++) {
                byte[] data = new byte[32];
                raf.seek(i * 100);
                raf.read(data);
                String name = new String(data, "utf-8").trim();
                if (username.equals(name)) {
                    raf.read(data);
                    String pwd = new String(data, "utf-8").trim();
                    if (password.equals(pwd)) {
                        File file = new File("webapps/myweb/login_success.html");
                        response.setEntity(file);
                        return;
                    }
                    break;
                }
            }
        File file=new File("webapps/myweb/login_fail.html");
        response.setEntity(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("LoginServlet:用户登录处理完成");
    }
}
