package com.winfo.wenjie.toolbar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //侧滑菜单的布局
    private DrawerLayout drawerLayout;
    //标题栏
    private Toolbar toolbar;
    private String[] items = {"首页", "动态", "设置"};
    private int imgIds[] = {R.mipmap.boat , R.mipmap.elect , R.mipmap.anji};
    //侧滑菜单的内容项
    private ListView listView;
    private MyAdapter adapter;
    //切换的页面
    private Fragment1 f1;
    private Fragment2 f2;
    private Fragment3 f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");//设置标题
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.left_drawer);

        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        adapter.setSelectedItem(0);//默认选中第一个item

        f1 = new Fragment1();

        mContent = f1;//默认显示fragment1
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_frame, f1);
        ft.commit();

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.syncState();//将左上角的图标和侧滑监听进行联动 达到动画效果显示
        drawerLayout.addDrawerListener(drawerToggle);//设置侧滑菜单的监听

    }


    /**
     * 修改显示的内容 不会重新加载
     **/
    private Fragment mContent;
    public void switchContent(Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(mContent).add(R.id.content_frame, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case 0:
                if (f1 == null) {
                    f1 = new Fragment1();
                }
                switchContent(f1);
                adapter.setSelectedItem(0);
                break;
            case 1:
                if (f2 == null) {
                    f2 = new Fragment2();
                }
                switchContent(f2);
                adapter.setSelectedItem(1);
                break;
            case 2:
                if (f3 == null) {
                    f3 = new Fragment3();
                }
                switchContent(f3);
                adapter.setSelectedItem(2);
                break;
        }
        drawerLayout.closeDrawers();//自动关闭侧滑菜单
        adapter.notifyDataSetChanged();
    }

    private class MyAdapter extends BaseAdapter {

        private int selestedItemPos = -1;

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private void setSelectedItem(int positon) {
            this.selestedItemPos = positon;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false);
                holder = new ViewHolder();
                holder.tv = (TextView) convertView.findViewById(R.id.id_tv);
                holder.icon = (ImageView)convertView.findViewById(R.id.id_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(items[position]);
            holder.icon.setImageResource(imgIds[position]);
            //选中设置字体为高亮显示
            if (selestedItemPos == position) {
                holder.tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
            } else {
                holder.tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            }
            return convertView;
        }

        class ViewHolder {
            TextView tv;
            ImageView icon;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setIconVisible(menu,true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_edit){
            if(drawerLayout.isDrawerOpen(Gravity.START)){
                drawerLayout.closeDrawer(Gravity.START);
                return true;
            }else{
                drawerLayout.openDrawer(Gravity.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * *显示溢出菜单图标
     **/
    public void setIconVisible(Menu menu, boolean visable){
        Field field;
        try {
            field = menu.getClass().getDeclaredField("mOptionalIconsVisible");
            field.setAccessible(true);
            field.set(menu, visable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
