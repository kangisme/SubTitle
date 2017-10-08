package com.kangren.subtitles;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 字幕容器，包含编辑字幕按钮
 * Created by kangren on 2017/10/7.
 */

public class SubTitle extends LinearLayout
{
    // 默认字体大小
    private final float TEXT_SIZE = 22.0f;

    // 显示字幕的TextView
    private SubTitleView subTitleView;

    private Context mContext;

    // 装载按钮的容器
    private LinearLayout buttons;

    // 字幕调节按钮，分别是-0.5s、重置、+0.5s、删除字幕、退出编辑
    private Button pre;

    private Button recover;

    private Button delay;

    private Button delete;

    private Button exit;

    public SubTitle(Context context)
    {
        super(context);
        mContext = context;
        setOrientation(VERTICAL);
        buttons = new LinearLayout(context);
        buttons.setOrientation(HORIZONTAL);
        buttons.setVisibility(GONE);
    }

    public void loadSubTitle(String path)
    {
        ISubTitleParser parser;
        if (path.toUpperCase().endsWith("SRT"))
        {
            parser = new SRTParser();
        }
        else
        {
            parser = new ASSParser();
        }
        subTitleView = new SubTitleView.Builder(mContext)
                // 设置字幕文件路径
                .setPath(path)
                // 设置字幕解析器
                .setSubTitleParser(parser).build();
        subTitleView.setTextSize(TEXT_SIZE);
        this.addView(subTitleView, 0);

    }

    private void deleteSubTitle()
    {
        subTitleView.setText("");
        this.removeView(subTitleView);
        subTitleView = null;
        buttons.setVisibility(GONE);
    }

    /**
     * 进入编辑模式
     */
    public void edit()
    {
        // 第一次添加按钮
        if (subTitleView != null)
        {
            if (buttons.getParent() != this)
            {
                Log.d("kang", "第一次添加按钮，只能运行一次");
                pre = new Button(mContext);
                pre.setText("-0.5s");
                pre.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        subTitleView.delayAdjust();
                    }
                });
                buttons.addView(pre);
                recover = new Button(mContext);
                recover.setText("重置");
                recover.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        subTitleView.resetAdjust();
                    }
                });
                buttons.addView(recover);
                delay = new Button(mContext);
                delay.setText("+0.5s");
                delay.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        subTitleView.preAdjust();
                    }
                });
                buttons.addView(delay);
                exit = new Button(mContext);
                exit.setText("退出编辑");
                exit.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        buttons.setVisibility(GONE);
                    }
                });
                buttons.addView(exit);
                delete = new Button(mContext);
                delete.setText("删除字幕");
                delete.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        deleteSubTitle();
                    }
                });
                buttons.addView(delete);
                this.addView(buttons);
            }
            buttons.setVisibility(VISIBLE);
        }
        else
        {
            Toast.makeText(mContext, "当前没有字幕", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 退出编辑模式
     */
    private void exit()
    {
        buttons.setVisibility(GONE);
    }

    /**
     * 传递当前时间给TextView
     * 
     * @param currentPosition
     */
    public void setSubTitle(long currentPosition)
    {
        if (subTitleView != null)
        {
            subTitleView.setSubTitle(currentPosition);
        }
    }
}
