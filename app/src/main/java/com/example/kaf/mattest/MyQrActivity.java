package com.example.kaf.mattest;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


public class MyQrActivity extends AppCompatActivity {

  public final static int WIDTH = 400;
  public final static int HEIGHT = 400;
  public final static String STR = "Sender Acc No: 1109845635, Paid Amount: 50, Receiver Ac No: 2209845997, Receiver Name: Mr. Merchant";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_qr);
    ImageView imageView = (ImageView) findViewById(R.id.qrImage);
    try {
      Bitmap bitmap = encodeAsBitmap(STR);
      imageView.setImageBitmap(bitmap);
    } catch (WriterException e) {
      e.printStackTrace();
    }
  }

  Bitmap encodeAsBitmap(String str) throws WriterException {
    BitMatrix result;
    try {
      result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
    } catch (IllegalArgumentException iae) {
      // Unsupported format
      return null;
    }

    int width = result.getWidth();
    int height = result.getHeight();
    int[] pixels = new int[width * height];
    for (int y = 0; y < height; y++) {
      int offset = y * width;
      for (int x = 0; x < width; x++) {
        pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
      }
    }

    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    return bitmap;
  }
}
