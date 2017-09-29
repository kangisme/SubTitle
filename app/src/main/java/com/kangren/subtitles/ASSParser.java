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
 * ASS格式字幕解析器 Created by kangren on 2017/9/29.
 */

public class ASSParser implements ISubTitleParser
{
    @Override
    public List<SubTitleModel> getSubTitle(String path)
    {
        String pattern = "Dialogue: \\d,[0-9]:\\d{2}:\\d{2}\\.\\d{2},[0-9]:\\d{2}:\\d{2}\\.\\d{2},Default,,0,0,0,,\\{\\\\blur3\\}.{0,}";
        Pattern r = Pattern.compile(pattern);
        List<SubTitleModel> list = new ArrayList<>();
        BufferedReader reader = null;
        try
        {
            FileInputStream fileInputStream = new FileInputStream(path);
            reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = reader.readLine()) != null)
            {
                Matcher m = r.matcher(line);
                if (m.matches())
                {
                    String[] temp = line.split(",");
                    SubTitleModel subTitleModel = new SubTitleModel();
                    subTitleModel.setStartTime(dateFormat(temp[1]));
                    subTitleModel.setEndTime(dateFormat(temp[2]));
                    subTitleModel.addDialog(temp[9].substring(8));
                    list.add(subTitleModel);
                }
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
