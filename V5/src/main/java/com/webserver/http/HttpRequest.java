package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示客户端发送过来的一个HTTP请求内容
 * 每个请求由三部分组成：
 * 请求行、消息头、消息正文
 */
public class HttpRequest {
    //请求行相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议版本

    //消息头相关信息
    private Map<String,String> headers=new HashMap<>();
    //消息正文相关信息

    private Socket socket;

    /**
     * HttpRequest的实例化过程就是解析请求的过程
     * @param socket
     */
    public HttpRequest(Socket socket){
        this.socket=socket;
        //1.解析请求行
        parseRequestLine();
        //2.解析消息头
        parseHeadlers();
        //3.解析消息正文
        parseContent();
    }
    //解析一个请求的三步骤：
    //1：解析请求行
    private void parseRequestLine(){
        System.out.println("HttpRequest:开始解析请求行...");
        try{
            String line=readLine();
            System.out.println("请求行:"+line);
            //http://localhost:8088/index.html
            //将请求行按照空格拆分为三部分，并分别赋值给上述变量
            String[]data=line.split("\\s");
            method=data[0];
            /*
              下面的代码可能在运行后浏览器发送请求拆分时，在这里赋值给uri出现
              字符串下标越界异常，这是由于浏览器发送了空请求，原因与常见错误5一样。
             */
            uri=data[1];
            protocol=data[2];
            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("HttpRequest:请求行解析完成");
    }
    //2：解析消息头
    private void parseHeadlers(){
        System.out.println("HttpRequest:开始解析消息头...");
        try{
            //下面读取每一个消息头，将消息头的名字作为key，消息头的值作为value保存到headers中
            while (true) {
                String line = readLine();
                //读取消息头时，如果只读取到了回车加换行符就应当停止读取e
                if (line.isEmpty()){//readLine单独读取CRLF返回值应当是空字符串
                    break;
                }
                System.out.println("消息头:" + line);
                //将消息头按照冒号空格拆分并存入到headers
                String[] data=line.split(":\\s");
                headers.put(data[0],data[1]);
            }
            System.out.println("headers:"+headers);
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("HttpRequest:消息头解析完成");
    }
    //3：解析消息正文
    private void parseContent(){
        System.out.println("HttpRequest:开始解析消息正文...");

        System.out.println("HttpRequest:消息正文解析完成");
    }

    public String readLine() throws IOException {
        /*
          当socket对象相同时，无论调用多少次getInputStream方法，获取回来的输入流总是
          同一个流。输出流也是一样的。
         */
        InputStream in=socket.getInputStream();
        int d;
        char cur=' ';//表示本次读取到的字符
        char pre=' ';//表示上次读取到的字符
        StringBuilder builder=new StringBuilder();
        while((d=in.read())!=-1){
            cur=(char)d;//本次读取到的字符
            if (pre==13&&cur==10){
                break;
            }
            builder.append(cur);
            pre=cur;
        }
        return builder.toString().trim();//用trim()删除最后拼上的回车符
    }

}















