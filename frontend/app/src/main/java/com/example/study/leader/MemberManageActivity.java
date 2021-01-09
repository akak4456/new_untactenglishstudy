package com.example.study.leader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.global.CommonHeader;
import com.example.study.leader.adapter.MemberManageAdapter;
import com.example.study.leader.vo.MemberManageResponse;
import com.example.study.leader.vo.MemberManageVO;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MemberManageActivity extends AppCompatActivity {
    private Context context;
    private Long gno;
    private ListView member_manage_list;
    private MemberManageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_manage);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        member_manage_list = findViewById(R.id.member_manage_list);
        new JsonRequestUtil<MemberManageResponse>(context).request(
                Request.Method.GET,
                "/leader/"+gno,
                MemberManageResponse.class,
                CommonHeader.getInstance().getCommonHeader(),
                response->{
                    String json = new Gson().toJson(response.getMembers());
                    Type listType = new TypeToken<List<MemberManageVO>>(){}.getType();
                    List<MemberManageVO> list = new Gson().fromJson(json,listType);
                    adapter = new MemberManageAdapter(list,gno);
                    member_manage_list.setAdapter(adapter);
                },
                error->{
                    Toast.makeText(context,"읽을 수 없음",Toast.LENGTH_SHORT).show();
                }
        );
    }
}