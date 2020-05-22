package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;

/**
 * 聊天界面右上角按钮弹出的对话框，包括举报和屏蔽
 */
@ContentView(R.layout.dialog_chat_more)
public class MoreDialogFragment extends BaseDialogFragment {

    // private ChatActivity chatActivity;
    @ViewInject(R.id.dialog_chatMore_tv_toggleShielding)
    private TextView tv_toggleShielding;

    private boolean isShielding = false;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // chatActivity = (ChatActivity) getActivity();
        Bundle bundle = getArguments();
        //  isShielding = bundle.getBoolean(ChatActivity.KEY_IS_SHIELDING);
        if (isShielding) {
            tv_toggleShielding.setText(R.string.dialog_chatMore_cancelShieldingThisPerson);
        } else {
            tv_toggleShielding.setText(R.string.dialog_chatMore_shieldingThisPerson);
        }
    }

    @Event(R.id.dialog_chatMore_ll_report)
    private void report(View view) {
        dismiss();
        ReportDialogFragment reportDialogFragment = new ReportDialogFragment();
        reportDialogFragment.show(getFragmentManager(), "report");
    }

    @Event(R.id.dialog_chatMore_ll_toggleShielding)
    private void toggleShield(View view) {
        if (isShielding) {
            //   chatActivity.cancelShieldingThisPerson();
        } else {
            //   chatActivity.shieldingThisPerson();
        }
        dismiss();
    }

}
