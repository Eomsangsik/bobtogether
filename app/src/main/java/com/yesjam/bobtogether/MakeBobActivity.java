package com.yesjam.bobtogether;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.shawnlin.numberpicker.NumberPicker;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yesjam.bobtogether.Dialog.RotateImgDialog;
import com.yesjam.bobtogether.Preferences.UserDataPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import de.hdodenhof.circleimageview.CircleImageView;

public class MakeBobActivity extends AppCompatActivity {

    private CircleImageView foodImg;
    private EditText title;
    private EditText place;
    private Button sendBob;

    private String yymmdd;
    private String hhmm;
    private String yymmdd2;
    private String hhmm2;
    private String ymdhm;
    private Bitmap image_bitmap;
    private String foodImgString = "default";

    private TextView datePickerTv;
    private TextView timePickerTv;
    private NumberPicker numberPicker;
    private ConnServer connServer;
    private ProgressDialog progressDialog;


    private AsyncHttpClient asyncHttpClient;
    private JSONObject jsonObject;
    private ByteArrayEntity byteArrayEntity;

    private UserDataPreferences userDataPreferences;
    private RotateImgDialog rotateImgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_bob);

        init();
        listner();

    }

    private void init() {

        userDataPreferences = new UserDataPreferences(MakeBobActivity.this);
        foodImg = (CircleImageView) findViewById(R.id.make_bob_img);
        title = (EditText) findViewById(R.id.make_bob_title_et);
        place = (EditText) findViewById(R.id.make_bob_palce_et);
        sendBob = (Button) findViewById(R.id.make_bob_send_btn);
        datePickerTv = (TextView) findViewById(R.id.make_bob_date_picker);
        timePickerTv = (TextView) findViewById(R.id.make_bob_time_picker);
        numberPicker = (NumberPicker) findViewById(R.id.make_bob_number_picker);
        connServer = new ConnServer(MakeBobActivity.this);
        progressDialog = new ProgressDialog(MakeBobActivity.this);
        progressDialog.setMessage(StringKor.PROGRESS_DIALOG);
        rotateImgDialog = new RotateImgDialog(MakeBobActivity.this);

    }

    private void listner() {

        foodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MakeBobActivity.this, foodImg.getResources().toString(), Toast.LENGTH_SHORT).show();
                pickPic();
            }
        });

        sendBob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText() == null || title.getText().equals("")
                        || place.getText() == null || place.getText().equals("")
                        || datePickerTv.getText() == null || datePickerTv.getText().equals("")
                        || timePickerTv.getText() == null || timePickerTv.getText().equals("")) {
                    Toast.makeText(MakeBobActivity.this, "내용을 전부 작성해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    toServer();
                }

            }
        });

        datePickerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MakeBobActivity.this, dateSetListener, 2017, 6, 1).show();
            }
        });

        timePickerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(MakeBobActivity.this, timeSetListener, 12, 35, false).show();
            }
        });

    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            yymmdd = String.format("%d년 %d월 %d일", year, monthOfYear + 1, dayOfMonth);
            yymmdd2 = String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth);
            datePickerTv.setText(yymmdd);
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hhmm = String.format("%d시 %d분", hourOfDay, minute);
            hhmm2 = String.format("%d:%d", hourOfDay, minute);
            timePickerTv.setText(hhmm);
        }
    };

    private void pickPic() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Etc.REQ_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Etc.REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    image_bitmap = decodeUri(data.getData());

                    rotateImgDialog.show();

//                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    foodImg.setImageBitmap(image_bitmap);
                    foodImgString = getStringFromBitmap(image_bitmap);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 100;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public Bitmap rotateImage(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {

        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    private void toServer() {
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
        jsonObject = new JSONObject();

        try {
            jsonObject.put("foodImg", foodImgString);
            jsonObject.put("title", title.getText());
            jsonObject.put("place", place.getText());
            jsonObject.put("time", yymmdd2 + " " + hhmm2);
            jsonObject.put("tNumber", numberPicker.getValue());
            jsonObject.put("email", userDataPreferences.getEmail());
            byteArrayEntity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            jsonObject = null;
            System.gc();    //jsonObject 갹체를 재활용 하기 위해 기사용한 객체는 가비지콜렉션.
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        asyncHttpClient.post(MakeBobActivity.this, Etc.MAKE_BOB, byteArrayEntity, "application/json", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MakeBobActivity.this, "실패", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(MakeBobActivity.this, "성공", Toast.LENGTH_SHORT).show();
                title.setText("");
                place.setText("");

                connServer.callChats();
                connServer.callBobs();
                progressDialog.dismiss();
                finish();
            }
        });
    }

}
