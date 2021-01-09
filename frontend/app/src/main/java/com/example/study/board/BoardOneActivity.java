package com.example.study.board;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.global.CommonHeader;
import com.example.study.R;
import com.example.study.board.vo.BoardVO;
import com.example.study.reply.ReplyActivity;
import com.example.study.util.JsonRequestUtil;
import com.github.irshulx.Editor;


public class BoardOneActivity extends AppCompatActivity {
    Editor editor;

    private Context context;//HTTP REQUEST AND RESPONSE를 위해서 필요함
    Long gno;
    Long bno;
    Long mno;
    Button modify_btn;
    Button delete_btn;
    Button reply_btn;
    BoardVO board;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_one);
        gno = getIntent().getLongExtra("gno",0L);
        bno = getIntent().getLongExtra("bno",0L);
        context = this;//HTTP REQUEST AND RESPONSE를 위해서 필요함
        editor = findViewById(R.id.boardoneview);
        modify_btn = findViewById(R.id.modify_btn);
        title = findViewById(R.id.title);
        modify_btn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BoardModifyActivity.class );
                intent.putExtra("bno",bno);
                intent.putExtra("mno",mno);
                intent.putExtra("board",  board);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        delete_btn = findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(view -> new JsonRequestUtil<String>(context).request(
                Request.Method.DELETE,
                "/board/"+bno+"/"+mno,
                String.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    Toast.makeText(context,"성공", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> Toast.makeText(context,error.getMessage(), Toast.LENGTH_SHORT).show()

        ));
        reply_btn = findViewById(R.id.reply_btn);
        reply_btn.setOnClickListener(view->{
            Intent intent = new Intent(context, ReplyActivity.class );
            intent.putExtra("gno",gno);
            intent.putExtra("bno",bno);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

    }
    @Override
    public void onStart(){
        super.onStart();
        editor.clearAllContents();
        new JsonRequestUtil<BoardVO>(context).request(
                Request.Method.GET,
                "/board/"+gno+"/"+bno,
                BoardVO.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    board = response;
                    title.setText("제목:"+board.getTitle());
                    try {
                        editor.render(response.getContent());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    mno = response.getMember().getMno();
                },
                error -> {

                }
        );
    }
}
