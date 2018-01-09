package zaoren.demo.com.entertainment.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatMediaCodecMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCaptureOrientation;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCropRatio;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoFrameRate;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoQuality;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatLiveCompositingLayout;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import zaoren.demo.com.DemoCache;
import zaoren.demo.com.R;
import zaoren.demo.com.base.http.BaseHttpClient;
import zaoren.demo.com.base.util.StringUtil;
import zaoren.demo.com.base.util.log.DebugLog;
import zaoren.demo.com.constant.LiveType;
import zaoren.demo.com.constant.PushLinkConstant;
import zaoren.demo.com.entertainment.helper.MicHelper;
import zaoren.demo.com.entertainment.http.ChatRoomHttpClient;
import zaoren.demo.com.permission.MPermission;
import zaoren.demo.com.permission.annotation.OnMPermissionDenied;
import zaoren.demo.com.permission.annotation.OnMPermissionGranted;
import zaoren.demo.com.permission.annotation.OnMPermissionNeverAskAgain;
import zaoren.demo.com.permission.util.MPermissionUtil;

/**
 * Created by Administrator on 2017/12/28.
 */

public class LiveActivity extends LivePlayerBaseActivity {

    @BindView(R.id.surface_view)
    AVChatSurfaceViewRenderer mSurfaceView;
    @BindView(R.id.start_live)
    Button mStartLive;


    private AVChatCameraCapturer mVideoCapturer;//控制camera
    private String meetingName;//视频会议房间名称


