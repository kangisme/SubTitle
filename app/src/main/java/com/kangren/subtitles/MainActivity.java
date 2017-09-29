package com.kangren.subtitles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements IPositionCallBack
{
    private long startTime;

    private SubTitleView subTitleView;

    private FrameLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取程序开始的时间
        startTime = System.currentTimeMillis();
        content = (FrameLayout) findViewById(R.id.content);
        subTitleView = new SubTitleView.Builder(this)
                //设置字幕文件路径
                .setPath("/storage/sdcard0/Download/Spider-Man.Homecoming.srt")
                //设置字幕解析器
                .setSubTitleParser(new SRTParser())
                //设置时间回调接口
                .setPositionCallBack(this)
                .build();
        subTitleView.setTextSize(24);
        content.addView(subTitleView);
        subTitleView.start();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        subTitleView.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        subTitleView.end();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        subTitleView.end();
    }

    @Override
    public long getPlayerPosition()
    {
        return System.currentTimeMillis() - startTime;
    }
}
