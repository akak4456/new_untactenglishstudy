package com.example.study.leader.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.attendance.vo.Attendance;
import com.example.study.global.CommonHeader;
import com.example.study.leader.AttendanceManageActivity;
import com.example.study.leader.MemberManageActivity;
import com.example.study.leader.vo.AttendanceVO;
import com.example.study.leader.vo.MemberManageVO;
import com.example.study.util.JsonRequestUtil;

import java.util.List;

public class AttendanceManageAdapter extends BaseAdapter {
    private static final String ATTENDANCE = "OK";
    private static final String ABSENT = "ABSENT";
    private static final String LATE = "LATE";
    private List<AttendanceVO> data;
    private Long gno;
    public AttendanceManageAdapter(List<AttendanceVO> list, Long gno){
        data = list; this.gno = gno;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public AttendanceVO getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        final Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.activity_attendance_manage_item, null);

        AttendanceVO item = data.get(position);

        TextView name = view.findViewById(R.id.name);

        Button btn_modify = view.findViewById(R.id.btn_modify);
        btn_modify.setVisibility(View.INVISIBLE);
        ImageButton attendance = view.findViewById(R.id.attendance);
        ImageButton absent = view.findViewById(R.id.absent);
        ImageButton late = view.findViewById(R.id.late);
        attendance.setOnClickListener(v->{
            attendance.setImageResource(android.R.color.transparent);
            absent.setImageResource(android.R.color.transparent);
            late.setImageResource(android.R.color.transparent);

            attendance.setTag("uncheck");
            absent.setTag("uncheck");
            late.setTag("uncheck");

            attendance.setImageResource(R.drawable.check_icon);
            attendance.setTag("check");

            if(item.getStatus().equals(ATTENDANCE)){
                btn_modify.setVisibility(View.INVISIBLE);
            }else{
                btn_modify.setVisibility(View.VISIBLE);
            }
        });
        absent.setOnClickListener(v->{
            attendance.setImageResource(android.R.color.transparent);
            absent.setImageResource(android.R.color.transparent);
            late.setImageResource(android.R.color.transparent);

            attendance.setTag("uncheck");
            absent.setTag("uncheck");
            late.setTag("uncheck");

            absent.setImageResource(R.drawable.check_icon);
            absent.setTag("check");

            if(item.getStatus().equals(ABSENT)){
                btn_modify.setVisibility(View.INVISIBLE);
            }else{
                btn_modify.setVisibility(View.VISIBLE);
            }
        });
        late.setOnClickListener(v->{
            attendance.setImageResource(android.R.color.transparent);
            absent.setImageResource(android.R.color.transparent);
            late.setImageResource(android.R.color.transparent);

            attendance.setTag("uncheck");
            absent.setTag("uncheck");
            late.setTag("uncheck");

            late.setImageResource(R.drawable.check_icon);
            late.setTag("check");

            if(item.getStatus().equals(LATE)){
                btn_modify.setVisibility(View.INVISIBLE);
            }else{
                btn_modify.setVisibility(View.VISIBLE);
            }
        });
        btn_modify.setOnClickListener(v->{
            String oldStatus = item.getStatus();
            String newStatus = "";
            if(attendance.getTag().equals("check")){
                newStatus = ATTENDANCE;
            }else if(absent.getTag().equals("check")){
                newStatus = ABSENT;
            }else if(late.getTag().equals("check")){
                newStatus = LATE;
            }
            new JsonRequestUtil<String>(context).request(
                    Request.Method.PUT,
                    "/leader/attendance/"+gno+"/"+item.getMno()+"/"+item.getAno()+"/"+oldStatus+"/"+newStatus,
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response -> {
                        Toast.makeText(context,"출석 조정 성공",Toast.LENGTH_SHORT).show();
                        reload(context);
                    },
                    error->{
                        Toast.makeText(context,"출석 조정 실패",Toast.LENGTH_SHORT).show();
                    }
            );
        });

        name.setText(item.getName());
        if(item.getStatus().equals(ATTENDANCE)){
            attendance.setImageResource(R.drawable.check_icon);
        }else if(item.getStatus().equals(LATE)){
            late.setImageResource(R.drawable.check_icon);
        }else if(item.getStatus().equals(ABSENT)){
            absent.setImageResource(R.drawable.check_icon);
        }
        return view;
    }
    private void reload(Context ctx){
        Activity activity = (Activity)ctx;
        activity.finish();

        Intent intent = new Intent(ctx, AttendanceManageActivity.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("gno",gno);
        activity.startActivity(intent);
    }
}
