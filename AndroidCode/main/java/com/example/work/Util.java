package com.example.work;

import com.example.work.item.item_comment;
import com.example.work.item.item_word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 杨旺旺 on 2017/12/8.
 */

public class Util {
    final String ServerUrl="http://192.168.137.207:8080/DataBaseTask/MyServlet";
    public  JSONObject getFirst(JSONObject cotent)
    {
        try {
            return cotent.getJSONArray("content").getJSONObject(0);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Boolean register(String userAccount,String userPassword)//注册
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("ReqType", "0");
                requestJson.put("account",userAccount);
                requestJson.put("password",userPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//成功
                {
                    return true;
                }
                else if(resCode.equals("201"))//失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public String login(String userAccount,String userPassword)//登陆
    {
        {
            HttpURLConnection connection = null;
            JSONObject requestJson = new JSONObject();
            try {
                try {
                    requestJson.put("ReqType", "1");
                    requestJson.put("account",userAccount);
                    requestJson.put("password",userPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                byte[] data = requestJson.toString().getBytes();//获得请求体
                URL url = new URL(ServerUrl); // 声明一个URL
                connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                connection.setConnectTimeout(8000); // 设置连接建立的超时时间
                connection.setReadTimeout(8000); // 设置网络报文收发超时时间
                connection.setUseCaches(false);               //使用Post方式不能使用缓存
                //设置请求体的类型是文本类型
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //设置请求体的长度
                connection.setRequestProperty("Content-Length", String.valueOf(data.length));
                //获得输出流，向服务器写入数据
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(data);
                outputStream.close();

                InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                try {
                    JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                    String resCode=resultJson.getString("resCode");
                    JSONArray temp=new JSONArray(resultJson.getString("content"));
                    String userId=temp.getJSONObject(0).getString("USID");
                    if(resCode.equals("200"))//成功
                    {
                        return userId;
                    }
                    else if(resCode.equals("201"))//账号不存在
                    {
                        return "0";
                    }
                    else//密码不对
                    {
                        return "-1";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "-1";
        }
    }

    public JSONArray upload_Papers()//下载试卷列表信息，返回到JSONArray中
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("ReqType", "2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//相册信息下载成功
                {
                    return new JSONArray(resultJson.getString("content"));
                }
                else if(resCode.equals("201"))//用户没有相册
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public JSONObject upload_One_papercontent(String UserId,String PaperId)//下载某一张试卷的试题选项和答案
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("ReqType", "4");
                requestJson.put("paperId", PaperId);
                requestJson.put("userId",UserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//相册信息下载成功
                {
                    if(resultJson.getString("isAnswered").equals("true"))//该用户做过这张卷纸了
                    {
                        JSONObject re=new JSONObject();
                        re.put("Done","Yes");
                        re.put("content",resultJson.getString("content"));
                        re.put("mypreAns",resultJson.getString("answer"));
                        return re;
                    }
                    else//第一次做
                    {
                        JSONObject re=new JSONObject();
                        re.put("Done","No");
                        re.put("content",resultJson.getString("content"));
                        return re;
                    }
                }
                else if(resCode.equals("201"))//用户没有相册
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public JSONArray upload_MyWords(String UserId)//下载一个用户的全部生词
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("ReqType", "6");
                requestJson.put("userId",UserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//相册信息下载成功
                {
                    return new JSONArray(resultJson.getString("content"));
                }
                else if(resCode.equals("201"))//用户没有相册
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public JSONArray upload_PaperWords(String PaperId)//下载一个试卷的全部单词
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("ReqType", "7");
                requestJson.put("paperId",PaperId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//相册信息下载成功
                {
                    return new JSONArray(resultJson.getString("content"));
                }
                else if(resCode.equals("201"))//用户没有相册
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public Boolean  add_one_word_to_MyWordbank(String userId, item_word newword)//添加一个新单词到用户词库
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("userId", userId);
                requestJson.put("ReqType", "8");
                requestJson.put("UWENG", newword.getSpell());
                requestJson.put("UWCHI", newword.getChinese());
                requestJson.put("UWTIME",newword.getAddTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//新建成功
                {
                    return true;
                } else if (resCode.equals("201")) //新建失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public JSONObject upload_User_Info(String UserId)//下载一个用户的信息
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("ReqType", "3");
                requestJson.put("userId",UserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println("+++++++++++++++++++"+requestJson.toString());
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//个人信息下载成功
                {
                    return getFirst(resultJson);
                    //return new JSONObject(resultJson.getString("content"));
                }
                else if(resCode.equals("201"))//错误
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public Boolean change_User_password(String UserId,String newPassword)//更改用户密码
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("ReqType", "10");
                requestJson.put("userId",UserId);
                requestJson.put("newPassword",newPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//成功
                {
                    return true;
                }
                else if(resCode.equals("201"))//失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public Boolean upload_User_Paper_Ans(String UserId,String PaperId,JSONArray ans_content)//上传用户完成一份卷子的答案，痕迹
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("userId", UserId);
                requestJson.put("ReqType", "11");
                requestJson.put("paperId",PaperId);
                requestJson.put("content", ans_content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//新建成功
                {
                    return true;
                } else if (resCode.equals("201")) //新建失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public Boolean upload_add_My_paper_bank(String UserId,String questionId,String myAns)//添加一道题到我的错题库
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("userId", UserId);
                requestJson.put("ReqType", "12");
                requestJson.put("QUESId", questionId);
                requestJson.put("QUESANS",myAns);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//添加成功
                {
                    return true;
                } else if (resCode.equals("201")) //添加失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public JSONArray upload_Comments(String questionId)//下载一个题下面的所有评论内容
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("ReqType", "13");
                requestJson.put("QUESId",questionId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//评论信息下载成功
                {
                    return new JSONArray(resultJson.getString("content"));
                }
                else if(resCode.equals("201"))//评论为空或者下载失败
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public Boolean nice_one_Comment(String userId,String commentId,String time)//添加一个赞
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("userId", userId);
                requestJson.put("ReqType", "14");
                requestJson.put("commentId", commentId);
                requestJson.put("niceTime", time);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//添加成功
                {
                    return true;
                } else if (resCode.equals("201")) //添加失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public String add_one_Comment(item_comment item,String questionId)//添加一条评论,返回评论Id
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("ReqType", "15");
                requestJson.put("COTIME",item.getCo_Time());
                requestJson.put("COCONTENT",item.getCo_Content());
                requestJson.put("COFROM",item.getCo_From());
                requestJson.put("COQUESID",questionId);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//添加成功
                {
                    JSONObject temp=getFirst(resultJson);
                    return temp.getString("COID");
                } else if (resCode.equals("201")) //添加失败
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public JSONArray upload_User_questionIds(String UserId)//下载某一用户的所有错题的Id
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("ReqType", "16");
                requestJson.put("userId", UserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//相册信息下载成功
                {
                    return new JSONArray(resultJson.getString("content"));
                }
                else if(resCode.equals("201"))//用户没有相册
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public JSONObject upload_one_myquestion_Info(String UserId,String questionId)//下载用户做过的一个题的相关内容
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("ReqType", "17");
                requestJson.put("userId",UserId);
                requestJson.put("QUESId",questionId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//评论信息下载成功
                {
                    return getFirst(resultJson);
                    //return new JSONObject(resultJson.getString("content"));
                }
                else if(resCode.equals("201"))//评论为空或者下载失败
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public Boolean delete_one_Comment(String commentId)//删除一条评论（只有评论者自己可以删除）
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("ReqType", "18");
                requestJson.put("COID",commentId);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//添加成功
                {
                    return true;
                } else if (resCode.equals("201")) //添加失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public Boolean delete_one_word(String userId, String wordEnglish)//删除一个单词
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("ReqType", "20");
                requestJson.put("userId",userId);
                requestJson.put("UWENG", wordEnglish);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//添加成功
                {
                    return true;
                } else if (resCode.equals("201")) //添加失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public Boolean delete_one_myquestion(String userId, String questionId)//删除一个错题
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("ReqType", "19");
                requestJson.put("userId",userId);
                requestJson.put("QUESId", questionId);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(ServerUrl); // 声明一个URL
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(8000); // 设置连接建立的超时时间
            connection.setReadTimeout(8000); // 设置网络报文收发超时时间
            connection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            try {
                JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//添加成功
                {
                    return true;
                } else if (resCode.equals("201")) //添加失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
