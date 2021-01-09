package com.example.study.study.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.study.study.CreateGroupActivity;
import com.example.study.study.adapter.StudyListAdapter;
import com.example.study.study.vo.GroupItemVO;
import com.example.study.study.vo.GroupPageMaker;
import com.example.study.global.CommonHeader;
import com.example.study.global.MyMemberInfo;
import com.example.study.R;
import com.example.study.util.JsonRequestUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private StudyListAdapter adapter;
    private int totalPage;//총페이지수
    private int pageNum;//현재 페이지수 + 1
    private Parcelable recyclerViewState;//스크롤 위치 저장
    private boolean loading;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_study, container,false);
        //플로팅액션버튼 : 새로운 스터디 개설
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton_Addstudy);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CreateGroupActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        return view;

    }

    @Override
    public void onStart(){
        super.onStart();
        initView();
        getPage(1,10);//일단 처음 페이지를 불러온다
    }
    private void initView(){
        if(adapter != null)
            adapter.clearItems();
        adapter = new StudyListAdapter();
        pageNum = 2;
        loading = false;
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!loading&&!recyclerView.canScrollVertically(1)){
                    //스크롤의 마지막에 닿으면
                    loading = true;
                    recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();//일단 현재 스크롤된 곳을 저장한 뒤에
                    if(pageNum <= totalPage)
                        getPage(pageNum,10);//데이터를 불러와서 view를 바꾼다.
                    pageNum++;//다음 페이지를 부를 준비를 한다
                }
            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void getPage(int page, int size){
        new JsonRequestUtil<GroupPageMaker>(getContext()).request(
                Request.Method.GET,
                "/study/"+ MyMemberInfo.getInstance().getMember().getMno() +"?page="+page+"&size="+size,
                GroupPageMaker.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    totalPage = response.getTotalPages();
                    System.out.println(totalPage);
                    String json = new Gson().toJson(response.getResult().getContent());
                    Type listType = new TypeToken<List<GroupItemVO>>(){}.getType();
                    List<GroupItemVO> list = new Gson().fromJson(json,listType);
                    //부른 데이터를 이용해서 recycler view를 바꾼 뒤에 recycler view가 맨 위로 갈 것인데 저장한 스크롤 위치로 되돌아간다
                    adapter.addItems(list);
                    recyclerView.setAdapter(adapter);
                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    loading = false;
                },
                error -> {

                }
        );
    }


}
