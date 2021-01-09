package com.example.study.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.github.irshulx.Editor;

import java.io.IOException;

public class ImageLoadFromDeviceToEditorUtil {
    public static void load(int requestCode, int resultCode, Intent data, Editor editor, Context context){
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap;
                if (android.os.Build.VERSION.SDK_INT >= 29){
                    // To handle deprication use
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getContentResolver(),uri));
                } else{
                    // Use older version
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
                }
                editor.insertImage(bitmap);
            } catch (IOException e) {
                Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(context.getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            // editor.RestoreState();
        }
    }
}
