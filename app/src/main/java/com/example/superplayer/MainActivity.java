package com.example.superplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gl.playerlibrary.VideoPlayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, VideoPlayer.OnNetChangeListener{

    private VideoPlayer player;
    private boolean isLive;

    /**
     * 测试地址
     */
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        initPlayer();
    }

    /**
     * 初始化相关的信息
     */
    private void initData() {
        isLive = getIntent().getBooleanExtra("isLive", false);
        url = "https://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
    }

    /**
     * 初始化视图
     */
    private void initView() {
        findViewById(R.id.tv_replay).setOnClickListener(this);
        findViewById(R.id.tv_play_location).setOnClickListener(this);
        findViewById(R.id.tv_play_switch).setOnClickListener(this);
    }

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        player = (VideoPlayer) findViewById(R.id.view_super_player);
        if (isLive) {
            player.setLive(true);//设置该地址是直播的地址
        }
        player.setNetChangeListener(true)//设置监听手机网络的变化
                .setOnNetChangeListener(this)//实现网络变化的回调
                .onPrepared(new VideoPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared() {
                        /**
                         * 监听视频是否已经准备完成开始播放。（可以在这里处理视频封面的显示跟隐藏）
                         */
                    }
                }).onComplete(new Runnable() {
            @Override
            public void run() {
                /**
                 * 监听视频是否已经播放完成了。（可以在这里处理视频播放完成进行的操作）
                 */
            }
        }).onInfo(new VideoPlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                /**
                 * 监听视频的相关信息。
                 */

            }
        }).onError(new VideoPlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
                /**
                 * 监听视频播放失败的回调
                 */

            }
        }).setTitle(url)//设置视频的titleName
                .play(url);//开始播放视频
        player.setScaleType(VideoPlayer.SCALETYPE_FITXY);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_replay) {
            if (player != null) {
                player.play(url);
            }
        } else if (view.getId() == R.id.tv_play_location) {
            if (isLive) {
                Toast.makeText(this, "直播不支持指定播放", Toast.LENGTH_SHORT).show();
                return;
            }
            if (player != null) {
                /**
                 * 这个节点是根据视频的大小来获取的。不同的视频源节点也会不一致（一般用在退出视频播放后保存对应视频的节点从而来达到记录播放）
                 */
                player.play(url, 89528);
            }
        } else if (view.getId() == R.id.tv_play_switch) {
            /**
             * 切换视频播放源（一般是标清，高清的切换ps：由于我没有找到高清，标清的视频源，所以也是换相同的地址）
             */
            if (isLive) {
                player.playSwitch(url);
            } else {
                player.playSwitch("http://baobab.wandoujia.com/api/v1/playUrl?vid=2614&editionType=high");
            }
        }
    }

    /**
     * 网络链接监听类
     */
    @Override
    public void onWifi() {
        Toast.makeText(this, "当前网络环境是WIFI", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMobile() {
        Toast.makeText(this, "当前网络环境是手机网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisConnect() {
        Toast.makeText(this, "网络链接断开", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoAvailable() {
        Toast.makeText(this, "无网络链接", Toast.LENGTH_SHORT).show();
    }


    /**
     * 下面的这几个Activity的生命状态很重要
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

}