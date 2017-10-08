package com.kangren.subtitles.utils;

/**
 * 工具类
 * Created by kangren on 2017/9/25.
 */

public class SubTitleUtils
{
    /**
     * 去除{\...}字幕样式定义
     * 
     * @param subtitle
     * @return
     */
    public static String stringFormat(String subtitle)
    {
        // return
        // Pattern.compile("\\{\\\\.{0,}?\\}").matcher(subtitle).replaceAll("");
        return subtitle.replaceAll("\\{\\\\.{0,}?\\}", "");
    }

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
