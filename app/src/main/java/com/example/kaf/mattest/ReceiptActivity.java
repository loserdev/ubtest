package com.example.kaf.mattest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;





public class ReceiptActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ImageView v= (ImageView) findViewById(R.id.test);

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.aa__triangle);

        BitmapDrawable tile = new BitmapDrawable(getResources(),bitmap);


        int width = v.getWidth();
        int height = tile.getIntrinsicHeight();
        Rect bounds = new Rect(0,0,width,height);
        tile.setTileModeX(Shader.TileMode.REPEAT);
        tile.setBounds(bounds);
        v.setBackground(tile);
        v.setMaxHeight(height);
        v.setMinimumHeight(height);
    }
}
