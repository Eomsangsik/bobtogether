package com.yesjam.bobtogether.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.yesjam.bobtogether.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RotateImgDialog extends Dialog implements View.OnClickListener {

    private CircleImageView circleImageView;
    private Button rotateBtn;
    private Button okBtn;
    private Bitmap imgBitmap;
    private int angle = 0;

    public RotateImgDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rotate_img_dialog);

        circleImageView = (CircleImageView) findViewById(R.id.rotate_img_dialog_imgView);
        rotateBtn = (Button) findViewById(R.id.rotate_img_dialog_rotate_btn);
        okBtn = (Button) findViewById(R.id.rotate_img_dialog_ok_btn);
        circleImageView.setOnClickListener(this);
        rotateBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
        circleImageView.setImageBitmap(imgBitmap);
    }

    @Override
    public void onClick(View view) {

        if (view == rotateBtn) {

            imgBitmap = rotateBitmap(imgBitmap, angle += 90);
            circleImageView.setImageBitmap(imgBitmap);
        }
        if (view == okBtn) {

            View root = getLayoutInflater().inflate(
                    (R.layout.activity_make_bob), null);

            CircleImageView makeBobCircleImageView = (CircleImageView) root.findViewById(R.id.make_bob_img);
            makeBobCircleImageView.setImageBitmap(imgBitmap);

            dismiss();
        }
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
