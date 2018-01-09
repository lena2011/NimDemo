package zaoren.demo.com.constant;

import com.netease.nimlib.sdk.avchat.constant.AVChatType;

/**
 * Created by Administrator on 2018/1/9.
 */

public enum LiveType {
    /**
     * 未直播
     */
    NOT_ONLINE(-1),
    /**
     * 视频类型
     */
    VIDEO_TYPE(AVChatType.VIDEO.getValue()),
    /**
     * 语音类型
     */
    AUDIO_TYPE(AVChatType.AUDIO.getValue());
    private int value;

    LiveType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
