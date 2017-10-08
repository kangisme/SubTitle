package com.kangren.subtitles.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 工具类：统一关闭Closeable接口实例
 * Created by kangren on 2017/10/5.
 */

public class CloseUtils
{
    private CloseUtils()
    {

    }

    public static void closeQuietly(Closeable closeable)
    {
        if (null != closeable)
        {
            try
            {
                closeable.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
