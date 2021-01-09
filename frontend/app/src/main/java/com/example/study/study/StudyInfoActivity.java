package com.example.study.study;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.global.CommonHeader;
import com.example.study.study.vo.GroupInfoVO;
import com.example.study.study.vo.GroupItemVO;
import com.example.study.study.vo.RewardAndFineVO;
import com.example.study.util.JsonRequestUtil;

public class StudyInfoActivity extends AppCompatActivity {
    private Context context;
    private Long gno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_info);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        TextView test_kind = findViewById(R.id.test_kind);
        TextView max_people = findViewById(R.id.max_people);
        TextView deposit = findViewById(R.id.deposit);
        TextView max_absence = findViewById(R.id.max_absence);
        TextView late_fine = findViewById(R.id.late_fine);
        TextView absence_fine = findViewById(R.id.absence_fine);
        TextView last_day = findViewById(R.id.last_day);
        TextView reward = findViewById(R.id.reward);
        TextView fine = findViewById(R.id.fine);

        new JsonRequestUtil<GroupInfoVO>(context).request(
                Request.Method.GET,
                "/study/one/"+gno,
                GroupInfoVO.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    GroupItemVO group = response.getGroup();
                    test_kind.setText(group.getType());
                    max_people.setText(group.getMaximumNumberOfPeople()+"명");
                    deposit.setText(group.getDepositToBePaid()+"원");
                    max_absence.setText(group.getMaximumNumberOfAbsencesAllowed()+"번");
                    late_fine.setText(group.getFineForBeingLate()+"원");
                    absence_fine.setText(group.getFineForBeingAbsence()+"원");
                    last_day.setText(group.getDuedate());
                },
                error -> {

                }
        );

        new JsonRequestUtil<RewardAndFineVO>(context).request(
                Request.Method.GET,
                "/study/findrewardandfine/"+gno,
                RewardAndFineVO.class,
                CommonHeader.getInstance().getCommonHeader(),
                response->{
                    reward.setText(response.getReward()+"원");
                    fine.setText(response.getFine()+"원");
                },
                error->{

                }
        );

        Button btn_pay_deposit = findViewById(R.id.btn_pay_deposit);
        btn_pay_deposit.setOnClickListener(view->{
            new JsonRequestUtil<String>(context).request(
                    Request.Method.POST,
                    "/deposit/"+gno,
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response -> {
                        Toast.makeText(context,"예치금 지불 성공", Toast.LENGTH_SHORT).show();
                    },
                    error ->{
                        Toast.makeText(context,"예치금 지불 실패", Toast.LENGTH_SHORT).show();
                    }
            );
        });
    }
}
