package com.example.study.ranking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.study.R;
import com.example.study.constant.Constant;
import com.example.study.engcontent.adapter.QuizPagerAdapter;
import com.example.study.global.CommonHeader;
import com.example.study.ranking.adapter.RankGameAdapter;
import com.example.study.ranking.vo.RankGameRequest;
import com.example.study.ranking.vo.RankGameResponse;
import com.example.study.ranking.vo.Problem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class RankingGameActivity extends AppCompatActivity {
    private static final String SOCKET_ADDRESS = Constant.SOCKET_ROOT_ADDRESS + "/endpoint/websocket";
    private static final String SOCKET_SUBSCRIPTION_PREFIX = "/sub/rankgame/";
    private static final String SOCKET_PUBLIC_HELLO_PREFIX = "/pub/hellogame/";
    private static final String SOCKET_PUBLIC_FINISHGAME_PREFIX = "/pub/finishgame/";
    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;
    private Gson gson = new Gson();

    private TextView tv_remain_time;
    private Long gno;
    private Context context;
    private RankGameAdapter pagerAdapter;
    private ViewPager viewPager;
    private CountDownTimer CDT = null;
    private int remain_time = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_game);
        tv_remain_time = findViewById(R.id.tv_remain_time);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        viewPager = findViewById(R.id.viewPager_quiz);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_ADDRESS);
        connectStomp();
    }

    @Override
    public void onDestroy() {
        mStompClient.disconnect();
        if (compositeDisposable != null) compositeDisposable.dispose();
        CDT.cancel();
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
            final RankGameResponse response = gson.fromJson(topicMessage.getPayload(), RankGameResponse.class);
            String kind = response.getKind();
            if(kind.equals("init")){
                String json = new Gson().toJson(response.getProblems().getProblems());
                Type listType = new TypeToken<List<Problem>>(){}.getType();
                List<Problem> list = new Gson().fromJson(json,listType);
                new Thread(() -> runOnUiThread(() -> {
                    pagerAdapter = new RankGameAdapter(context, list);
                    viewPager.setOffscreenPageLimit(list.size());
                    viewPager.setAdapter(pagerAdapter);
                    tv_remain_time.setText("남은시간\n"+remain_time+"초");
                    if (CDT == null) {
                        CDT = new CountDownTimer(100*1000,1000){
                            public void onTick(long millisUntilFinished){
                                //반복 실행할 구문
                                remain_time--;
                                tv_remain_time.setText("남은시간\n"+remain_time+"초");
                            }
                            public void onFinish(){
                                //마지막에 실행할 구문
                                RankGameRequest req = new RankGameRequest(pagerAdapter.getUserAnswer());
                                String json = gson.toJson(req);
                                mStompClient
                                        .send(SOCKET_PUBLIC_FINISHGAME_PREFIX+gno,json)
                                        .subscribe(() -> {
                                            Log.d("RANKWAITSOCKET", "STOMP echo send successfully");
                                        }, throwable -> {
                                            Log.e("RANKWAITSOCKET", "Error send STOMP echo", throwable);
                                        });
                                Toast.makeText(context,"게임이 끝났습니다. 잠시 뒤에 결과화면으로 이동합니다.",Toast.LENGTH_SHORT).show();
                            }
                        };
                        CDT.start();
                    }
                })).start();
            }else if(kind.equals("finish")){
                String first_member = response.getFirst_member();

                String second_member = response.getSecond_member();

                String third_member = response.getThird_member();

                Long first_prize = response.getFirst_prize();

                Long second_prize = response.getSecond_prize();

                Long third_prize = response.getThird_prize();
                new Thread(() -> runOnUiThread(() -> {
                    Intent intent = new Intent(context, RankingResultActivity.class );
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("first_member",first_member);
                    intent.putExtra("second_member",second_member);
                    intent.putExtra("third_member",third_member);
                    intent.putExtra("first_prize",first_prize);
                    intent.putExtra("second_prize",second_prize);
                    intent.putExtra("third_prize",third_prize);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                })).start();
            }
        });
        compositeDisposable.add(dispTopic);
        mStompClient.connect();
        sendHelloMsg();
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
