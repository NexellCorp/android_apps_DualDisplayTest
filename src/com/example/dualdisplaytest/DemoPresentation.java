package com.example.dualdisplaytest;

import android.app.Presentation;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.widget.VideoView;

public class DemoPresentation extends Presentation {
    private static final String TAG = "TVOutView";

    public DemoPresentation(Context context, Display display) {
        super(context, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        Resources r = getContext().getResources();
        setContentView(R.layout.presentation_content);

        //showVideo();
        showTextScroll();
    }

    //private void showVideo() {
         //VideoView videoView = (VideoView)findViewById(R.id.image);
         //videoView.setVideoURI(Uri.parse("/mnt/media_rw/sdcard1/Demo_h264_video/CF1.avi"));
         //videoView.start();
    //}

    private void showTextScroll() {
         TextScrollView textView = (TextScrollView) findViewById(R.id.TextScrollView1);
         textView.setText("Here is TVOUT Display~~");
         textView.setSpeed(2.0f);
         textView.init();
         textView.startScroll();
    }

}
