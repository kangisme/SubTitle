package com.kangren.subtitles;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity
{
    private long startTime;

    private SubTitle subTitle;

    private LinearLayout content;

    private Button load;

    private Button remove;

    private Handler handler = new Handler();

    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init()
    {
        // 获取程序开始的时间
        startTime = SystemClock.elapsedRealtime();
        content = (LinearLayout) findViewById(R.id.content);
        load = (Button) findViewById(R.id.load);
        remove = (Button) findViewById(R.id.remove);
        subTitle = new SubTitle(this);
        load.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(MainActivity.this, FileActivity.class), 0);
            }
        });
        remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                subTitle.edit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
        {
            if (requestCode == 0 && resultCode == 0)
            {
                String path = data.getStringExtra("path");
                subTitle.loadSubTitle(path);
                if (subTitle.getParent() != content)
                {
                    content.addView(subTitle);
                }
                runnable = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // 传入时间
                        subTitle.setSubTitle(getCurrentPosition());
                        handler.postDelayed(this, 500);
                    }
                };
                handler.postDelayed(runnable, 500);
            }
        }
    }

    private long getCurrentPosition()
    {
        return SystemClock.elapsedRealtime() - startTime;
    }

    @Override
    protected void onDestroy()
    {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
