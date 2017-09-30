package com.kangren.subtitles;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字幕控制类 Created by kangren on 2017/9/25.
 */

public class SubTitleUtils
{
    /**
     * 将String格式时间转换为毫秒时间
     * 
     * @param date
     * @return
     */
    public static long dateFormat(String date)
    {
        if (date == null)
        {
            return -1;
    }
        else
        {
            String[] temp = date.split(":");
            if (temp.length != 3)
            {
                return -1;
            }
            else
            {
                long hour = Integer.parseInt(temp[0]) * 60L * 60 * 1000;
                long minute = Integer.parseInt(temp[1]) * 60L * 1000;
                temp[2] = temp[2].replace(",", ".");
                long second = Math.round(Double.parseDouble(temp[2]) * 1000);
                return hour + minute + second;
            }
        }
    }
}
