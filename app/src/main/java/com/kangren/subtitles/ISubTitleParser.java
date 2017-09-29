package com.kangren.subtitles;

import java.util.List;

/**
 * 字幕解析器 Created by kangren on 2017/9/29.
 */

public interface ISubTitleParser
{
    List<SubTitleModel> getSubTitle(String path);
}
