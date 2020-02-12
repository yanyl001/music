package com.demo.music.controller;

import com.demo.music.model.FileModel;
import com.demo.music.util.DownloadUtil;
import com.demo.music.util.MusicSearch;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <p>
 *  前端控制器 客户端应用
 * </p>
 *
 * @author yanyl
 * @date 2018-08-13
 */
@Controller
public class MusicController {
	@RequestMapping(value = {"/", "index.html"})
	public String index(){
		return "index";
	}

	@ResponseBody
	@RequestMapping("list")
	public Object list(@RequestParam Map<String, Object> map){
		try {
			String keyword = "";
			if (StringUtils.isNotBlank((CharSequence) map.get("key"))){
				keyword = URLEncoder.encode((String) map.get("key"), "UTF-8");
			}
			String type = MapUtils.getString(map, "type");
			Long page = MapUtils.getLong(map, "page");
			Long pageSize = MapUtils.getLong(map, "limit");
			return MusicSearch.kugou(keyword, page, pageSize);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/download")
	public void download(HttpServletResponse response, String hash) throws IOException {
		FileModel fileModel = DownloadUtil.getKugou(hash);
		DownloadUtil.downloadURL(response, fileModel.getPlayUrl(), fileModel.getAudioName() + fileModel.getSuffix());
	}
	
}
