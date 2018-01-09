package zaoren.demo.com.entertainment.http;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zaoren.demo.com.DemoCache;
import zaoren.demo.com.base.http.BaseHttpClient;
import zaoren.demo.com.base.http.NimHttpClient;
import zaoren.demo.com.base.util.StringUtil;
import zaoren.demo.com.base.util.log.DebugLog;
import zaoren.demo.com.base.util.log.LogUtil;
import zaoren.demo.com.constant.BasicApi;

/**
 * Created by Administrator on 2018/1/8.
 */

public class ChatRoomHttpClient extends BaseHttpClient {

    public class EnterRoomParam {
        private String roomId;//创建房间成功返回的房间号
        private String pushUrl;//推流地址
        private String pullUrl;//拉流地址
        private String avType;
        private int orientation;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getPushUrl() {
            return pushUrl;
        }

        public void setPushUrl(String pushUrl) {
            this.pushUrl = pushUrl;
        }

        public String getPullUrl() {
            return pullUrl;
        }

        public void setPullUrl(String pullUrl) {
            this.pullUrl = pullUrl;
        }

        public String getAvType() {
            return avType;
        }

        public void setAvType(String avType) {
            this.avType = avType;
        }

        public int getOrientation() {
            return orientation;
        }

        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }
    }

    private static final String REQUEST_USER_UID = "uid"; // 用户id
    private static final String REQUEST_ROOM_ID = "roomid"; // 直播间id
    private static final String REQUEST_ROOM_EXT = "ext"; // 直播间扩展字段
    private static final String REQUEST_AVTYPE = "avType"; // 主播直播类型
    private static final String REQUEST_ORIENTATION = "orientation"; // 主播直播方向
    // param
    public static final String KEY_VIDEO = "VIDEO";
    public static final String KEY_AUDIO = "AUDIO";

    private static ChatRoomHttpClient instance;

    public static synchronized ChatRoomHttpClient getInstance() {
        if (instance == null) {
            instance = new ChatRoomHttpClient();
        }
        return instance;
    }

    private ChatRoomHttpClient() {
        NimHttpClient.getInstance().init(DemoCache.getContext());
    }


    public void masterEnterRoom(String account, String ext, boolean isVideoMode, boolean isPortrait, final HttpCallback<EnterRoomParam> callback) {
        String url = BasicApi.API_NAME_MASTER_ENTRANCE;

        StringBuilder body = new StringBuilder();
        body.append(REQUEST_USER_UID).append("=").append(account).append("&")
                .append(REQUEST_ROOM_EXT).append("=").append(ext).append("&")
                .append(REQUEST_AVTYPE).append("=").append(isVideoMode ? KEY_VIDEO : KEY_AUDIO).append("&")
                .append(REQUEST_ORIENTATION).append("=").append(isPortrait ? 1 : 2);
        String bodyString = body.toString();

        NimHttpClient.getInstance().execute(url, getHeaders(), bodyString, new NimHttpClient.NimHttpCallback() {
            @Override
            public void onResponse(String response, int code, String errorMsg) {
                if (code != 0) {
                    DebugLog.e("masterEnterRoom failed : code = " + code + ", errorMsg = " + errorMsg);
                    if (callback != null) {
                        callback.onFailed(code, errorMsg);
                    }
                    return;
                }
                try {
                    JSONObject res = JSONObject.parseObject(response);
                    int resCode = res.getIntValue(RESULT_KEY_RES);
                    if (resCode == RESULT_CODE_SUCCESS) {
                        JSONObject msg = res.getJSONObject(RESULT_KEY_MSG);
                        EnterRoomParam param = new EnterRoomParam();
                        param.setRoomId(msg.getString(RESULT_KEY_ROOM_ID));
                        if (msg != null) {
                            JSONObject live = msg.getJSONObject(RESULT_KEY_LIVE);
                            param.setPushUrl(live.getString(RESULT_KEY_PUSH_URL));
                            param.setPullUrl(live.getString(RESULT_KEY_PULL_URL));
                        }
                        callback.onSuccess(param);
                    } else {
                        callback.onFailed(resCode, res.getString(RESULT_KEY_ERROR_MSG));
                    }
                } catch (JSONException e) {
                    callback.onFailed(-1, e.getMessage());
                }
            }
        });
    }
}
