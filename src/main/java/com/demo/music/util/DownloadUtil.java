package com.demo.music.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.music.ConsoleProgressBar;
import com.demo.music.model.FileModel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * <pre></pre>
 *
 * @author yanyl
 * @date 2019/1/22
 */
public class DownloadUtil {
    private static CloseableHttpClient HTTPCLIENT = HttpClients.createDefault();
    private static RequestConfig REQUESTCONFIG = RequestConfig.custom().setSocketTimeout(50000)
            .setConnectTimeout(50000).build();
    private static String KUGOU_URL = "https://wwwapi.kugou.com/yy/index.php?r=play/getdata&" +
            "callback=jQuery191027067069941080546_1546235744250&hash=%s&album_id=0&_=%s";

    public static FileModel getKugou(String hash){
        FileModel fileModel = new FileModel();
        if (StringUtils.isNotBlank(hash)){
            String url = String.format(KUGOU_URL, hash, System.currentTimeMillis());
            String result = HttpUtil.get(url);
            int index = result.indexOf("(");
            if (index > 0) {
                result = result.substring(index + 1, result.lastIndexOf(")"));
            }
            JSONObject data = JSON.parseObject(result);
            Integer status = data.getInteger("status");
            if (status == 1){
                String playUrl = data.getJSONObject("data").getString("play_url");
                String audioName = data.getJSONObject("data").getString("audio_name");
                String img = data.getJSONObject("data").getString("img");
                if (StringUtils.isNotBlank(playUrl)) {
                    String suffix = playUrl.substring(playUrl.lastIndexOf("."));
                    fileModel.setSuffix(suffix);
                }
                fileModel.setPlayUrl(playUrl);
                fileModel.setAudioName(audioName);
                fileModel.setImg(img);
            }
        }
        return fileModel;
    }

    /**
     * 下载文件
     * @param file
     * @param downloadName
     * @return
     */
    public static void downloadFile(HttpServletResponse response, File file, String downloadName) {
        if (file == null || !file.exists()) {
            return;
        }
        byte[] buffer = new byte[2048];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    new String(downloadName.getBytes("UTF-8"), "ISO8859-1"));

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bis, fis);
            file.delete();
        }
    }

    /**
     * 文件下载
     * @param response
     * @param url
     * @param downloadName
     */
    public static void downloadURL(HttpServletResponse response, String url, String downloadName){
        HttpGet get = new HttpGet(url);
        get.setConfig(REQUESTCONFIG);

        BufferedInputStream in = null;
        OutputStream out = null;
        try{
            //失败重试3次
            for(int i=0; i < 3; i++) {
                CloseableHttpResponse httpClient = HTTPCLIENT.execute(get);
                int statusCode = httpClient.getStatusLine().getStatusCode();
                if (statusCode == 500) {
                    continue;
                } else if (statusCode == 200) {
                    long fileSize = httpClient.getEntity().getContentLength();
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Length", String.valueOf(fileSize));
                    response.setHeader("Content-Disposition", "attachment;filename=" +
                            new String(downloadName.getBytes("UTF-8"), "ISO8859-1"));

                    in = new BufferedInputStream(httpClient.getEntity().getContent());
                    out = response.getOutputStream();
                    byte[] buffer = new byte[2048];
                    int copySize = -1;
                    long currentSize = 0;

                    ConsoleProgressBar progressBar = new ConsoleProgressBar(downloadName, 0, fileSize, 20);
                    while ((copySize = in.read(buffer, 0, 2048)) > -1) {
                        out.write(buffer, 0, copySize);
                        currentSize += copySize;
                        progressBar.show(currentSize);
                    }
                    out.flush();
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            get.releaseConnection();
            IOUtils.closeQuietly(in, out);
        }
    }
}
