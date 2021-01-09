package com.example.study.leader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.global.CommonHeader;
import com.example.study.leader.adapter.MemberManageAdapter;
import com.example.study.main.MainActivity;
import com.example.study.study.ModifyGroupActivity;
import com.example.study.timetable.RepresentativeTimeTableOneActivity;
import com.example.study.util.JsonRequestUtil;

public class LeaderViewActivity extends AppCompatActivity {
    private Context context;
    private Long gno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_view);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        Button btn_representative_time_table = findViewById(R.id.btn_representative_time_table);
        btn_representative_time_table.setOnClickListener(view->{
            Intent intent = new Intent(context, RepresentativeTimeTableOneActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });

        Button management = findViewById(R.id.btn_management);
        management.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MemberManageActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            startActivity(intent);
        });

        Button btn_attendance_manage = findViewById(R.id.btn_attendance_manage);
        btn_attendance_manage.setOnClickListener(view->{
            Intent intent = new Intent(getApplicationContext(), AttendanceManageActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            startActivity(intent);
        });

        Button btn_dismiss = findViewById(R.id.btn_dismiss);
        btn_dismiss.setOnClickListener(view->{
            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            ad.setTitle("그룹 해체");
            ad.setMessage("정말로 그룹해체를 하시겠습니까?");
            ad.setPositiveButton("확인", (dialog, which) -> {
                new JsonRequestUtil<String>(context).request(
                        Request.Method.DELETE,
                        "/study/"+gno,
                        String.class,
                        CommonHeader.getInstance().getCommonHeader(),
                        response->{
                            Toast.makeText(context,"그룹 해체 성공",Toast.LENGTH_SHORT).show();
                            finish();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class );
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        },
                        error->{
                            Toast.makeText(context,"그룹 해체 실패",Toast.LENGTH_SHORT).show();
                        }
                );
                dialog.dismiss();
            });
            ad.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
            ad.show();
        });

        Button btn_modify_group = findViewById(R.id.btn_modify_group);
        btn_modify_group.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), ModifyGroupActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            startActivity(intent);
        });
        Button btn_accept_member = findViewById(R.id.btn_accept_member);
        btn_accept_member.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), AcceptMemberActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            startActivity(intent);
        });
    }
}