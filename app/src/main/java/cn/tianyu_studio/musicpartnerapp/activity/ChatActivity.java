package cn.tianyu_studio.musicpartnerapp.activity;

import org.xutils.view.annotation.ContentView;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.util.BaseActivity;

@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

//    SharedPreferences sharedPreferences;
//    @ViewInject(R.id.chat_tv_title)
//    private TextView tv_title;
//    @ViewInject(R.id.chat_msg_list)
//    private MessageList messageList;
//    public static final String KEY_IS_SHIELDING = "shield";
//    //摄像头和录音权限
//    private static String[] CHAT_PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
//    //请求状态码
//    private static int REQUEST_PERMISSION_CODE = 1;
//    @ViewInject(R.id.chat_chatInputView)
//    private ChatInputView chatInputView;
//    @ViewInject(R.id.chat_pullToRefreshLayout)
//    private PullToRefreshLayout ptrLayout;
//  //  private MsgListAdapter<Message> msgAdapter;
//    private boolean isShielding = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // 如果没有权限，则请求权限
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, CHAT_PERMISSIONS, REQUEST_PERMISSION_CODE);
//        }
//
//        sharedPreferences = getSharedPreferences(SysConsts.SP_NAME, MODE_PRIVATE);
//        tv_title.setText(getIntent().getStringExtra("name"));
//        initPullToRefreshLayout();
//        initMessageList();
//        initChatInputView();
//    }
//
//    private void initChatInputView() {
//        chatInputView.setMenuContainerHeight(819);
//        chatInputView.setMenuClickListener(new OnMenuClickListener() {
//            @Override
//            public boolean onSendTextMessage(CharSequence input) {
//                // 输入框输入文字后，点击发送按钮事件
//                Message message = new Message(input.toString(), IMessage.MessageType.SEND_TEXT.ordinal());
//                message.setUserInfo(new User("1", "Ironman", "R.drawable.author1"));
//                message.setTimeString(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date()));
//                message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//                msgAdapter.addToStart(message, true);
//                return true;
//            }
//
//            @Override
//            public void onSendFiles(List<FileItem> list) {
//                // 选中文件或者录制完视频后，点击发送按钮触发此事件
//            }
//
//            @Override
//            public boolean switchToMicrophoneMode() {
//                // 点击语音按钮触发事件，显示录音界面前触发此事件
//                // 返回 true 表示使用默认的界面，若返回 false 应该自己实现界面
//                return true;
//            }
//
//            @Override
//            public boolean switchToGalleryMode() {
//                // 点击图片按钮触发事件，显示图片选择界面前触发此事件
//                // 返回 true 表示使用默认的界面
//                return true;
//            }
//
//            @Override
//            public boolean switchToCameraMode() {
//                // 点击拍照按钮触发事件，显示拍照界面前触发此事件
//                // 返回 true 表示使用默认的界面
//                return true;
//            }
//
//            @Override
//            public boolean switchToEmojiMode() {
//                return true;
//            }
//        });
//    }
//
//    private void initPullToRefreshLayout() {
//        PtrDefaultHeader header = new PtrDefaultHeader(this);
//        int[] colors = {R.color.app_color};
//        header.setColorSchemeColors(colors);
//        header.setLayoutParams(new PullToRefreshLayout.LayoutParams(-1, -2));
//        header.setPadding(0, DisplayUtil.dp2px(this, 15), 0, DisplayUtil.dp2px(this, 10));
//        header.setPtrFrameLayout(ptrLayout);
//        ptrLayout.setLoadingMinTime(1000);
//        ptrLayout.setDurationToCloseHeader(1500);
//        ptrLayout.setHeaderView(header);
//        ptrLayout.addPtrUIHandler(header);
//        // 如果设置为 true，下拉刷新时，内容固定，只有 Header 变化
//        ptrLayout.setPinContent(true);
//        ptrLayout.setPtrHandler(layout -> {
//            LogUtil.e("pull refresh");
//            // loadNextPage();
//            // 加载完历史消息后调用
//            ptrLayout.refreshComplete();
//        });
//    }
//
//    private void initMessageList() {
//        ImageLoader imageLoader = new ImageLoader() {
//            @Override
//            public void loadAvatarImage(ImageView avatarImageView, String string) {
//                if (string.contains("R.drawable")) {
//                    Integer resId = getResources().getIdentifier(string.replace("R.drawable.", ""), "drawable", getPackageName());
//                    LogUtil.e("resouce: " + resId);
//                    avatarImageView.setImageResource(resId);
//                    return;
//                }
////                Glide.with(getApplicationContext()).load(string).into(avatarImageView);
//                x.image().bind(avatarImageView, string);
//            }
//
//            @Override
//            public void loadImage(ImageView imageView, String string) {
////                Glide.with(getApplicationContext()).load(string).into(imageView);
//                x.image().bind(imageView, string);
//            }
//
//            @Override
//            public void loadVideo(ImageView imageCover, String uri) {
////                Glide.with(getApplicationContext()).load(uri).into(imageCover);
//                x.image().bind(imageCover, uri);
//            }
//        };
//        msgAdapter = new MsgListAdapter<>("1", imageLoader);
//        messageList.setAdapter(msgAdapter);
//
//        Message message = new Message("你好", IMessage.MessageType.SEND_TEXT.ordinal());
//        message.setUserInfo(new User("1", "Ironman", "R.drawable.author1"));
//        message.setTimeString(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date()));
//        message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//        msgAdapter.addToStart(message, true);
//
//        Message voiceMessage = new Message("", IMessage.MessageType.RECEIVE_VOICE.ordinal());
//        voiceMessage.setUserInfo(new User("0", "Deadpool", "R.drawable.author2"));
//        voiceMessage.setMediaFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/testvoice.mp3");
//        voiceMessage.setDuration(4);
//        voiceMessage.setMessageStatus(IMessage.MessageStatus.RECEIVE_SUCCEED);
//        msgAdapter.addToStart(voiceMessage, true);
//
//        Message sendVoiceMsg = new Message("", IMessage.MessageType.SEND_VOICE.ordinal());
//        sendVoiceMsg.setUserInfo(new User("1", "Ironman", "R.drawable.author1"));
//        sendVoiceMsg.setMediaFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/testvoice.mp3");
//        sendVoiceMsg.setDuration(4);
//        sendVoiceMsg.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//        msgAdapter.addToStart(sendVoiceMsg, true);
//
//        Message receiveText = new Message(getString(R.string.test_content), IMessage.MessageType.RECEIVE_TEXT.ordinal());
//        receiveText.setUserInfo(new User("0", "Deadpool", "R.drawable.author2"));
//        receiveText.setTimeString(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date()));
//        receiveText.setMessageStatus(IMessage.MessageStatus.RECEIVE_SUCCEED);
//        msgAdapter.addToStart(receiveText, true);
//
//        Message receiveImage = new Message("", IMessage.MessageType.RECEIVE_IMAGE.ordinal());
//        receiveImage.setMediaFilePath("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1722027871,290689511&fm=27&gp=0.jpg");
//        receiveImage.setDuration(4);
//        receiveImage.setUserInfo(new User("0", "Deadpool", "R.drawable.author2"));
//        msgAdapter.addToStart(receiveImage, true);
//
//    }
//
//    public void shieldingThisPerson() {
//        isShielding = true;
//        Message eventMsg = new Message("你已经屏蔽了该用户，不会再接收到他的消息", IMessage.MessageType.EVENT.ordinal());
//        msgAdapter.addToStart(eventMsg, true);
//    }
//
//    public void cancelShieldingThisPerson() {
//        isShielding = false;
//        Message eventMsg = new Message("你已经取消屏蔽该用户", IMessage.MessageType.EVENT.ordinal());
//        msgAdapter.addToStart(eventMsg, true);
//    }
//
//    @Event(R.id.chat_iv_more)
//    private void showOperationDialog(View view) {
//        MoreDialogFragment moreDialogFragment = new MoreDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(KEY_IS_SHIELDING, isShielding);
//        moreDialogFragment.setArguments(bundle);
//        moreDialogFragment.show(getFragmentManager(), "chatoperation");
//    }
//
//    @Event(R.id.chat_tv_back)
//    private void back(View view) {
//        finish();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSION_CODE) {
//            for (int i = 0; i < permissions.length; i++) {
//                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                    LogUtil.w("聊天权限被拒");
//                    Toast.makeText(this, "权限不足，应用可能无法正常使用", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//            }
//        }
//    }
}
