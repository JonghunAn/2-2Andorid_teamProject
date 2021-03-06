package com.example.getsumfoot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getsumfoot.data.LikesData;
import com.example.getsumfoot.data.OrderData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

//todo firebase 연결해서 필요한 정보 뿌리기
public class CustomerOrdersFragment extends Fragment {
    private static final String TAG="CustomerOrdersFragment";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<OrderData> list;

    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private String current_user;
    //private String uid;
    private int num = 1;

    public CustomerOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_customer_orders, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        current_user = BaseActivity.current_user;
//
        databaseReference = database.getReference("Customer/"+current_user+"/orders");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_customer_orders);
        list = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear(); //기존 배열리스트 초기화
                    for(DataSnapshot snap : snapshot.getChildren()){ // 데이터 리스트 추출
                        OrderData orderData = snap.getValue(OrderData.class); //만들어뒀던 OrderData 객체에 데이터를 담는다
                        list.add(orderData); // 담은 데이터들을 배열리스트에 넣고 리사이틀러뷰로 보낼준비
                        Log.e("CustomerOrders", list.toString());

                        num++;
                    }
                }
                Log.e("Orders", list.toString());
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, String.valueOf(error.toException()));
            }
        });
        adapter = new CustomerOrdersAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}