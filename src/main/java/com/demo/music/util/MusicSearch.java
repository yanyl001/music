package com.demo.music.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.music.model.MusicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * <pre></pre>
 *
 * @author yanyl
 * @date 2019/4/13
 */
public class MusicSearch {
    public static PageUtil kugou(String keyword, Long page, Long pageSize){
        String url = "https://songsearch.kugou.com/song_search_v2?callback=jQuery1124030897607539681093_1555135225293&" +
                "keyword=" + keyword +
                "&page=" + page +
                "&pagesize=" + pageSize +
                "&userid=-1&" +
                "clientver=&platform=WebFilter&tag=&filter=2&iscorrection=1&privilege_filter=0&_=" + System.currentTimeMillis();
        String resultStr = HttpUtil.get(url);
        resultStr = resultStr.substring(resultStr.indexOf("(") + 1, resultStr.lastIndexOf(")"));
        JSONObject result = JSON.parseObject(resultStr);

        Long total = result.getJSONObject("data").getLong("total");
        JSONArray lists = result.getJSONObject("data").getJSONArray("lists");
        List<MusicModel> musicList = new ArrayList<>();
        IntStream.range(0, lists.size()).forEach(i ->  musicList.add(null));
        IntStream.range(0, lists.size()).parallel().forEach(i -> {
            JSONObject item = lists.getJSONObject(i);
            MusicModel music = new MusicModel();
            music.setId(item.getString("ID"));
            music.setFileName(item.getString("FileName"));
            music.setAlbumName(item.getString("AlbumName"));
            music.setSingerName(item.getString("SingerName"));
            music.setSongName(item.getString("SongName"));
            music.setDuration(item.getString("Duration"));
            music.setStandardSound(item.getString("FileHash"));
            musicList.set(i, music);
        });
        return new PageUtil(musicList, total, pageSize, page);
    }
}
