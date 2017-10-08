package com.kangren.subtitles;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 文件目录界面
 * Created by kangren on 2017/8/30.
 */

public class FileActivity extends Activity
{
    // 默认目录
    private final String DEFAULT_CATALOG = "storage/sdcard0/";

    private TextView currentCatalog;

    private ListView listFiles;

    // 上级目录按钮
    private Button back;

    // 当前目录
    private String currentCatalogS;

    private ArrayAdapter<String> adapter;

    private List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_file);
        init();
    }

    private void init()
    {
        // Activity刚启动时设置当前目录为sdcard目录
        currentCatalogS = DEFAULT_CATALOG;
        list = getFiles(currentCatalogS);
        currentCatalog = (TextView) findViewById(R.id.current_catalog);
        listFiles = (ListView) findViewById(R.id.list_file);
        back = (Button) findViewById(R.id.back);
        adapter = new ArrayAdapter<String>(FileActivity.this, android.R.layout.activity_list_item, android.R.id.text1,
                list);
        listFiles.setAdapter(adapter);
        currentCatalog.setText(currentCatalogS);
        // 为ListView列表项设置监听
        listFiles.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                currentCatalogS = currentCatalogS + "/" + list.get(position);
                if (currentCatalogS.toUpperCase().endsWith("SRT") || currentCatalogS.toUpperCase().endsWith("ASS"))
                {
                    Intent intent = getIntent();
                    intent.putExtra("path", currentCatalogS);
                    setResult(0, intent);
                    finish();
                }
                else
                {
                    list = getFiles(currentCatalogS);
                    adapter = new ArrayAdapter<String>(FileActivity.this, android.R.layout.activity_list_item,
                            android.R.id.text1, list);
                    listFiles.setAdapter(adapter);
                    currentCatalog.setText(currentCatalogS);
                }
            }
        });
        // 为上级目录按钮设置监听
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                File current_file = new File(currentCatalogS);
                File parent_file = current_file.getParentFile();
                currentCatalogS = parent_file.getAbsolutePath();
                list = getFiles(currentCatalogS);
                adapter = new ArrayAdapter<String>(FileActivity.this, android.R.layout.activity_list_item,
                        android.R.id.text1, list);
                listFiles.setAdapter(adapter);
                currentCatalog.setText(currentCatalogS);
            }
        });
    }

    private List<String> getFiles(String current_catalog)
    {
        List<String> list = new ArrayList<>();
        File current_file = new File(current_catalog);
        File[] files = current_file.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                if (pathname.isDirectory() || pathname.getName().toUpperCase().endsWith(".SRT")
                        || pathname.getName().toUpperCase().endsWith(".ASS"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        for (File temp : files)
        {
            list.add(temp.getName());
        }
        return list;
    }
}
