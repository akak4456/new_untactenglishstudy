package com.example.study.chat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.board.vo.BoardItemVO;
import com.example.study.chat.adapter.ChatAdapter;
import com.example.study.chat.adapter.ChatMemberListAdapter;
import com.example.study.chat.vo.ChatRequest;
import com.example.study.chat.vo.ChatResponse;
import com.example.study.member.vo.MemberEntity;
import com.example.study.global.CommonHeader;
import com.example.study.global.MyMemberInfo;
import com.example.study.R;
import com.example.study.constant.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class GeneralChatRoomActivity extends AppCompatActivity {

    private Context context;

    private Long gno;

    private StompClient mStompClient;

    private Gson gson = new Gson();

    private EditText edittext;

    private ListView lv;
    private ChatAdapter adapter;
    private CompositeDisposable compositeDisposable;

    private static final String TAG = "SOCKET";
    private static final String SOCKET_ADDRESS = Constant.SOCKET_ROOT_ADDRESS + "/endpoint/websocket";
    private static final String SOCKET_PUBLIC_SEND_PREFIX = "/pub/send/";
    private static final String SOCKET_PUBLIC_HELLO_PREFIX = "/pub/hello/";
    private static final String SOCKET_SUBSCRIPTION_PREFIX = "/sub/group/";

    private List<MemberEntity> memberList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_chat_room);
        context = this;

        lv = findViewById(R.id.listview_chat);
        edittext = findViewById(R.id.edittext);
        //테스트
        adapter = new ChatAdapter();
        lv.setAdapter(adapter);
        gno = getIntent().getLongExtra("gno",0L);
        Button btn_show_chat_member = findViewById(R.id.btn_show_chat_member);
        btn_show_chat_member.setOnClickListener(view->{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_chat_member_list, null);
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            int width = (int) (dm.widthPixels * 0.8);
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            Log.i("AAA",width+"");
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.showAtLocation(((Activity)context).findViewById(R.id.btn_show_chat_member), Gravity.RIGHT, 0, 0);
            ListView lv = popupView.findViewById(R.id.chat_member_list);
            ChatMemberListAdapter adapter = new ChatMemberListAdapter(memberList);
            lv.setAdapter(adapter);
        });
        Button button = findViewById(R.id.button_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 내 채팅내용 전송
                MemberEntity member = MyMemberInfo.getInstance().getMember();
                ChatRequest chatVO = new ChatRequest(member.getMno(),member.getName(),edittext.getText().toString());
                String json = gson.toJson(chatVO);
                sendMsg(json);
                edittext.setText("");
            }
        });
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_ADDRESS);
        resetSubscriptions();
        connectStomp();
    }
    private void sendMsg(String json){
        mStompClient
                .send(SOCKET_PUBLIC_SEND_PREFIX+gno,json)
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
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

        Disposable dispLifeCycle = mStompClient.lifecycle()
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.i(TAG, "Stomp Connection Opened");
                            break;
                        case ERROR:
                            Log.d(TAG, "Error ", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.w(TAG, "Stomp Connection Closed");

                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.d(TAG, "Failed Server Heartbeat ");
                            break;
                    }
                });
        compositeDisposable.add(dispLifeCycle);

        Disposable dispTopic = mStompClient.topic(SOCKET_SUBSCRIPTION_PREFIX+gno,headers).subscribe(topicMessage -> {
            //구독한 뒤에 receive할때 이 부분이 처리됨
            Log.d(TAG, topicMessage.getPayload());
            final ChatResponse response = gson.fromJson(topicMessage.getPayload(), ChatResponse.class);
            String json = new Gson().toJson(response.getUserList());
            Type listType = new TypeToken<List<MemberEntity>>(){}.getType();
            List<MemberEntity> list = new Gson().fromJson(json,listType);
            if(list != null) {
                memberList.clear();
                memberList.addAll(list);
            }
            new Thread(() -> runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    // 해당 작업을 처리함
                    adapter.addItem(response);
                    lv.setAdapter(adapter);
                    lv.setSelection(adapter.getCount() - 1);
                }
            })).start();
        });
        compositeDisposable.add(dispTopic);
        mStompClient.connect();
        sendHelloMsg();
    }
    private void sendHelloMsg(){
        mStompClient
                .send(SOCKET_PUBLIC_HELLO_PREFIX+gno)
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                });
    }
}
