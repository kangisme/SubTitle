package com.kangren.subtitles;

import java.util.ArrayList;
import java.util.List;

/**
 * 字幕格式 Created by kangren on 2017/9/20.
 */

public class SubTitleModel
{

    // 开始时间毫秒
    private long startTime;

    // 结束时间毫秒
    private long endTime;

//    // 类型
//    private String type;

    // 对话内容
    private List<String> dialog = new ArrayList<String>();

    public long getStartTime()
    {
        return startTime;
    }

    public void setStartTime(long startTime)
    {
        this.startTime = startTime;
    }

    public long getEndTime()
    {
        return endTime;
    }

    public void setEndTime(long endTime)
    {
        this.endTime = endTime;
    }

//    public String getType()
//    {
//        return type;
//    }
//
//    public void setType(String type)
//    {
//        this.type = type;
//    }

    public int getLanguageNum()
    {
        return dialog.size();
    }

    public String getDialog(int index)
    {
        return dialog.get(index);
    }

    public void addDialog(String dialog)
    {
        this.dialog.add(dialog);
    }
}
