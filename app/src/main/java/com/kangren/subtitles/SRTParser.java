package com.kangren.subtitles;

import static com.kangren.subtitles.SubTitleUtils.dateFormat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SRT格式字幕解析器 Created by kangren on 2017/9/29.
 */

public class SRTParser implements ISubTitleParser
{
    @Override
    public List<SubTitleModel> getSubTitle(String path)
    {
        List<SubTitleModel> list = new ArrayList<>();
        BufferedReader reader = null;
        try
        {
            FileInputStream fileInputStream = new FileInputStream(path);
            reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                if (!line.equals(""))
                {
                    sb.append(line).append("@");
                    continue;
                }
                String[] tempStrs = sb.toString().split("@");
                // 排除字幕开始的空行和其它格式不符情况
                if (tempStrs.length < 3)
                {
                    sb.delete(0, sb.length());
                    continue;
                }
                SubTitleModel model = new SubTitleModel();
                // 匹配时间的正则表达式
                String pattern = "\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2},\\d{3}";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(tempStrs[1]);
                // 如果该条字幕的时间格式不对，跳过继续匹配
                if (!m.matches())
                {
                    continue;
                }
                String startTime = tempStrs[1].substring(0, 12);
                String endTime = tempStrs[1].substring(17, 29);
                model.setStartTime(dateFormat(startTime));
                model.setEndTime(dateFormat(endTime));
                // 可能有一条字幕，也可能两条及两条以上
                for (int i = 2; i < tempStrs.length; i++)
                {
                    model.addDialog(tempStrs[i]);
                }
                list.add(model);
                // 清空StringBuilder，不然影响读取下一条字幕
                sb.delete(0, sb.length());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return list;
    }
}
