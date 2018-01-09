package zaoren.demo.com.entertainment.helper;

import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatData;

import zaoren.demo.com.base.util.StringUtil;
import zaoren.demo.com.base.util.log.DebugLog;

/**
 * Created by Administrator on 2018/1/9.
 */

public class MicHelper {
    private static MicHelper instance;

    public static synchronized MicHelper getInstance() {
        if (instance == null)
            instance = new MicHelper();
        return instance;
    }

    public interface ChannelCallback {
        void onJoinChannelSuccess();

        void onJoinChannelFailed();
    }

    public void joinChannel(String meetingName, boolean isVideo, final ChannelCallback channelCallback) {
        if (StringUtil.isEmpty(meetingName)) {
            return;
        }
        AVChatManager.getInstance().joinRoom2(meetingName, isVideo ? AVChatType.VIDEO : AVChatType.AUDIO, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData avChatData) {
                DebugLog.i("joinRoom2");
                channelCallback.onJoinChannelSuccess();
            }

            @Override
            public void onFailed(int code) {
                channelCallback.onJoinChannelFailed();
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }
}
