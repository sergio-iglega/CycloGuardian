package com.example.sergi.conexioncamara;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class LiveActivity extends AppCompatActivity {
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        videoView = (VideoView) this.findViewById(R.id.rtspVideo);
    }

    public void startVideoLive(View view) {
        rtspStream("rtsp://192.168.42.1/live");
    }

    private void rtspStream(String rtspUrl) {
        videoView.setVideoURI(Uri.parse(rtspUrl));
        videoView.requestFocus();
        videoView.start();
    }
}
