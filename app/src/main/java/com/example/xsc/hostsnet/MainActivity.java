package com.example.xsc.hostsnet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //看不见的webview
    private WebView webView;
    //ViewPager的子项
    private View init_view,dianxin_view,duoxian_view,liantong_view,yidong_view,haiwai_view;
    //声明viewPager
    private ViewPager viewPager;
    //存放view，作为传递到MyViewPagerAdapter适配器的变量
    private static List<View> viewList;
    //声明tabLayout
    private TabLayout tabLayout;
    //存放tabLayout的标签文本
    private String choose[] = new String[]{"电信","多线","联通","移动","海外"},selected[] = new String[5];
    //上划到最顶端是，若webEdt内容非空，则显示webEdt的内容，该部件处于工具条顶端
    private TextView webText,txtxt;
    //网址输入框
    private EditText webEdt;
    //搜索按钮
    private Button searchBtn,dianxin_btn,duoxian_btn,liantong_btn,yidong_btn,haiwai_btn;
    //用于退出事件的布尔参数
    private static Boolean isExit = false;
    //两次返回键的时间超过两秒会调用这个Handler，使isExit重设为false
    private static Handler exitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    //设备版本号
    public static int version = android.os.Build.VERSION.SDK_INT;
    //存放item的list
    static List<Item> itemList1,itemList2,itemList3,itemList4,itemList5;
    //
    Toolbar toolbar;
    //
    AppBarLayout appBarLayout;
    //
    DrawerLayout drawer;
    //
    ActionBarDrawerToggle toggle;
    //
    NavigationView navigationView;
    //
    FloatingActionButton fab;

    //暂时
    Handler handler = new Handler();
    static int okokok = 0;
    Document doc;
    Elements html_number,html_city,html_ip,html_time;
    LayoutInflater lf;
    RecyclerView recyclerView1,recyclerView2,recyclerView3,recyclerView4,recyclerView5;
    Thread thread0,thread;
    static int selected_num;
    static int itemList1_num = 0,itemList2_num = 0,itemList3_num = 0,itemList4_num = 0,itemList5_num = 0;
    Thread search_btn_thread = new Thread();
    MyAdapter MyAdapter1 = new MyAdapter((ArrayList<Item>) itemList1);
    MyAdapter MyAdapter2 = new MyAdapter((ArrayList<Item>) itemList2);
    MyAdapter MyAdapter3 = new MyAdapter((ArrayList<Item>) itemList3);
    MyAdapter MyAdapter4 = new MyAdapter((ArrayList<Item>) itemList4);
    MyAdapter MyAdapter5 = new MyAdapter((ArrayList<Item>) itemList5);
    MyAdapter.OnItemClickListener onItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //初始化布局
        super.onCreate(savedInstanceState);
        //用于判断SDK版本号，对应不同布局
        if(version == 19) {
            setContentView(R.layout.activity_main_19);
        }else {
            setContentView(R.layout.activity_main);
        }
        init();

        //
        itemList1 = new ArrayList<Item>();
        itemList2 = new ArrayList<Item>();
        itemList3 = new ArrayList<Item>();
        itemList4 = new ArrayList<Item>();
        itemList5 = new ArrayList<Item>();

        recyclerView1 = (RecyclerView) dianxin_view.findViewById(R.id.recycler);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView1.setHasFixedSize(true);

        recyclerView2 = (RecyclerView) duoxian_view.findViewById(R.id.recycler);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView2.setHasFixedSize(true);

        recyclerView3 = (RecyclerView) liantong_view.findViewById(R.id.recycler);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView3.setNestedScrollingEnabled(false);
        recyclerView3.setHasFixedSize(true);

        recyclerView4 = (RecyclerView) yidong_view.findViewById(R.id.recycler);
        recyclerView4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView4.setNestedScrollingEnabled(false);
        recyclerView4.setHasFixedSize(true);

        recyclerView5 = (RecyclerView) haiwai_view.findViewById(R.id.recycler);
        recyclerView5.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView5.setNestedScrollingEnabled(false);
        recyclerView5.setHasFixedSize(true);

        onItemClickListener = new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setDialog();
            }
        };
        MyAdapter1.setOnItemClickListener(onItemClickListener);
        MyAdapter2.setOnItemClickListener(onItemClickListener);
        MyAdapter3.setOnItemClickListener(onItemClickListener);
        MyAdapter4.setOnItemClickListener(onItemClickListener);
        MyAdapter5.setOnItemClickListener(onItemClickListener);


        //暂时使用的view
        webView = (WebView) findViewById(R.id.webview);
        txtxt = (TextView) findViewById(R.id.qwer);
        initButton();

        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setUserAgentString("computer");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                if(okokok == 0){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            txtxt.setText("准备就绪");
                        }
                    });
                    webView.loadUrl("javascript:document.getElementsByClassName('actv chkall')[0].click()");
                    if(viewList.contains(dianxin_view)){
                        webView.loadUrl("javascript:document.getElementsByClassName(' chk')[0].click()");
                    }
                    if(viewList.contains(duoxian_view)){
                        webView.loadUrl("javascript:document.getElementsByClassName(' chk')[1].click()");
                    }
                    if(viewList.contains(liantong_view)){
                        webView.loadUrl("javascript:document.getElementsByClassName(' chk')[2].click()");
                    }
                    if(viewList.contains(yidong_view)){
                        webView.loadUrl("javascript:document.getElementsByClassName(' chk')[3].click()");
                    }
                    if(viewList.contains(haiwai_view)){
                        webView.loadUrl("javascript:document.getElementsByClassName(' chk')[4].click()");
                    }
                    webView.loadUrl("javascript:document.getElementById('host').setAttribute('value','"+webEdt.getText().toString()+"')");
                    webView.loadUrl("javascript:document.getElementsByClassName('search-write-btn')[0].click()");
                    okokok = 1;
                }
            }
        });

        //实例化搜索按钮，设置点击事件
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!search_btn_thread.isAlive()){
                    if(!webEdt.getText().toString().isEmpty()){
                        if(!((dianxin_btn.getCurrentTextColor() == -1)&&(duoxian_btn.getCurrentTextColor() == -1)&&(liantong_btn.getCurrentTextColor() == -1)&&(yidong_btn.getCurrentTextColor() == -1)&&(haiwai_btn.getCurrentTextColor() == -1))){
                            itemList1.clear();
                            itemList2.clear();
                            itemList3.clear();
                            itemList4.clear();
                            itemList5.clear();
                            viewList = new ArrayList<View>();
                            selected_num = 0;
                            if(dianxin_btn.getCurrentTextColor() != -1){
                                viewList.add(selected_num,dianxin_view);
                                selected[selected_num] = choose[0];
                                selected_num++;
                            }
                            if(duoxian_btn.getCurrentTextColor() != -1){
                                viewList.add(selected_num,duoxian_view);
                                selected[selected_num] = choose[1];
                                selected_num++;
                            }
                            if(liantong_btn.getCurrentTextColor() != -1){
                                viewList.add(selected_num,liantong_view);
                                selected[selected_num] = choose[2];
                                selected_num++;
                            }
                            if(yidong_btn.getCurrentTextColor() != -1){
                                viewList.add(selected_num,yidong_view);
                                selected[selected_num] = choose[3];
                                selected_num++;
                            }
                            if(haiwai_btn.getCurrentTextColor() != -1){
                                viewList.add(selected_num,haiwai_view);
                                selected[selected_num] = choose[4];
                            }
                            viewPager.setAdapter(new MyViewPagerAdapter(viewList));
                            for(int a = 0 ; a < tabLayout.getTabCount() ; a++){
                                tabLayout.getTabAt(a).setText(selected[a]);
                            }
                            if(itemList1_num != 0){
                                recyclerView1.setAdapter(MyAdapter1.updateData((ArrayList<Item>)itemList1));
                            }
                            if(itemList2_num != 0){
                                recyclerView2.setAdapter(MyAdapter2.updateData((ArrayList<Item>)itemList2));
                            }
                            if(itemList3_num != 0){
                                recyclerView3.setAdapter(MyAdapter3.updateData((ArrayList<Item>)itemList3));
                            }
                            if(itemList4_num != 0){
                                recyclerView4.setAdapter(MyAdapter4.updateData((ArrayList<Item>)itemList4));
                            }
                            if(itemList5_num != 0){
                                recyclerView5.setAdapter(MyAdapter5.updateData((ArrayList<Item>)itemList5));
                            }

                            webView.loadUrl("http://ping.chinaz.com");
                            okokok = 0;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    txtxt.setText("开始准备...");
                                }
                            });
                            search_btn_thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for(int a = 0 ; a < 100 ; a++ ){
                                        if(okokok == 1){
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(webView.getUrl().equals("http://ping.chinaz.com/"+webEdt.getText().toString())) {
                                                        webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                                                    }
                                                }
                                            });
                                            try {
                                                Thread.sleep(2000);
                                            }catch (InterruptedException e){
                                                e.printStackTrace();
                                            }
                                        }else if (okokok == 0){
                                            try {
                                                Thread.sleep(2000);
                                            }catch (InterruptedException e){
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            Thread.interrupted();
                                        }
                                    }
                                }
                            });
                            search_btn_thread.start();
                        }else {
//                            viewList.add(init_view);
//                            viewPager.setAdapter(new MyViewPagerAdapter(viewList));
                            txtxt.setText("请选择线路");
                        }
                    }else {
                        txtxt.setText("请输入网址");
                    }
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "已经有任务在进行了，请等候其加载完成再重试", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public void init(){
        //初始化工具条
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化AppBarlayout，webText，并在监听器中重写onStateChanged方法
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        webText = (TextView) findViewById(R.id.webText);
        //初始化ViewPager和TabLayout，并将它们关联起来
        lf = getLayoutInflater().from(this);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        //用viewList数组存放ViewPager的子项
        viewList = new ArrayList<View>();
        init_view = lf.inflate(R.layout.init_image,null);
        dianxin_view = lf.inflate(R.layout.show_item, null);
        duoxian_view = lf.inflate(R.layout.show_item, null);
        liantong_view = lf.inflate(R.layout.show_item, null);
        yidong_view = lf.inflate(R.layout.show_item, null);
        haiwai_view = lf.inflate(R.layout.show_item, null);
        viewList.add(init_view);
        //为ViewPager设置适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewList));
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {

                    //展开状态
                    tabLayout.setBackgroundColor(Color.parseColor("#262364"));

                }else if(state == State.COLLAPSED){

                    //折叠状态
                    tabLayout.setBackgroundColor(Color.parseColor("#355390"));
                    webText.setText(webEdt.getText().toString());

                }else {

                    //中间状态
                    tabLayout.setBackgroundColor(Color.parseColor("#355390"));
                    webText.setText("");

                }
            }
        });

        //初始化DrawerLayout，并为其设置可旋转的三线菜单按钮（工具条左侧）
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //初始化侧滑菜单
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //初始化网址输入框，设置当文本长度大于文本框长度时，水平延伸（若不设置则分行）
        webEdt = (EditText) findViewById(R.id.webEdt);
        webEdt.setHorizontallyScrolling(true);

        //初始化悬浮按钮
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void initButton(){
        dianxin_btn = (Button) findViewById(R.id.dianxin_btn);
        duoxian_btn = (Button) findViewById(R.id.duoxian_btn);
        liantong_btn = (Button) findViewById(R.id.liantong_btn);
        yidong_btn = (Button) findViewById(R.id.yidong_btn);
        haiwai_btn = (Button) findViewById(R.id.haiwai_btn);
        dianxin_btn.getBackground().setAlpha(0);
        duoxian_btn.getBackground().setAlpha(0);
        liantong_btn.getBackground().setAlpha(0);
        yidong_btn.getBackground().setAlpha(0);
        haiwai_btn.getBackground().setAlpha(0);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Button)view).getCurrentTextColor() == -1){
                    ((Button) view).setTextColor(Color.parseColor("#ff99cc00"));
                }else {
                    ((Button)view).setTextColor(-1);
                }

            }
        };
        dianxin_btn.setOnClickListener(onClickListener);
        duoxian_btn.setOnClickListener(onClickListener);
        liantong_btn.setOnClickListener(onClickListener);
        yidong_btn.setOnClickListener(onClickListener);
        haiwai_btn.setOnClickListener(onClickListener);
    }

    //点击返回时，当侧滑菜单正处于焦点时，则关闭菜单，否则按两次后退退出程序
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!isExit) {
                isExit = true;
                Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
                exitHandler.sendEmptyMessageDelayed(0, 2000);  // 利用handler延迟发送更改状态信息
            } else {
                this.finish();
                System.exit(0);
            }

        }

    }

    //设置工具条右侧的菜单布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //三点菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_toolbar_help) {
            return true;
        }
        else if (id == R.id.menu_toolbar_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //三线菜单点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.menu_nav_change){
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {
            doc = Jsoup.parse(html);
            html_city = doc.select("[name=city]");
            html_ip = doc.select("[name=ip]");
            html_time = doc.select("[name=responsetime]");
            html_number = doc.select("[id=gjd]");
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!(html_number.text().toString().equals("")||html_number.text().toString().equals("0"))) {
                        itemList1_num = 0;
                        itemList2_num = 0;
                        itemList3_num = 0;
                        itemList4_num = 0;
                        itemList5_num = 0;
                        itemList1.clear();
                        itemList2.clear();
                        itemList3.clear();
                        itemList4.clear();
                        itemList5.clear();
                        for (int a = 0; a < Integer.parseInt(html_number.text().toString()); a++) {
                            if (html_city.get(a).text().substring(html_city.get(a).text().length()-4,html_city.get(a).text().length()).equals("[电信]")) {
                                itemList1.add(itemList1_num, new Item(html_city.get(a).text(), html_ip.get(a).text(), html_time.get(a).text()));
                                itemList1_num++;
                            }
                            else if (html_city.get(a).text().substring(html_city.get(a).text().length()-4,html_city.get(a).text().length()).equals("[多线]")) {
                                itemList2.add(itemList2_num, new Item(html_city.get(a).text(), html_ip.get(a).text(), html_time.get(a).text()));
                                itemList2_num++;
                            }
                            else if (html_city.get(a).text().substring(html_city.get(a).text().length()-4,html_city.get(a).text().length()).equals("[联通]")) {
                                itemList3.add(itemList3_num, new Item(html_city.get(a).text(), html_ip.get(a).text(), html_time.get(a).text()));
                                itemList3_num++;
                            }
                            else if (html_city.get(a).text().substring(html_city.get(a).text().length()-4,html_city.get(a).text().length()).equals("[移动]")) {
                                itemList4.add(itemList4_num, new Item(html_city.get(a).text(), html_ip.get(a).text(), html_time.get(a).text()));
                                itemList4_num++;
                            }
                            else if (html_city.get(a).text().substring(html_city.get(a).text().length()-4,html_city.get(a).text().length()).equals("[海外]")) {
                                itemList5.add(itemList5_num, new Item(html_city.get(a).text(), html_ip.get(a).text(), html_time.get(a).text()));
                                itemList5_num++;
                            }else {
                            }
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(itemList1_num != 0){
                                recyclerView1.setAdapter(MyAdapter1.updateData((ArrayList<Item>)itemList1));
                            }
                            if(itemList2_num != 0){
                                recyclerView2.setAdapter(MyAdapter2.updateData((ArrayList<Item>)itemList2));
                            }
                            if(itemList3_num != 0){
                                recyclerView3.setAdapter(MyAdapter3.updateData((ArrayList<Item>)itemList3));
                            }
                            if(itemList4_num != 0){
                                recyclerView4.setAdapter(MyAdapter4.updateData((ArrayList<Item>)itemList4));
                            }
                            if(itemList5_num != 0){
                                recyclerView5.setAdapter(MyAdapter5.updateData((ArrayList<Item>)itemList5));
                            }
                        }
                    });
                    thread.interrupted();
                }
            });
            if(!thread.isAlive()){
                thread.start();
            }
            if(!(html_ip.get(html_ip.size()-1).text().toString().equals("-")||html_ip.get(html_ip.size()-1).text().toString().equals("正在加载..."))){
                okokok = 2;
                thread.interrupt();
                thread0 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(!(html_number.text().toString().equals("")||html_number.text().toString().equals("0"))) {
                            itemList1.clear();
                            itemList2.clear();
                            itemList3.clear();
                            itemList4.clear();
                            itemList5.clear();
                            itemList1_num = 0;
                            itemList2_num = 0;
                            itemList3_num = 0;
                            itemList4_num = 0;
                            itemList5_num = 0;
                            for (int a = 0; a < Integer.parseInt(html_number.text().toString()); a++) {
                                if (html_city.get(a).text().substring(html_city.get(a).text().length()-4,html_city.get(a).text().length()).equals("[电信]")) {
                                    itemList1.add(itemList1_num, new Item(html_city.get(a).text(), html_ip.get(a).text(), html_time.get(a).text()));
                                    itemList1_num++;
                                }
                                else if (html_city.get(a).text().substring(html_city.get(a).text().length()-4,html_city.get(a).text().length()).equals("[多线]")) {
                                    itemList2.add(itemList2_num, new Item(html_city.get(a).text(), html_ip.get(a).text(), html_time.get(a).text()));
                                    itemList2_num++;
                                }
                                else if (html_city.get(a).text().substring(html_city.get(a).text().length()-4,html_city.get(a).text().length()).equals("[联通]")) {
                                    itemList3.add(itemList3_num, new Item(html_city.get(a).text(), html_ip.get(a).text(), html_time.get(a).text()));
                                    itemList3_num++;
                                }
                                else if (html_city.get(a).text().substring(html_city.get(a).text().length()-4,html_city.get(a).text().length()).equals("[移动]")) {
                                    itemList4.add(itemList4_num, new Item(html_city.get(a).text(), html_ip.get(a).text(), html_time.get(a).text()));
                                    itemList4_num++;
                                }
                                else if (html_city.get(a).text().substring(html_city.get(a).text().length()-4,html_city.get(a).text().length()).equals("[海外]")) {
                                    itemList5.add(itemList5_num, new Item(html_city.get(a).text(), html_ip.get(a).text(), html_time.get(a).text()));
                                    itemList5_num++;
                                }else {
                                }
                            }
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(itemList1_num != 0){
                                    recyclerView1.setAdapter(MyAdapter1.updateData((ArrayList<Item>)itemList1));
                                }
                                if(itemList2_num != 0){
                                    recyclerView2.setAdapter(MyAdapter2.updateData((ArrayList<Item>)itemList2));
                                }
                                if(itemList3_num != 0){
                                    recyclerView3.setAdapter(MyAdapter3.updateData((ArrayList<Item>)itemList3));
                                }
                                if(itemList4_num != 0){
                                    recyclerView4.setAdapter(MyAdapter4.updateData((ArrayList<Item>)itemList4));
                                }
                                if(itemList5_num != 0){
                                    recyclerView5.setAdapter(MyAdapter5.updateData((ArrayList<Item>)itemList5));
                                }
                            }
                        });
                        thread0.interrupted();
                    }
                });
                if(!thread.isAlive()){
                    thread.start();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        txtxt.setText("有效探测点已全部加载完成，共"+html_number.text().toString()+"个");
                    }
                });
            }else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        txtxt.setText("一共"+html_city.size()+"个探测点，已完成"+html_number.text().toString()+"个");
                    }
                });
            }
        }
    }
    private void setDialog() {
        final Dialog mCameraDialog = new Dialog(this, R.style.ItemDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.item_dialog, null);
        //初始化视图
        root.findViewById(R.id.ping_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        root.findViewById(R.id.add_hosts_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        root.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraDialog.cancel();
            }
        });
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }
}
