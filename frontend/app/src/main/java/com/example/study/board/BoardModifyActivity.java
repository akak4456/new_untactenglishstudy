package com.example.study.board;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.study.board.vo.BoardVO;
import com.example.study.global.CommonHeader;
import com.example.study.R;
import com.example.study.constant.Constant;
import com.example.study.util.ImageLoadFromDeviceToEditorUtil;
import com.example.study.util.JsonRequestUtil;
import com.example.study.util.ParseHtmlToFileEntityVO;
import com.example.study.util.UploadImageUtil;
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;

import org.json.JSONException;
import org.json.JSONObject;

public class BoardModifyActivity extends AppCompatActivity {

    private Editor editor;

    private EditText board_modify_title;

    //upload된 image들의 경로들
    private Context context;

    private BoardVO board;

    private Long mno;

    private Long bno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_modify);
        context = this;
        editor = (Editor) findViewById(R.id.editor);
        board_modify_title = (EditText) findViewById(R.id.board_modify_title);
        setUpEditor();
        board = (BoardVO)getIntent().getParcelableExtra("board");
        board_modify_title.setText(board.getTitle());
        bno = getIntent().getLongExtra("bno",0L);
        editor.render(board.getContent());
        mno = getIntent().getLongExtra("mno",0L);
        findViewById(R.id.btnRender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JsonRequestUtil<String>(context).request(
                        Request.Method.PUT,
                        "/board/"+bno+"/"+mno,
                        String.class,
                        CommonHeader.getInstance().getCommonHeader(),
                        new BoardVO(
                                board_modify_title.getText().toString(),
                                editor.getContentAsHTML(),
                                ParseHtmlToFileEntityVO.parse(editor.getContentAsHTML())
                        ),
                        response -> {
                            Toast.makeText(context,"성공", Toast.LENGTH_SHORT).show();
                            finish();
                        },//성공 할때 행동
                        error -> {

                        }
                );
            }
        });
    }

    private void setUpEditor(){
        findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H1);
            }
        });

        findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H2);
            }
        });

        findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H3);
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BOLD);
            }
        });

        findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.ITALIC);
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.INDENT);
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.OUTDENT);
            }
        });

        findViewById(R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(false);
            }
        });

        findViewById(R.id.action_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextColor("#FF3333");
            }
        });

        findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(true);
            }
        });

        findViewById(R.id.action_hr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertDivider();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertLink();
            }
        });


        findViewById(R.id.action_erase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clearAllContents();
            }
        });


        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
            }

            @Override
            public void onUpload(Bitmap image, final String uuid) {
                /**
                 * TODO do your upload here from the bitmap received and all onImageUploadComplete(String url); to insert the result url to
                 * let the editor know the upload has completed
                 */
                new UploadImageUtil(context).uploadImage(
                        image,
                        uuid,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                try {

                                    JSONObject obj = new JSONObject(new String(response.data));
                                    String url = obj.getString("url");
                                    editor.onImageUploadComplete(Constant.rootAddress+"/download/"+url, uuid);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e("GotError",""+error.getMessage());
                            }
                        });
                //서버에 image upload
            }

        });
        editor.render();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageLoadFromDeviceToEditorUtil.load(requestCode,resultCode,data,editor,context);

    }
}