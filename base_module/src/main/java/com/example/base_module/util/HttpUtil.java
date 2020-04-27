package com.example.base_module.util;

import com.example.base_module.bean.PersonBean;
import com.example.base_module.bean.UserBean;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * http 工具类
 */
public class HttpUtil {


    public static void test(){
        ArrayList<PersonBean> userBeans = new ArrayList<>();
        ArrayList<PersonBean> normalList = new ArrayList<>();
        userBeans.stream().mapToInt(it -> it.getAge()).limit(5);
        ArrayList<ArrayList<PersonBean>> lists = new ArrayList<>();
        lists.stream().flatMap(it -> it.stream().filter(bean -> bean.getAge() > 16).collect(Collectors.toList()).stream())
                .collect(Collectors.toList());

        lists.stream().flatMap(it -> it.stream().filter(bean -> bean.getAge()>16)).collect(Collectors.toList());
        lists.stream().findFirst().ifPresent(it -> it.get(0));
//        lists.stream().findFirst().get()
//        userBeans.stream().
        userBeans.stream().sorted(Comparator.comparing((Function<PersonBean, String>) personBean -> null));
        userBeans.sort(Comparator.comparingInt(HttpUtil::applyTest).reversed());
        userBeans.stream().reduce(new PersonBean("李四",62),(o1,o2) -> {
           return new PersonBean("王五",55);
        });
        userBeans.stream().collect(Collectors.maxBy(Comparator.comparingInt(PersonBean::getAge))).get().getAge();
        userBeans.stream().collect(Collectors.groupingBy(it -> it.getAge()));
        Map<Integer,Set<PersonBean>> map = new HashMap<>();
        userBeans.stream().collect(Collectors.groupingBy(it -> it.getName(),HashMap<String,Set<PersonBean>>::new,Collectors.toSet()));
        userBeans.stream().collect(Collectors.groupingBy(it -> it.getName(),() -> new HashMap<String,Set<PersonBean>>(),Collectors.toSet()));


    }

    public static int compare(PersonBean o1,PersonBean o2){
        return 1;
    }

    public static int applyTest(PersonBean bean){
        return 1;
    }


    public static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtil.post(requestUrl, accessToken, contentType, params);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpUtil.post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpUtil.postGeneralUrl(url, contentType, params, encoding);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        System.err.println("result:" + result);
        return result;
    }
}
