package com.example.study.study;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.global.CommonHeader;
import com.example.study.main.MainActivity;
import com.example.study.study.vo.GroupAddVO;
import com.example.study.study.vo.GroupInfoVO;
import com.example.study.study.vo.GroupItemVO;
import com.example.study.util.JsonRequestUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//버튼(id = Group_create) 클릭하면 db에 정보 저장
//다시 studyfragment로 돌아감

public class ModifyGroupActivity extends AppCompatActivity {
    private Context context;
    private Long gno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        EditText mGroupName = findViewById(R.id.Group_name);
        EditText mGroupDeposit = findViewById(R.id.Group_deposit);
        EditText mGroupType = findViewById(R.id.Group_type);
        EditText mGroupIntro = findViewById(R.id.Group_intro);
        EditText mGroupMaxAbsence = findViewById(R.id.Group_max_absence);
        EditText mGroupMax = findViewById(R.id.Group_max);
        EditText mGroupLateFine = findViewById(R.id.Group_late_fine);
        EditText mGroupAbsenceFine = findViewById(R.id.Group_absence_fine);
        DatePicker mDate = findViewById(R.id.datepicker);
        TextView mTxtDate = findViewById(R.id.Group_period);

        new JsonRequestUtil<GroupInfoVO>(context).request(
                Request.Method.GET,
                "/study/one/"+gno,
                GroupInfoVO.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    GroupItemVO group = response.getGroup();
                    mGroupName.setText(group.getTitle());
                    mGroupDeposit.setText(group.getDepositToBePaid()+"");
                    mGroupType.setText(group.getType());
                    mGroupIntro.setText(group.getDetail());
                    mGroupMaxAbsence.setText(group.getMaximumNumberOfAbsencesAllowed()+"");
                    mGroupMax.setText(group.getMaximumNumberOfPeople()+"");
                    mGroupLateFine.setText(group.getFineForBeingLate()+"");
                    mGroupAbsenceFine.setText(group.getFineForBeingAbsence()+"");
                    String dateStr = group.getDuedate();
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar date = Calendar.getInstance();
                    try {
                        date.setTime(format1.parse(dateStr));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(date.get(Calendar.YEAR));
                    System.out.println(date.get(Calendar.MONTH));
                    System.out.println(date.get(Calendar.DATE));
                    mDate.updateDate(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE));
                },
                error -> {

                }
        );
        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) -> mTxtDate.setText(String.format("%d/%d/%d",year,monthOfYear+1,dayOfMonth)));
        mDate.setMinDate(new Date().getTime());
        // 개설 버튼
        Button button = findViewById(R.id.Group_create);
        button.setOnClickListener(view -> {
            //나중에 클라이언트 쪽에서 이 데이터가 적절한지 확인해야함
            new JsonRequestUtil<String>(getApplicationContext()).request(
                    Request.Method.PUT,
                    "/study/"+gno,
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    new GroupAddVO(
                            mGroupName.getText().toString(),
                            mGroupType.getText().toString(),
                            mGroupIntro.getText().toString(),
                            Long.parseLong(mGroupMax.getText().toString()),
                            Long.parseLong(mGroupDeposit.getText().toString()),
                            Long.parseLong(mGroupMaxAbsence.getText().toString()),
                            Long.parseLong(mGroupLateFine.getText().toString()),
                            Long.parseLong(mGroupAbsenceFine.getText().toString()),
                            String.format("%04d-%02d-%02d 00:00:00",mDate.getYear(),mDate.getMonth()+1,mDate.getDayOfMonth())
                    ),
                    response -> {
                        String result = "수정되었습니다.";
                        Toast.makeText(ModifyGroupActivity.this, result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class );
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    },
                    error -> {

                    }
            );
        });


    }

}


