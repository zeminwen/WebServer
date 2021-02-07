package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责与指定客户端进行HTTP交互
 * HTTP协议要求与客户端的交互规则采取一问一答的方式。因此，处理客户端交互以三步形式完成：
 * 1.解析请求(一问)
 * 2.处理请求
 * 3.发送响应(一答)
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        try{
            //1解析请求
            InputStream in=socket.getInputStream();
            //测试读取客户端发送过来的请求内容
            String line=readLine();
            System.out.println("请求行:"+line);
            String method;//请求方式
            String uri;//抽象路径
            String protocol;//协议版本


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

            //读取所有消息头
            Map<String,String>headers=new HashMap<>();
            //下面读取每一个消息头，将消息头的名字作为key，消息头的值作为value保存到headers中
            while (true) {
                line = readLine();
                //读取消息头时，如果只读取到了回车加换行符就应当停止读取e
                if (line.isEmpty()){//readLine单独读取CRLF返回值应当是空字符串
                    break;
                }
                System.out.println("消息头:" + line);
            }
            System.out.println("headers:"+headers);
            //2处理请求

            //3发送响应

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //处理完毕后与客户端断开连接
            try{
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
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
