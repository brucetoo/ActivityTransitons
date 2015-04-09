package com.example.brucetoo.activitytransitons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.rv_photos)
    RecyclerView rv_photos;
    private PhotoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rv_photos.setLayoutManager(manager);
        adapter = new PhotoAdapter();
        rv_photos.setAdapter(adapter);
        rv_photos.addOnItemTouchListener(new RecyclerItemClickListener(this,rv_photos,new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               //纪录下点击的点坐标 和 点击 view 的宽高
                int[] screenLocation = new int[2];
                view.getLocationOnScreen(screenLocation);
                Log.e("left:",screenLocation[0]+"");
                Log.e("top:",screenLocation[1]+"");
                Log.e("right:",view.getWidth()+"");
                Log.e("bottom:",view.getHeight()+"");
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("left",screenLocation[0]);
                intent.putExtra("top",screenLocation[1]);
                intent.putExtra("right",view.getWidth());
                intent.putExtra("bottom",view.getHeight());
                startActivity(intent);
                //此处非常重要，用于屏蔽系统默认的activity跳转
                overridePendingTransition(0,0);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
