package com.demo.music.util;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <pre></pre>
 *
 * @author yanyl
 * @date 2019/1/22
 */
public class HttpUtil {
    private HttpUtil() {  }

    public static String get(String url) {
        Request request = new Request.Builder().url(url).get().build();
        Map<String, String> headers = new HashMap<>();
        headers.put("cache-control", "max-age=0");
        return exec(request, headers);
    }

    public static String post(String url, String params) {
        RequestBody body = RequestBody.create(MediaType
                .parse("application/x-www-form-urlencoded;charset=UTF-8"), params);
        Request request = new Request.Builder().url(url).post(body).build();
        Map<String, String> headers = new HashMap<>();
        headers.put("cache-control", "max-age=0");
        return exec(request, headers);
    }

    /**
     * 格式化参数
     * @param params
     * @return	name1=value1&name2=value2
     */
    public static String mapToString(Map<String, String> params){
        StringBuffer sb = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> iter = params.entrySet();
            for(Map.Entry<String, String> item : iter){
                sb.append(item.getKey()).append("=").append(item.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
        return null;
    }

    private static String exec(okhttp3.Request request, Map<String, String> headers) {
        try {
            if (headers != null && headers.size() > 0){
                headers.forEach((name, value) -> {
                    request.newBuilder().addHeader(name, value);
                });
            }
            TrustAllCerts trustAllCerts = new TrustAllCerts();
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{ trustAllCerts }, new java.security.SecureRandom());
            OkHttpClient client = new OkHttpClient().newBuilder().sslSocketFactory(sslContext.getSocketFactory(), trustAllCerts)
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(20, TimeUnit.MINUTES)
                    .readTimeout(20, TimeUnit.MINUTES).build();
            okhttp3.Response response = client.newCall(request).execute();
            if (!response.isSuccessful()){
                throw new RuntimeException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }
}
