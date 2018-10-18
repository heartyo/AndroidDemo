package com.webabcd.androiddemo;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webabcd.androiddemo.utils.Helper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "NormalExpandActivity222222";
    private ExpandableListView mExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mExpandableListView = (ExpandableListView) findViewById(R.id.expandable_list);



        String jsonString = Helper.getAssetString("site_map.json", this);
        Type type = new TypeToken<List<MainNavigationBean>>() { }.getType();
        Gson gson = new Gson();
        final ArrayList<MainNavigationBean> navigationBeanList = gson.fromJson(jsonString, type);




        final MainExpandableListAdapter adapter = new MainExpandableListAdapter(navigationBeanList);
        mExpandableListView.setAdapter(adapter);
        adapter.setOnGroupExpandedListener(new OnGroupExpandedListener() {
            @Override
            public void onGroupExpanded(int groupPosition) {
                expandOnlyOne(groupPosition);
            }
        });

        //  设置分组项的点击监听事件
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
               //  Log.d(TAG, "onGroupClick: groupPosition:" + groupPosition + ", id:" + id);
                // 请务必返回 false，否则分组不会展开
                return false;
            }
        });

        //  设置子选项点击监听事件
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // Toast.makeText(MainActivity.this, Constant.FIGURES[groupPosition][childPosition], Toast.LENGTH_SHORT).show();

                String className = "com.webabcd.androiddemo" + navigationBeanList.get(groupPosition).getNode().get(childPosition).getUrl();
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.webabcd.androiddemo", className);
                intent.setComponent(componentName);
                startActivity(intent);


                return true;
            }
        });
    }

    // 每次展开一个分组后，关闭其他的分组
    private boolean expandOnlyOne(int expandedPosition) {
        boolean result = true;
        int groupLength = mExpandableListView.getExpandableListAdapter().getGroupCount();
        for (int i = 0; i < groupLength; i++) {
            if (i != expandedPosition && mExpandableListView.isGroupExpanded(i)) {
                result &= mExpandableListView.collapseGroup(i);
            }
        }
        return result;
    }

}
