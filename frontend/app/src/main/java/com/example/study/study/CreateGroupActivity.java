package com.example.study.study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.study.global.CommonHeader;
import com.example.study.R;
import com.example.study.main.MainActivity;
import com.example.study.study.vo.GroupAddVO;
import com.example.study.util.JsonRequestUtil;

import java.util.Date;

//버튼(id = Group_create) 클릭하면 db에 정보 저장
//다시 studyfragment로 돌아감

public class CreateGroupActivity extends AppCompatActivity {

    DatePicker mDate;
    TextView mTxtDate;

    EditText mGroupName;
    EditText mGroupDeposit;
    EditText mGroupType;
    EditText mGroupIntro;
    EditText mGroupMax;
    EditText mGroupMaxAbsence;
    EditText mGroupLateFine;
    EditText mGroupAbsenceFine;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        context = this;

        mGroupName = findViewById(R.id.Group_name);
        mGroupDeposit = findViewById(R.id.Group_deposit);
        mGroupType = findViewById(R.id.Group_type);
        mGroupIntro = findViewById(R.id.Group_intro);
        mGroupMaxAbsence = findViewById(R.id.Group_max_absence);
        mGroupMax = findViewById(R.id.Group_max);
        mGroupLateFine = findViewById(R.id.Group_late_fine);
        mGroupAbsenceFine = findViewById(R.id.Group_absence_fine);
        mDate = findViewById(R.id.datepicker);
        mTxtDate = findViewById(R.id.Group_period);

        // 날짜 선택
        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) -> mTxtDate.setText(String.format("%d/%d/%d",year,monthOfYear+1,dayOfMonth)));
        mDate.setMinDate(new Date().getTime());
        // 개설 버튼
        Button button = findViewById(R.id.Group_create);
        button.setOnClickListener(view -> {
            //나중에 클라이언트 쪽에서 이 데이터가 적절한지 확인해야함
            new JsonRequestUtil<String>(getApplicationContext()).request(
                    Request.Method.POST,
                    "/study",
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
                        String result = "개설되었습니다.";
                        Toast.makeText(CreateGroupActivity.this, result, Toast.LENGTH_SHORT).show();
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