    @Override
    protected int getActvityLayout() {
        return R.layout.act_live;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    @OnClick(R.id.start_live)
    void setStartLive() {
        if (AVChatManager.getInstance().checkPermission(LiveActivity.this).size()!=0){
            Toast.makeText(LiveActivity.this,"权限不足，请检查您的权限设置",Toast.LENGTH_SHORT).show();
            return;
        }
        mStartLive.setText("准备中...");
        createChannel();
    }

    /**
     * 创建房间
     */
    private void createChannel() {
        this.meetingName = StringUtil.get36UUID();
        DebugLog.i("createChannel meetingName"+ meetingName);
        AVChatManager.getInstance().createRoom(meetingName, null, new AVChatCallback<AVChatChannelInfo>() {
            @Override
            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                Toast.makeText(LiveActivity.this, "创建的房间名：" + meetingName, Toast.LENGTH_SHORT).show();
                DebugLog.i("createChannel createRoom");
                masterEnterRoom(mLiveType == LiveType.VIDEO_TYPE);
            }

            @Override
            public void onFailed(int code) {
                mStartLive.setText("开始直播");
                DebugLog.i("code"+code);
                Toast.makeText(LiveActivity.this, "创建直播间失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                DebugLog.i("e " + exception.getMessage());
            }
        });
    }

    private void masterEnterRoom(boolean isVideoMode) {
        Map<String, Object> ext = new HashMap<>();
        ext.put("type", AVChatType.VIDEO);
        ext.put(PushLinkConstant.meetingName, meetingName);
        JSONObject jsonObject = null;
        try {
            jsonObject = parseMap(ext);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        ChatRoomHttpClient.getInstance().masterEnterRoom(DemoCache.getAccount(), jsonObject.toString(), isVideoMode, isPortrait, new BaseHttpClient.HttpCallback<ChatRoomHttpClient.EnterRoomParam>() {
            @Override
            public void onSuccess(ChatRoomHttpClient.EnterRoomParam enterRoomParam) {
                // TODO: 2018/1/9  获取推流，加入聊天室，进入房间
                roomId = enterRoomParam.getRoomId();
                joinChannel(enterRoomParam.getPushUrl());
                enterRoom();
            }

            @Override
            public void onFailed(int code, String errorMsg) {

            }
        });
    }

    private void joinChannel(String pushUrl) {
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SESSION_LIVE_URL, pushUrl);
        MicHelper.getInstance().joinChannel(meetingName, mLiveType == LiveType.VIDEO_TYPE, new MicHelper.ChannelCallback() {
            @Override
            public void onJoinChannelSuccess() {
                //如果为语音
                if (mLiveType == LiveType.AUDIO_TYPE) {
                    AVChatManager.getInstance().setSpeaker(true);//设置扬声器
                }
                // TODO: 2018/1/9 发送断开连麦消息
//                MicHelper.getInstance().s
                DebugLog.i("joinChannel");
                dropQueue();
            }

            @Override
            public void onJoinChannelFailed() {

            }
        });
    }

    /**
     * 主播进来清空消息队列
     */
    private void dropQueue() {
        NIMClient.getService(ChatRoomService.class).dropQueue(roomId).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {

            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }


    @Override
    protected void updateUI(EnterChatRoomResultData result) {
        super.updateUI(result);
        mStartLive.setVisibility(View.GONE);
    }

    private JSONObject parseMap(Map map) throws JSONException {
        if (map == null)
            return null;
        JSONObject object = new JSONObject();
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String key = String.valueOf(entry.getKey());
            Object value = entry.getValue();
            object.put(key, value);
        }
        return object;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {
        Toast.makeText(LiveActivity.this, "授权成功", Toast.LENGTH_SHORT).show();

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startPreview();
            }
        }, 50);
    }


    @OnMPermissionDenied(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, LIVE_PERMISSIONS);
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions);
        Toast.makeText(LiveActivity.this, tip, Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionNeverAskAgain(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, LIVE_PERMISSIONS);
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermission(this, LIVE_PERMISSIONS);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("无法开启直播，请到系统设置页面开启权限");
        stringBuilder.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            stringBuilder.append(",下次询问请授予权限");
            stringBuilder.append(MPermissionUtil.toString(deniedPermissions));
        }

        Toast.makeText(LiveActivity.this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
    }


    private void startPreview() {
        AVChatManager.getInstance().enableRtc();
        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();//创建相机捕获器
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
        }

        AVChatParameters parameters = new AVChatParameters();
        parameters.setBoolean(AVChatParameters.KEY_SESSION_LIVE_MODE, true);
        //多人模式下用户角色设定,normal（允许发送和接受数据）,audience(仅接受数据，不在发送数据)
        parameters.setInteger(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
        parameters.setString(AVChatParameters.KEY_VIDEO_DECODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_SOFTWARE);
        //视频情绪度
        parameters.setInteger(AVChatParameters.KEY_VIDEO_QUALITY, AVChatVideoQuality.QUALITY_720P);
        //视频帧率，若用到美颜，建议设为15帧，不用，设为25帧
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FRAME_RATE, AVChatVideoFrameRate.FRAME_RATE_15);
        parameters.set(AVChatParameters.KEY_SESSION_LIVE_COMPOSITING_LAYOUT, new AVChatLiveCompositingLayout(AVChatLiveCompositingLayout.Mode.LAYOUT_FLOATING_RIGHT_VERTICAL));
        //视频画面菜裁剪比例
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, AVChatVideoCropRatio.CROP_RATIO_16_9);
        //视频绘制时自动旋转
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_ROTATE_IN_RENDING, true);
        int videoOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? AVChatVideoCaptureOrientation.ORIENTATION_PORTRAIT : AVChatVideoCaptureOrientation.ORIENTATION_LANDSCAPE_RIGHT;
        //视频采集方向，默认竖屏
        parameters.setInteger(AVChatParameters.KEY_VIDEO_CAPTURE_ORIENTATION, videoOrientation);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);

        AVChatManager.getInstance().setParameters(parameters);

        AVChatManager.getInstance().enableVideo();//打开视频模块
        //设置本地预览画布
        AVChatManager.getInstance().setupLocalVideoRender(mSurfaceView, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        AVChatManager.getInstance().startVideoPreview();//开启本地视频预览
        if (mVideoCapturer != null) {
            mVideoCapturer.setAutoFocus(true);//自动对焦
        }


    }
}
