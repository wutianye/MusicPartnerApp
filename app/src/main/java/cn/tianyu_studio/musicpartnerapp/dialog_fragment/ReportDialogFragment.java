package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;

@ContentView(R.layout.dialog_report)
public class ReportDialogFragment extends BaseDialogFragment {

    //private ChatActivity chatActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //chatActivity = (ChatActivity) getActivity();
    }

    @Event(R.id.dialog_report_tv_violenceAndPornography)
    private void reportByViolence(View view) {
        Toast.makeText(getActivity(), "举报成功：黄色信息", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Event(R.id.dialog_report_tv_illegality)
    private void reportByIllegality(View view) {
        Toast.makeText(getActivity(), "举报成功：违法", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Event(R.id.dialog_report_tv_advertisement)
    private void reportByAdvertisement(View view) {
        Toast.makeText(getActivity(), "举报成功：广告", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Event(R.id.dialog_report_tv_other)
    private void reportByOther(View view) {
        Toast.makeText(getActivity(), "举报成功：其他", Toast.LENGTH_SHORT).show();
        dismiss();
    }

}
