package com.feicui.android.videoplayer.full;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.feicui.android.videoplayer.R;

import io.vov.vitamio.widget.MediaController;

/**
 * Created by Administrator on 2016/10/26.
 */
public class CustomMediaController extends MediaController {
    private MediaPlayerControl mediaPlayerControl;

    private final AudioManager audioManager;
    private Window window;//视频亮度管理
    private final int maxVolume;//最大音量
    private int currentVolume;//当前音量
    private float currentBrightness;//当前亮度

    public CustomMediaController(Context context) {
        super(context);
        audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        window = ((Activity)context).getWindow();
    }

    @Override
    protected View makeControllerView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_custom_video_controller,this);
        initView(view);
        return view;
    }



    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
        mediaPlayerControl = player;
    }
    private void initView(View view) {
        ImageButton btnFastForward = (ImageButton) view.findViewById(R.id.btnFastForward);
        btnFastForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 快进按钮
                long postion = mediaPlayerControl.getCurrentPosition();
                postion += 10000;//快进10秒
                if(postion>= mediaPlayerControl.getDuration()){
                    postion = mediaPlayerControl.getDuration();
                }
                mediaPlayerControl.seekTo(postion);
            }
        });
        ImageButton btnFastRewind = (ImageButton) view.findViewById(R.id.btnFastRewind);
        btnFastRewind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 快退按钮
                long postion = mediaPlayerControl.getCurrentPosition();
                postion -= 10000;//快退10秒
                if(postion<= 0){
                    postion = 0;
                }
                mediaPlayerControl.seekTo(postion);
            }
        });
        final View adjustView = view.findViewById(R.id.adjustView);

        final GestureDetector gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float startX = e1.getX();//开始的X轴
                float startY = e1.getY();//开始的Y轴
                float endX = e2.getX();//结束的X轴
                float endY = e2.getY();//结束的Y轴
                float width = adjustView.getWidth();//拿到视图的宽
                float height = adjustView.getHeight();//拿到视图的高
                float percentage = (startY-endY)/height;//高度滑动的百分比

                //左侧：亮度
                if(startX<width /4){
                    //调整亮度
                    adjustbRrightness(percentage);
                }
                if (startX > width*3/4){
                    //调整音量
                    adjustVolume(percentage);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        //对view进行touch监听
        //但是我们不去做touch事件的处理，交给GestureDetector处理
        adjustView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //判断用户的操作是不是一个摁下的操作
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){
                    //当前音量
                    currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    //当前亮度
                    currentBrightness = window.getAttributes().screenBrightness;
                }
                //传给gD用户初始操作，后续gD自行处理
                gestureDetector.onTouchEvent(event);
                // 在调整的过程中，一直显示
                show();
                return true;
            }
        });
    }

    //调整音量
    private void adjustVolume(float percentage) {
        //最终音量 = 最大音量*百分比 + 当前音量
//       音量增大   35       =   100*15%      +   20
//       音量减小   5        =   100*-15%     +   20
        int volume = (int) ((maxVolume * percentage) + currentVolume);
        //最终音量是否大于最大音量
        volume = volume > maxVolume ? maxVolume : volume;
        //最终音量是否小于最小音量
        volume = volume < 0 ? 0 : volume;
        //设置音量
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,
                audioManager.FLAG_SHOW_UI);
    }
    //调整亮度
    private void adjustbRrightness(float percentage) {
        float brightness = percentage+currentBrightness;
        brightness = brightness > 1.0f ? 1.0f : brightness;
        brightness = brightness < 0 ? 0 : brightness;
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = brightness;
        window.setAttributes(layoutParams);
    }
}
