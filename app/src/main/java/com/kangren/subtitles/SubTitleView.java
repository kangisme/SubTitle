package com.kangren.subtitles;

import java.util.List;

import android.content.Context;
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

    private ISubTitleParser iSubTitleParser;

    // 字母列表
    private List<SubTitleModel> modelList;

    // 当前显示的语言
    private int language = 0;

    // 当前正在显示的字幕
    private SubTitleModel currentTitle;

    // 当前字幕索引
    private int currentIndex;

    // 字幕时间调整
    private int adjust;

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

    public void preAdjust()
    {
        adjust = adjust + 500;
    }

    public void delayAdjust()
    {
        adjust = adjust - 500;
    }

    public void resetAdjust()
    {
        adjust = 0;
    }

    public void setSubTitle(long currentPosition)
    {
        currentTitle = getCurrentTitle(currentPosition + adjust);
        if (currentTitle != null)
        {
            setText(currentTitle.getDialog(language));
        }
        else
        {
            setText("");
        }
        Log.d("kang", currentPosition + "");
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

        public SubTitleView build()
        {
            view.init();
            return view;
        }
    }
}
