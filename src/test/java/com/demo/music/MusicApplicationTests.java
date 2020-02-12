package com.demo.music;

import com.demo.music.util.HttpUtil;
import org.junit.Test;

public class MusicApplicationTests {

    @Test
    public void contextLoads() {
        String url = "https://music.163.com/weapi/cloudsearch/get/web?csrf_token=";
        String params = "params=czCn3wQ8zo7zr75to4v7QnbgE0UBOQmG4vOqBdr7KLn1nsvig/rGNqjZDqx8An1PBWc+MioRUUPnkFRw1sY9HhZ5lcLCYk6KAXcJSga0Hy9VUZ0VdZTQodBKx0KzwDj2QVTq8+xdcXrCVoqhpklU6iRJ4YHu4d4SnQXjBejUei/IsGBCGXee3VrpG9JT7LLx0KWswbEvJo8naLQjcf4HE/DbnPHVyjGZ9pW+4rkbg18fGq3h09PmRDMraKdfcKvOiXNoKVKuhj2AIvjWBCNAmw==" +
                "&encSecKey=d7072361167f317e1d5de4ff733131dafcb2713c9b2efb2e6ab311bb404b2d91bc31e1cf5d64bfc3dd482b4e42d469b2987b91c84f5806f544e49195514d442806b30a0ebd77a69fd82cf32a12beee27a6575a16763f03dfcc63e41c18ffb232a1323eb8fcccdfd7aadbba4af695a4476d0c2cc43b79e30ebc2e74384d418ca6";
        String result = HttpUtil.post(url, null);
        System.out.println("result=" + result);
    }

}

