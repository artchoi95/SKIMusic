package com.example.theodore.staffline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Bitmap;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * Created by theodore on 3/26/2016.
 */
public class Note implements Comparable<Note>{
    private Mat img;
    private Rect rect;
    private int pitch;
    private int pitchX;
    private int pitchY;
    private int type;

    public Note()
    {

    }

    public Mat getImg() {
        return img;
    }

    public void setImg(Mat img) {
        this.img = img;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getPitchX() {
        return pitchX;
    }

    public void setPitchX(int pitchX) {
        this.pitchX = pitchX;
    }

    public int getPitchY() {
        return pitchY;
    }

    public void setPitchY(int pitchY) {
        this.pitchY = pitchY;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        if(type == 1)
        {
             pitchY = (int)(rect.y + (rect.height * 0.87));
             pitchX = (int)(rect.x + (rect.width  * 0.71));
        }
    }

    @Override
    public int compareTo(Note comparestu) {
        int compareage=((Note)comparestu).getPitchX();
        /* For Ascending order*/
        return this.pitchX-compareage;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
}
