package com.example.getsumfoot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity{
    public static String current_user; //fragment에서 이 변수 접근
    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //main activity로부터 받아옴- seller, customer type에 따라 메뉴와 시작화면 다름
        Intent intent = getIntent();
        boolean is_seller = Boolean.parseBoolean(intent.getStringExtra("is_seller"));
        current_user = intent.getStringExtra("current_user");

        final int[] customerMenu = {R.id.nav_home, R.id.nav_my_page_customer, R.id.nav_review};
        final int[] sellerMenu = {R.id.nav_home, R.id.nav_my_page_seller, R.id.nav_map_event};

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);

        int[] list;
        if(is_seller){ //seller? sellermenu.show : customermenu.show
            navigationView.getMenu().setGroupVisible(R.id.menu_seller,true);
            list = sellerMenu;
            graph.setStartDestination(R.id.nav_my_page_seller); //seller일때 mypage부터 시작
        }else{
            navigationView.getMenu().setGroupVisible(R.id.menu_customer,true);
            list = customerMenu;
            graph.setStartDestination(R.id.nav_home); //customer일 때 메인 지도화면으로 시작
        }
        navController.setGraph(graph);
        mAppBarConfiguration = new AppBarConfiguration.Builder(list)
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //FCM
        receiveMessage();
    }
    //jonghun add code(firebasemessageservice)
    //firebasemessage service Intent
    private void receiveMessage() {
        Intent intent = getIntent();
        if (intent != null) {//푸시알림을 선택해서 실행한것이 아닌경우 예외처리
            String notificationData = intent.getStringExtra("test");
            if (notificationData != null)
                Log.d("FCM_TEST", notificationData);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}
