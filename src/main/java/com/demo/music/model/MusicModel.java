package com.demo.music.model;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre></pre>
 *
 * @author yanyl
 * @date 2019/4/9
 */
@Getter
@Setter
public class MusicModel {
    private String id;
    private String fileName;
    private String albumName;
    private String singerName;
    private String songName;
    private String duration;
    private String mvHash;
    /** 流畅音质 */
    private String m4aSound;
    /** 标准音质 */
    private String standardSound;
    /** 高品音质 */
    private String highSound;
    /** 无损FLAC */
    private String flacSound;
    /** 无损APE */
    private String apeSound;
}
