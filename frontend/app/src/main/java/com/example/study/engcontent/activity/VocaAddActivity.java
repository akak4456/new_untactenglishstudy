package com.example.study.engcontent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.engcontent.adapter.VocaAddAdapter;
import com.example.study.engcontent.vo.BufferedReaderFactory;
import com.example.study.engcontent.vo.VocabularyRequest;
import com.example.study.global.CommonHeader;
import com.example.study.util.JsonRequestUtil;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VocaAddActivity extends AppCompatActivity {
    private Long gno;
    EditText editText;
    TextView textView;
    String _path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
    private ListView listView;
    private VocaAddAdapter adapter;
    private ArrayList<String> array_path;
    private Context mContext;
    private EditText voca_title;

    private String[] permissions = {

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int MULTIPLE_PERMISSIONS = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_add);
        mContext = this;
        gno = getIntent().getLongExtra("gno",0L);
        editText = (EditText)findViewById(R.id.et_filename);
        voca_title = findViewById(R.id.voca_title);

        checkPermissions();
        Button button = (Button) findViewById(R.id.btn_add);
        button.setOnClickListener(new ButtonReadClickListener(this));


        // 파일 목록 리스트뷰로 불러오기
        array_path = new ArrayList<>();
        File directory = new File(_path);
        File[] files = directory.listFiles();

        if (files != null) {
            for(int i = 0; i<files.length; i++){
                array_path.add(files[i].getName());
            }
        }


        adapter = new VocaAddAdapter(this, array_path);
        listView = (ListView) findViewById(R.id.storage_list);
        listView.setAdapter(adapter);

        // 리스트뷰 파일목록 클릭 했을때
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String filename = (String) parent.getItemAtPosition(position) ;
                editText.setText(filename);

            }
        }) ;

    }




    // 텍스트 파일 읽어오기
    class ButtonReadClickListener implements View.OnClickListener {
        Context context;

        public ButtonReadClickListener(Context context) {
            this.context = context;
        }

        public void onClick(View v) {
            String path = _path;
            String textFileName;

            textFileName = editText.getText().toString();

            BufferedReader br = null;

            //-----------------------------------------------------
            // path 경로로부터 textFileName 파일을 읽습니다.

            try {
                Pattern englishPattern = Pattern.compile("[a-zA-Z]");

                br = BufferedReaderFactory.create(path, textFileName);

                String temp;
                StringBuilder contentGetter = new StringBuilder();
                ArrayList<String> wordList = new ArrayList<>();
                temp = br.readLine();

                for (; temp != null; temp = br.readLine()) {
                    String result = "";
                    Matcher matcher = englishPattern.matcher(temp);
                    while(matcher.find()){
                        result += matcher.group();
                    }
                    result = result.toLowerCase();
                    wordList.add(result);
                    contentGetter.append(result+'\n');

                }

                // 사용자에게 알림창으로 추가하려는 단어장이 맞는지 확인
                AlertDialog.Builder builder = new AlertDialog.Builder(VocaAddActivity.this);
                builder.setTitle("내용");
                builder.setMessage(contentGetter.toString());
                builder.setPositiveButton("확인", (dialog, id) -> {
                    new JsonRequestUtil<String>(mContext).request(
                            Request.Method.POST,
                            "/vocabulary/"+gno,
                            String.class,
                            CommonHeader.getInstance().getCommonHeader(),
                            new VocabularyRequest(voca_title.getText().toString(),wordList),
                            response->{
                                Toast.makeText(mContext,"단어장 추가에 성공했습니다!",Toast.LENGTH_SHORT).show();
                                ((Activity)mContext).finish();
                            },
                            error->{
                                Toast.makeText(mContext,"단어장 추가에 실패했습니다!",Toast.LENGTH_SHORT).show();
                            }
                    );
                });
                builder.setNegativeButton("취소", null);
                builder.create().show();

            }

            // 파일이 존재하지 않는 경우에 대한 예외 처리
            catch (FileNotFoundException e) {
                String exceptionMessage = path + File.separator +textFileName + " 파일 또는 그것의 경로가 존재하지 않습니다.";
                Toast.makeText(this.context, exceptionMessage, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            // 입출력 관련된 예외 처리
            catch (IOException e) {
                String exceptionMessage = "파일을 읽는 도중에 오류가 발생했습니다.";
                Toast.makeText(this.context, exceptionMessage, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            // 기타 예외 처리
            catch (Exception e) {
                Toast.makeText(this.context, "알 수 없는 오류입니다.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            // 파일을 닫을 때
            try {
                if (null != br)
                    br.close();
            }
            catch (IOException e) {
                Toast.makeText(this.context, textFileName + "파일을 닫을 수 없습니다.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    };

    // 권한 체크
    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            result = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }

        if (!permissionList.isEmpty()) {
            // 확보해야할 권한이 있음
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[i])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(this, "권한 요청에 동의하지 않으면 이용이 불가합니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                } else {
                    Toast.makeText(this,
                            "권한 요청에 동의하지 않으면 이용이 불가합니다.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}