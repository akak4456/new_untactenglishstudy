package com.example.study.ranking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;
import com.example.study.constant.Constant;
import com.example.study.global.CommonHeader;
import com.example.study.global.MyMemberInfo;
import com.example.study.leader.LeaderViewActivity;
import com.example.study.member.vo.MemberEntity;
import com.example.study.ranking.adapter.RankMemberListAdapter;
import com.example.study.ranking.vo.RankWaitingRequest;
import com.example.study.ranking.vo.RankWaitingResponse;
import com.example.study.timetableview.Schedule;
import com.example.study.timetableview.Time;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class RankingMainActivity extends AppCompatActivity {
    private Context context;
    private Long gno;

    private static final String SOCKET_ADDRESS = Constant.SOCKET_ROOT_ADDRESS + "/endpoint/websocket";
    private static final String SOCKET_SUBSCRIPTION_PREFIX = "/sub/rank/";
    private static final String SOCKET_PUBLIC_HELLO_PREFIX = "/pub/hellorank/";
    private static final String SOCKET_PUBLIC_RANKSTARTTRY_PREFIX = "/pub/rankstarttry/";
    private static final String SOCKET_PUBLIC_RANKSTART_PREFIX = "/pub/rankstart/";
    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;
    private Gson gson = new Gson();
    private Long savedTotalPeople;//disconnect broadcast 메시지를 받을 때 사용됨

    private List<MemberEntity> memberList;
    private RankMemberListAdapter adapter;
    private ListView lv;
    private TextView show_cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_main);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_ADDRESS);
        connectStomp();
        lv = findViewById(R.id.ll_wait);
        show_cnt = findViewById(R.id.show_cnt);
        memberList = new ArrayList<>();
        Button btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(view->{
            List<StompHeader> headers = new ArrayList<>();
            headers.add(new StompHeader("destination", SOCKET_PUBLIC_RANKSTARTTRY_PREFIX+gno));
            headers.add(new StompHeader("X-AUTH-TOKEN", CommonHeader.getInstance().getCommonHeader().get("X-AUTH-TOKEN")));
            StompMessage msg = new StompMessage("SEND",headers,"");
            mStompClient
                    .send(msg)
                    .subscribe(() -> {
                        Log.d("RANKWAITSOCKET", "STOMP echo send successfully");
                    }, throwable -> {
                        Log.e("RANKWAITSOCKET", "Error send STOMP echo", throwable);
                    });
        });
    }

    @Override
    public void onDestroy() {
        mStompClient.disconnect();
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    private void connectStomp(){
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("X-AUTH-TOKEN", CommonHeader.getInstance().getCommonHeader().get("X-AUTH-TOKEN")));
        resetSubscriptions();
        Disposable dispTopic = mStompClient.topic(SOCKET_SUBSCRIPTION_PREFIX+gno,headers).subscribe(topicMessage -> {
            //구독한 뒤에 receive할때 이 부분이 처리됨
            Log.i("TMP",topicMessage.getPayload());
            final RankWaitingResponse response = gson.fromJson(topicMessage.getPayload(), RankWaitingResponse.class);
            String kind = response.getKind();
            System.out.println(kind);
            if(kind.equals("list")||kind.equals("disconnect")) {
                String json = new Gson().toJson(response.getUserList());
                Type listType = new TypeToken<List<MemberEntity>>(){}.getType();
                List<MemberEntity> list = new Gson().fromJson(json,listType);
                memberList.clear();
                memberList.addAll(list);
                if(kind.equals("list"))
                    savedTotalPeople = response.getTotalPeople();
                new Thread(() -> runOnUiThread(() -> {
                    // 해당 작업을 처리함
                    adapter = new RankMemberListAdapter(memberList);
                    lv.setAdapter(adapter);
                    lv.setSelection(adapter.getCount() - 1);
                    show_cnt.setText(memberList.size()+"/"+savedTotalPeople);
                })).start();
            }else if(kind.equals("try")){
                if(MyMemberInfo.getInstance().getMember().getMno() == response.getTargetMno()){
                    if(response.getIsAccept().equals("Y")){
                        new Thread(() -> runOnUiThread(() -> {
                            showPopup();
                        })).start();
                    }else{
                        new Thread(() -> runOnUiThread(() -> {
                            Toast.makeText(context,"게임을 시작할 수 없습니다. 최소 3명이상이어야 하며 리더만이 시작 가능합니다.",Toast.LENGTH_SHORT).show();
                        })).start();

                    }
                }
            }else if(kind.equals("start")){

                if(response.getIsAccept().equals("N")){
                    if(MyMemberInfo.getInstance().getMember().getMno() == response.getTargetMno()){
                        new Thread(() -> runOnUiThread(() -> {
                            Toast.makeText(context,"상금은 0이상이어야 하며 모두 입력해야 합니다. 혹은 상금으로 걸 수 있는 벌금보다 더 큰 상금을 걸었습니다.",Toast.LENGTH_SHORT).show();
                        })).start();
                    }
                }else if(response.getIsAccept().equals("Y")){
                    //게임을 시작하기 위해 메인 화면을 닫음
                    //그리고 게임 화면을 보여줌
                    new Thread(() -> runOnUiThread(() -> {
                        Intent intent = new Intent(context, RankingGameActivity.class );
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("gno",gno);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    })).start();
                }
            }
        });
        compositeDisposable.add(dispTopic);
        mStompClient.connect();
        sendHelloMsg();
    }
    private void showPopup(){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_gamesetting, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(lv, Gravity.BOTTOM, 0, 0);

        EditText first_prize = popupView.findViewById(R.id.first_prize);
        EditText second_prize = popupView.findViewById(R.id.second_prize);
        EditText third_prize = popupView.findViewById(R.id.third_prize);

        Button btn_game_start = popupView.findViewById(R.id.btn_game_start);
        btn_game_start.setOnClickListener(v->{
            List<StompHeader> headers = new ArrayList<>();
            headers.add(new StompHeader("destination", SOCKET_PUBLIC_RANKSTART_PREFIX+gno));
            headers.add(new StompHeader("X-AUTH-TOKEN", CommonHeader.getInstance().getCommonHeader().get("X-AUTH-TOKEN")));
            try {
                Long first = Long.parseLong(first_prize.getText().toString());
                Long second = Long.parseLong(second_prize.getText().toString());
                Long third = Long.parseLong(third_prize.getText().toString());
                RankWaitingRequest request = new RankWaitingRequest(first,second,third);
                StompMessage msg = new StompMessage("SEND",headers,gson.toJson(request));
                mStompClient
                        .send(msg)
                        .subscribe(() -> {
                            Log.d("RANKWAITSOCKET", "STOMP echo send successfully");
                        }, throwable -> {
                            Log.e("RANKWAITSOCKET", "Error send STOMP echo", throwable);
                        });
                popupWindow.dismiss();
            }catch(Exception e){
                Toast.makeText(context,"숫자만 입력해주세요. 상금은 0이상이어야 합니다.(0포함)",Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_game_cancel = popupView.findViewById(R.id.btn_game_cancel);
        btn_game_cancel.setOnClickListener(v->{
            popupWindow.dismiss();
        });
    }
    private void sendHelloMsg(){
        mStompClient
                .send(SOCKET_PUBLIC_HELLO_PREFIX+gno)
                .subscribe(() -> {
                    Log.d("RANKWAITSOCKET", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("RANKWAITSOCKET", "Error send STOMP echo", throwable);
                });
    }
}
