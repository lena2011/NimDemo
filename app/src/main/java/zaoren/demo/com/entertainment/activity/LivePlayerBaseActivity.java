package zaoren.demo.com.entertainment.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;

import butterknife.BindView;
import butterknife.ButterKnife;
import zaoren.demo.com.R;
import zaoren.demo.com.base.BaseActivity;
import zaoren.demo.com.base.util.log.DebugLog;
import zaoren.demo.com.constant.LiveType;
import zaoren.demo.com.permission.MPermission;

/**
 * Created by Administrator on 2018/1/4.
 */

public abstract class LivePlayerBaseActivity extends BaseActivity {
    protected final int LIVE_PERMISSION_REQUEST_CODE = 100;

    protected abstract int getActvityLayout();

    @BindView(R.id.master_avatar)
    ImageView mMasterAvatar;
    @BindView(R.id.master_name)
    TextView mMasterName;
    @BindView(R.id.online_count_text)
    TextView mOnlineCountText;
    @BindView(R.id.room_id)
    TextView mRoomId;
    @BindView(R.id.master_layout)
    LinearLayout mMasterLayout;

    protected LiveType mLiveType;
    protected String roomId;
    private boolean isDestroyed = false;
    private AbortableFuture<EnterChatRoomResultData> enterRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActvityLayout());
        ButterKnife.bind(this);
    }

    protected static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE
    };

    protected void requestPermissions() {
        MPermission.with(this).addRequestCode(LIVE_PERMISSION_REQUEST_CODE).permissions(LIVE_PERMISSIONS).request();
    }


    /**
     * 进入聊天室
     */
    public void enterRoom() {
        if (isDestroyed) {
            return;
        }
        EnterChatRoomData data = new EnterChatRoomData(roomId);
        setEnterRoomExtension(data);
        enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoom(data);
        enterRequest.setCallback(new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData result) {
                DebugLog.i("onSuccess");
                Toast.makeText(LivePlayerBaseActivity.this, "成功进入聊天室", Toast.LENGTH_SHORT).show();
                updateUI(result);
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_CHATROOM_BLACKLIST) {
                    Toast.makeText(LivePlayerBaseActivity.this, "您已被拉入黑名单，不能再进入", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LivePlayerBaseActivity.this, "enter chat room failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(LivePlayerBaseActivity.this, "enter chat room exception, e=" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 扩展模式，并显示给观众
     *
     * @param data
     */
    private void setEnterRoomExtension(EnterChatRoomData data) {

    }

    protected void updateUI(EnterChatRoomResultData result) {
        mMasterLayout.setVisibility(View.VISIBLE);
        mMasterName.setText(result.getRoomInfo().getName());
        mOnlineCountText.setText(result.getRoomInfo().getOnlineUserCount());
        mRoomId.setText(result.getRoomId());
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        super.onDestroy();

    }
}
