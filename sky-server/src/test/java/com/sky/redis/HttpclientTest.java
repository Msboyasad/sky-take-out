package com.sky.redis;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class HttpclientTest {


    /**
     * 发送get请求
     */

    @Test
    public void testGet() throws Exception {
        //1.创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2.创建请求对象
        HttpGet httpGet = new HttpGet("http://localhost:7777/user/shop/status");

        //3.发送请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //4.获取响应信息

        int code = response.getStatusLine().getStatusCode();
        log.info("状态码为{}", code);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        log.info("响应结果为{}", result);
        //5.关闭资源
        response.close();
        httpClient.close();
    }

    /**
     * post请求
     */
    @Test
    public void testPOST() throws Exception {
        //1.创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //2.创建请求对象
        HttpPost httpPost = new HttpPost("http://localhost:7777/admin/employee/login");
        //3，创建json对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "admin");
        jsonObject.put("password", "123456");
        //4.创建entity对象
        StringEntity entity = new StringEntity(jsonObject.toString());
        //4.1设置编码 和发送数据的格式
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/json");
        //4.2设置求生数据
        httpPost.setEntity(entity);
        //5.发送请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //6.获取响应结果
        int code = response.getStatusLine().getStatusCode();
        log.info("状态码为{}", code);
        HttpEntity entity2 = response.getEntity();
        String result = EntityUtils.toString(entity2);
        log.info("响应结果为{}", result);
        //7.关闭
        response.close();
        httpClient.close();
    }
}
