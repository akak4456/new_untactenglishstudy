package com.example.study.leader;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.global.CommonHeader;
import com.example.study.leader.adapter.AcceptMemberAdapter;
import com.example.study.leader.adapter.AttendanceManageAdapter;
import com.example.study.leader.vo.AttendanceResponse;
import com.example.study.leader.vo.AttendanceVO;
import com.example.study.leader.vo.GroupInclude;
import com.example.study.leader.vo.WaitingResponse;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public class AcceptMemberActivity extends AppCompatActivity {
    private Context context;
    private ListView ll;
    private Long gno;
    private AcceptMemberAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_member);
        context = this;
        ll = findViewById(R.id.waiting_list);
        gno = getIntent().getLongExtra("gno",0L);

        new JsonRequestUtil<WaitingResponse>(context).request(
                Request.Method.GET,
                "/leader/waiting/"+gno,
                WaitingResponse.class,
                CommonHeader.getInstance().getCommonHeader(),
                response->{
                    String json = new Gson().toJson(response.getWaitings());
                    Type listType = new TypeToken<List<GroupInclude>>(){}.getType();
                    List<GroupInclude> list = new Gson().fromJson(json,listType);
                    adapter = new AcceptMemberAdapter(list,gno);
                    ll.setAdapter(adapter);
                },
                error->{
                    error.printStackTrace();
                    Toast.makeText(context,"읽을 수 없음",Toast.LENGTH_SHORT).show();
                }
        );

        Button btn_all_reject = findViewById(R.id.btn_all_reject);
        btn_all_reject.setOnClickListener(v->{
            new JsonRequestUtil<String>(context).request(
                    Request.Method.PUT,
                    "/leader/rejectall/"+gno,
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response->{
                        Toast.makeText(context,"모두 거절 하였습니다.",Toast.LENGTH_SHORT).show();
                        adapter.clearItems();
                    },
                    error->{
                        Toast.makeText(context,"모두 거절하지 못했습니다.",Toast.LENGTH_SHORT).show();
                    }
            );
        });
    }
}
