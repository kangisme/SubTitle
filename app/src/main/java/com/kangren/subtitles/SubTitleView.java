package com.kangren.subtitles;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 自定义TextView实现字幕显示与获取
 * Created by kangren on 2017/9/21.
 */

public class SubTitleView extends AppCompatTextView
{
    // 字幕放置路径
    private String SUBTITLE_PATH;

    private Context mContext;

    // 回调接口
    private IPositionCallBack iPositionCallBack;

    private ISubTitleParser iSubTitleParser;

    //字母列表
    private List<SubTitleModel> modelList;

    // 当前显示的语言
    private int language = 0;

    // 当前正在显示的字幕
    private SubTitleModel currentTitle;

    // 当前字幕索引
    private int currentIndex;

    private Timer timer;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            long i = iPositionCallBack.getPlayerPosition();
            currentTitle = getCurrentTitle(i);
            if (currentTitle != null)
            {
                setText(currentTitle.getDialog(language));
            }
            else
            {
                setText("");
            }
            Log.d("kang", i + "");
        }
    };

    private SubTitleView(Context context)
    {
        super(context);
    }

    private SubTitleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private SubTitleView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 获取当前时间的字幕
     * 
     * @param currentTime
     * @return
     */
    private SubTitleModel getCurrentTitle(long currentTime)
    {
        if (currentIndex >= modelList.size())
        {
            return null;
        }
        SubTitleModel model = modelList.get(currentIndex);
        // 如果当前时间仍然在当前字幕时间范围内
        if (currentTime <= model.getEndTime() && currentTime >= model.getStartTime())
        {
            Log.d("kang", "当前字幕内");
            return model;
        }
        else
        {
            for (int i = 0; i < modelList.size(); i++)
            {
                // 第一次循环时直接查找下一个字幕
                if (i == 0)
                {
                    SubTitleModel nextModel = modelList.get(currentIndex + 1);
                    // 在两句字幕的中间，直接返回null
                    if (currentTime <= nextModel.getStartTime() && currentTime >= model.getEndTime())
                    {
                        Log.d("kang", "字幕间隔");
                        return null;
                    }
                    // 匹配到了下一句字幕
                    if (currentTime <= nextModel.getEndTime() && currentTime >= nextModel.getStartTime())
                    {
                        Log.d("kang", "下一条字幕");
                        currentIndex++;
                        return nextModel;
                    }
                }
                model = modelList.get(i);
                if (currentTime <= model.getEndTime() && currentTime >= model.getStartTime())
                {
                    Log.d("kang", "重新查找字幕");
                    currentIndex = i;
                    return model;
                }
            }
        }
        return null;
    }

    private void init()
    {
        modelList = iSubTitleParser.getSubTitle(SUBTITLE_PATH);
    }

    /**
     * 开始播放字幕
     */
    public void start()
    {
        if (timer == null)
        {
            timer = new Timer();
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 100);
        }
    }

    /**
     * 结束播放字幕
     */
    public void end()
    {
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN && currentTitle != null)
        {
            language = (language + 1) % currentTitle.getLanguageNum();
        }
        return super.onTouchEvent(event);
    }

    public static class Builder
    {
        private SubTitleView view;

        public Builder(Context context)
        {
            view = new SubTitleView(context);
            view.mContext = context;
        }

        public Builder(Context context, AttributeSet attrs)
        {
            view = new SubTitleView(context, attrs);
            view.mContext = context;
        }

        public Builder(Context context, AttributeSet attrs, int defStyleAttr)
        {
            view = new SubTitleView(context, attrs, defStyleAttr);
            view.mContext = context;
        }

        public Builder setPath(String path)
        {
            view.SUBTITLE_PATH = path;
            return this;
        }

        public Builder setSubTitleParser(ISubTitleParser iSubTitleParser)
        {
            view.iSubTitleParser = iSubTitleParser;
            return this;
        }

        public Builder setPositionCallBack(IPositionCallBack iPositionCallBack)
        {
            view.iPositionCallBack = iPositionCallBack;
            return this;
        }

        public SubTitleView build()
        {
            view.init();
            return view;
        }
    }
}
