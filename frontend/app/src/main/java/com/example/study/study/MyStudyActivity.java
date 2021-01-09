package com.example.study.study;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.BuildConfig;
import com.example.study.R;
import com.example.study.attendance.AttendanceCheckActivity;
import com.example.study.board.BoardAddActivity;
import com.example.study.board.BoardListActivity;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.board.vo.BoardPageMaker;
import com.example.study.chat.EnglishChatRoomActivity;
import com.example.study.chat.GeneralChatRoomActivity;
import com.example.study.conference.SessionActivity;
import com.example.study.engcontent.activity.QuizMainActivity;
import com.example.study.engcontent.activity.VocaListActivity;
import com.example.study.global.CommonHeader;
import com.example.study.leader.LeaderViewActivity;
import com.example.study.ranking.activity.RankingMainActivity;
import com.example.study.study.adapter.NoticeListAdapter;
import com.example.study.study.vo.GroupInfoVO;
import com.example.study.study.vo.GroupItemVO;
import com.example.study.timetable.RepresentativeTimeTableOneActivity;
import com.example.study.timetable.TimeTableListActivity;
import com.example.study.timetable.vo.RepresentativeTimeTableVO;
import com.example.study.timetable.vo.TimeTableItem;
import com.example.study.timetableview.Schedule;
import com.example.study.timetableview.Time;
import com.example.study.timetableview.TimetableView;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyStudyActivity extends AppCompatActivity {
    private ListView lv_notice;
    private Context context;
    private Long gno;
    private NoticeListAdapter adapter;
    private GroupInfoVO vo;

    private TimetableView timetable;
    private ArrayList<Schedule> schedules;
    private TextView timetable_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystudy);
        gno = getIntent().getLongExtra("gno",0L);
        context = this;
        lv_notice = findViewById(R.id.lv_notice);
        new JsonRequestUtil<BoardPageMaker>(context).request(
                Request.Method.GET,
                "/board/kind/"+gno+"/NOTICE",
                BoardPageMaker.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    String json = new Gson().toJson(response.getResult().getContent());
                    Type listType = new TypeToken<List<BoardItemVO>>(){}.getType();
                    List<BoardItemVO> list = new Gson().fromJson(json,listType);
                    adapter = new NoticeListAdapter(list,gno);
                    lv_notice.setAdapter(adapter);
                },
                error -> {

                }
        );//공지사항 불러오기
        TextView groupname = findViewById(R.id.groupname);
        TextView groupnumber = findViewById(R.id.groupnumber);
        new JsonRequestUtil<GroupInfoVO>(context).request(
                Request.Method.GET,
                "/study/one/"+gno,
                GroupInfoVO.class,
                CommonHeader.getInstance().getCommonHeader(),
                response->{
                    vo = response;
                    groupname.setText(response.getGroup().getTitle());
                    Long cur = response.getMemberCount();
                    Long total = response.getGroup().getMaximumNumberOfPeople();
                    groupnumber.setText("인원수:"+cur+"/"+total);
                },
                error->{

                }
        );
        timetable = findViewById(R.id.timetable);
        timetable_title = findViewById(R.id.timetable_title);
        schedules = new ArrayList<>();
        new JsonRequestUtil<RepresentativeTimeTableVO>(context).request(
                Request.Method.GET,
                "/timetable/represent/"+gno,
                RepresentativeTimeTableVO.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    String json = new Gson().toJson(response.getTimeTableItem());
                    Type listType = new TypeToken<List<TimeTableItem>>(){}.getType();
                    List<TimeTableItem> timeTableItem = new Gson().fromJson(json,listType);
                    if(response.getTitle() != null) {
                        for (TimeTableItem item : timeTableItem) {
                            Schedule schedule = new Schedule();
                            schedule.setTitle(item.getTitle());
                            schedule.setDetail(item.getDetail());
                            schedule.setDay(item.getDay());
                            Time startTime = new Time(item.getStartHour(), item.getStartMinute());
                            schedule.setStartTime(startTime);
                            Time endTime = new Time(item.getEndHour(), item.getEndMinute());
                            schedule.setEndTime(endTime);
                            schedules.add(schedule);
                        }
                        timetable_title.setText(response.getTitle());
                        timetable.add(schedules);
                    }
                },
                error -> {

                }
        );

        Button btn_leader = findViewById(R.id.btn_leader);
        btn_leader.setOnClickListener(view -> {
            new JsonRequestUtil<String>(context).request(
                    Request.Method.GET,
                    "/study/try/leader/"+gno,
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response->{
                        // 리더만 입장 가능 설정 필요
                        Intent intent = new Intent(context, LeaderViewActivity.class );
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("gno",gno);
                        context.startActivity(intent);
                    },
                    error->{
                        //리더가 아니면 입장 불가 메시지
                        Toast myToast = Toast.makeText(this.getApplicationContext(),"그룹리더가 아닙니다.", Toast.LENGTH_SHORT);
                        myToast.show();
                    }
            );
        });

        Button btn_attendance = findViewById(R.id.btn_attendance);
        btn_attendance.setOnClickListener(view->{
            Intent intent = new Intent(context, AttendanceCheckActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });

        Button btn_timetable = findViewById(R.id.btn_timetable);
        btn_timetable.setOnClickListener(view->{
            Intent intent = new Intent(context, TimeTableListActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });

        Button btn_invite = findViewById(R.id.btn_invite);
        btn_invite.setOnClickListener(view->{
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Untact English Study");//앱 이름
                String shareMessage= "\n스터디에 당신을 초대합니다.\n\n";
                shareMessage = shareMessage + "https://www.untact.com/join/"+vo.getGroup().getGno()+"/"+vo.getGroup().getInviteCode()+"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        });

        Button btn_info = findViewById(R.id.btn_info);
        btn_info.setOnClickListener(view -> {
            Intent intent = new Intent(context, StudyInfoActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });

        Button btn_board = findViewById(R.id.btn_board);
        btn_board.setOnClickListener(view->{
            Intent intent = new Intent(context, BoardListActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });



        Button btn_chat = findViewById(R.id.btn_chat);
        btn_chat.setOnClickListener(view->{
            Intent intent = new Intent(context, GeneralChatRoomActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });

        Button btn_meet = findViewById(R.id.btn_meet);
        btn_meet.setOnClickListener(view->{
            Intent intent = new Intent(getApplicationContext(), SessionActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });

        Button btn_quiz = findViewById(R.id.btn_quiz);
        btn_quiz.setOnClickListener(view->{
            Intent intent = new Intent(getApplicationContext(), QuizMainActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });

        Button btn_engchat = findViewById(R.id.btn_engchat);
        btn_engchat.setOnClickListener(view->{
            Intent intent = new Intent(context, EnglishChatRoomActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });

        Button btn_voca = findViewById(R.id.btn_voca);
        btn_voca.setOnClickListener(view->{
            Intent intent = new Intent(context, VocaListActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });

        Button btn_rank = findViewById(R.id.btn_rank);
        btn_rank.setOnClickListener(view->{
            Intent intent = new Intent(context, RankingMainActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            context.startActivity(intent);
        });

        Button btn_notify_add = findViewById(R.id.btn_notify_add);
        btn_notify_add.setOnClickListener(view->{
            Intent intent = new Intent(context, BoardAddActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            intent.putExtra("kind","NOTICE");
            startActivity(intent);
        });
    }
}

