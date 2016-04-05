package com.example.theodore.staffline;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Bitmap;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;


public class MainActivity extends Activity {

    Button chooseImage, binarizeAdaptive, binarizeOtsu;
    ImageView displayImage;
    Mat sourceImage;
    Mat tempImage;
    Mat horizontalStructure;
    Mat verticalStructure;
    Mat output;
    int horizontalSize;
    int verticalSize;
    static final int SELECT_PHOTO = 12345;

    /* Static Initialization of OpenCV */
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.i("OpenCV","OpenCV Initialization Failed ");
        }
        else {
            Log.i("OpenCV", "OpenCV Initialization Successful ");
        }
    }

    /* Initialization of the activity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chooseImage = (Button) findViewById(R.id.button);
        displayImage = (ImageView) findViewById(R.id.binarized);
        displayImage.setScaleType(ImageView.ScaleType.FIT_XY);
        displayImage.setAdjustViewBounds(true);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        binarizeAdaptive = (Button) findViewById(R.id.binarizeAdaptive);
        binarizeAdaptive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binarizeImageAdaptive();
            }
        });

        binarizeOtsu = (Button) findViewById(R.id.button3);
        binarizeOtsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binarizeImageOtsu();
            }
        });
    }

    /* Function that will binarize image using otsu thresholding */
    public void binarizeImageOtsu() {

        try {
            sourceImage = Utils.loadResource(this,R.drawable.realimage5,
                                Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Mat mDest = new Mat(sourceImage.rows(), sourceImage.cols(), sourceImage.type());
            Imgproc.threshold(sourceImage, mDest, 0, 255,
                                Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
            Bitmap bmp = Bitmap.createBitmap(mDest.cols(), mDest.rows(), Bitmap.Config.RGB_565);
            // convert Mat image to bitmap::
            Utils.matToBitmap(mDest, bmp);
            displayImage.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Function that will binarize image using adaptive thresholding */
    public void binarizeImageAdaptive() {

        try {
            sourceImage = Utils.loadResource(this,R.drawable.realimage5,
                                Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Mat mDest = new Mat(sourceImage.rows(), sourceImage.cols(), sourceImage.type());
            Imgproc.GaussianBlur(sourceImage, sourceImage, new Size(25, 25), 2);
            Imgproc.adaptiveThreshold(sourceImage, mDest, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY, 41, 2);
            Bitmap bmp = Bitmap.createBitmap(mDest.cols(), mDest.rows(),Bitmap.Config.RGB_565);
            Utils.matToBitmap(mDest, bmp);
            displayImage.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* Calculate the inSampleSize value based on the requested width and height */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /* Display the chosen image of the user*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeResource(getResources(), R.id.binarized, options);
            options.inSampleSize = calculateInSampleSize(options, 720, 1280);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            displayImage.setImageBitmap(bitmap);
            cursor.close();
        }
    }

    /* Function that returns the removed staff lines of the source image */
    public Mat removedStaffline(){

        //get image to be modified
        try{
            sourceImage = Utils.loadResource(this,R.id.binarized,Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Core.bitwise_not(sourceImage, sourceImage);
            Imgproc.adaptiveThreshold(sourceImage,tempImage,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,
                                Imgproc.THRESH_BINARY,15,-2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //print dimensions of the image
        System.out.println("Source Image Horizontal Rows: " + sourceImage.rows());
        System.out.println("Source Image Vertical Columns: " + sourceImage.cols());

        //clone image
        Mat horizontal = tempImage.clone();
        Mat vertical = tempImage.clone();

        //perform opencv functions to get staff line and store the staff image into horizontal
        horizontalSize = horizontal.cols()/30;
        horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                            new Size(horizontalSize,1));
        Imgproc.erode(horizontal, horizontal, horizontalStructure);
        Imgproc.dilate(horizontal, horizontal, horizontalStructure);

        //perform opencv functions to remove staff line and store the removed staff image
        //into vertical
        verticalSize = vertical.rows() /150;
        verticalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                            new Size(1,verticalSize));
        Imgproc.erode(vertical, vertical, verticalStructure, new Point(-1, -1), 1);
        Imgproc.dilate(vertical, vertical, verticalStructure, new Point(-1, -1), 1);

        //subtract the staff line image to the original image and store it into output
        output = new Mat();
        Core.subtract(sourceImage, horizontal, output);

        //add output and vertical to get a better image and store into output
        Core.bitwise_not(output, output);
        Core.bitwise_not(vertical, vertical);
        Core.addWeighted(vertical, 0.7, output, 0.5, 0.0, output);

        //return the output
        return output;
    }
}



