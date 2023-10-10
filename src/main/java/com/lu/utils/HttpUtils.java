//package com.lu.utils;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.StringRequestEntity;
//import org.apache.http.Header;
//import org.apache.http.HttpEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class HttpUtils {
//    public static String sendPostWithJson(String url, String jsonStr, HashMap<String, String> headers) {
//        // 返回的结果
//        String jsonResult = "";
//        try {
//            HttpClient client = new HttpClient();
//            // 连接超时
////            client.getHttpConnectionManager().getParams().setConnectionTimeout(3 * 1000);
//            // 读取数据超时
////            client.getHttpConnectionManager().getParams().setSoTimeout(3 * 60 * 1000);
//            client.getParams().setContentCharset("UTF-8");
//            PostMethod postMethod = new PostMethod(url);
//
//            postMethod.setRequestHeader("content-type", headers.get("content-type"));
//
//            // 非空
//            if (null != jsonStr && !"".equals(jsonStr)) {
//                StringRequestEntity requestEntity = new StringRequestEntity(jsonStr, headers.get("content-type"), "UTF-8");
//                postMethod.setRequestEntity(requestEntity);
//            }
//            int status = client.executeMethod(postMethod);
//            if (status == HttpStatus.SC_OK) {
//                jsonResult = postMethod.getResponseBodyAsString();
//            } else {
//                throw new RuntimeException("接口连接失败！");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("接口连接失败！");
//        }
//        return jsonResult;
//    }
//
//    public static String sendPostWithJson1(String url, String jsonStr, HashMap<String, String> headers) throws IOException {
//        //创建httpclient对象
//        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
//
//        //创建HttpPost对象,设置url访问地址
//        HttpPost httpPost = new HttpPost("http://www.itcast.cn/");
//
//        //声明List集合,封装表单中的参数
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//        //设置请求地址
//        params.add(new BasicNameValuePair("txtUser", "itcast"));
//        params.add(new BasicNameValuePair("txtPass", "www.itcast.cn"));
//        params.add(new BasicNameValuePair("city", "北京"));
//        params.add(new BasicNameValuePair("birthday", "1999-01-24"));
//        params.add(new BasicNameValuePair("six", "1"));
//
//        //创建表单中的Entity对象，第一个参数就是封装好的表单数据，第二个参数就是编码
//        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "utf8");
//
//        //设置表单的Entity对象到Post请求中
//        httpPost.setEntity(urlEncodedFormEntity);
//
//        //发起post请求,并拿到response数据
//
//        CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
//
//        if (response.getStatusLine().getStatusCode() == 200) {
//            String context = EntityUtils.toString(response.getEntity());
//            System.err.println(context.length());
//        }
//        return "";
//    }
//
//    public static void test1() throws IOException {
//        //创建httpclient对象
//        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
//
//        //创建HttpPost对象,设置url访问地址
////        HttpPost httpPost = new HttpPost("https://biolab.gxu.edu.cn/AFP/config.jsp?https://biolab.gxu.edu.cn/AFP/config.jsp");
//        HttpPost httpPost = new HttpPost("https://biolab.gxu.edu.cn/AFP/config.jsp?startCompound=C00022&targetCompound=C00183&minatomic=2&solutionNumber=10&timeLimit=200&drawNpathways=10&searchingStrategy=non-conserved");
//
//        //声明List集合,封装表单中的参数
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//        //设置请求地址
//        params.add(new BasicNameValuePair("startCompound", "C00022"));
//        params.add(new BasicNameValuePair("targetCompound", "C00183"));
//        params.add(new BasicNameValuePair("minatomic", "2"));
//        params.add(new BasicNameValuePair("solutionNumber", "10"));
//        params.add(new BasicNameValuePair("timeLimit", "200"));
//        params.add(new BasicNameValuePair("drawNpathways", "10"));
//        params.add(new BasicNameValuePair("searchingStrategy", "non-conserved"));
//
//        //创建表单中的Entity对象，第一个参数就是封装好的表单数据，第二个参数就是编码
//        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "utf8");
//
//        //设置表单的Entity对象到Post请求中
//        httpPost.setEntity(urlEncodedFormEntity);
//
//        //发起post请求,并拿到response数据
//
//        CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
//        HttpEntity entity = response.getEntity();
//        if (response.getStatusLine().getStatusCode() == 302) {
//            // 跳转的目标地址是在 HTTP-HEAD 中的
//            Header header = response.getFirstHeader("location");
//            String newUrl = header.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
//            httpPost = new HttpPost(newUrl);
////            newUrl ="startCompound=C00022&targetCompound=C00183&minatomic=2&solutionNumber=10&timeLimit=200&drawNpathways=10&searchingStrategy=non-conserved";
//            response = closeableHttpClient.execute(httpPost);
//            String result = EntityUtils.toString(entity, "utf-8");
//            System.out.println(result);
//        }
//        String context = EntityUtils.toString(response.getEntity());
//        System.err.println(context.length());
//        EntityUtils.consume(entity);
//        //释放链接
//        response.close();
//
//    }
//
//    public static void main(String[] args) throws IOException {
//
//        HashMap<String, String> headers = new HashMap<>(3);
//        String requestUrl = "https://biolab.gxu.edu.cn/AFP/config.jsp";
//        String jsonStr = "{\"timeLimit\":\"200\",\"drawNpathways\":\"10\",\"minatomic\":\"2\",\"startCompound\":\"C00022\",\"searchingStrategy\":\"non-conserved\",\"targetCompound\":\"C00183\",\"solutionNumber\":\"10\"}";
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, String> map = new HashMap<>();
//        map.put("startCompound", "C00022");
//        map.put("targetCompound", "C00183");
//        map.put("minatomic", "2");
//        map.put("solutionNumber", "10");
//        map.put("timeLimit", "200");
//        map.put("drawNpathways", "10");
//        map.put("searchingStrategy", "non-conserved");
//        headers.put("content-type", "application/json");
//        String s = mapper.writeValueAsString(map);
//        System.out.println(s);
//        // 发送post请求
////        String resultData = HttpUtils.sendPostWithJson1(requestUrl, jsonStr, headers);
//        // 并接收返回结果
////        System.out.println(resultData);
//        test1();
//    }
//
//
//}
