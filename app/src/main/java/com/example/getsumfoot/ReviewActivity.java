package com.example.getsumfoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.getsumfoot.data.ReviewData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ReviewData> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        button = findViewById(R.id.write_button);
        button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ReviewWriteActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true); // 리사이클러 뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // Review Data를 담을 어레이 리스트

        database = FirebaseDatabase.getInstance(); //파이어베이스 연동


        database.getReference("ReviewData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear(); //기존 배열리스트 초기화
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ // 데이터 리스트 추출
                    ReviewData reviewData = snapshot.getValue(ReviewData.class); //만들어뒀던 ReviewData 객체에 데이터를 담는다
                    arrayList.add(reviewData); // 담은 데이터들을 배열리스트에 넣고 리사이틀러뷰로 보낼준비

                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            } // 파이어베이스 데이터베이스의 데이터 받아오는 곳

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ReviewActivity",String.valueOf(databaseError.toException())); // 에러문 출력
            } //데이터 베이스 가져오다가 에러 발생시 에러처리
        });

        adapter = new ReviewAdapter(arrayList,this);
        recyclerView.setAdapter(adapter); // 리사이클러 뷰에 adapter 연결
    }
}