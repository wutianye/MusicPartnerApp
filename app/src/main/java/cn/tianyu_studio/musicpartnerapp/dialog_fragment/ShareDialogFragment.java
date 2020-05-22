package cn.tianyu_studio.musicpartnerapp.dialog_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.activity.FillInformationActivity;
import cn.tianyu_studio.musicpartnerapp.util.BaseDialogFragment;


@ContentView(R.layout.dialog_play_share)
public class ShareDialogFragment extends BaseDialogFragment {

    Long id;
    boolean permission;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        permission = bundle.getBoolean("permission");
        id = bundle.getLong("workId");
    }

    @Event(R.id.share_dialog_cancel)
    private void cancel(View view) {
        dismiss();
    }

    @Event(R.id.share_ll_delete)
    private void workDelete(View view) {
        if (!permission) {
            Toast.makeText(getActivity(), "您无法删除他人的作品", Toast.LENGTH_SHORT).show();
            return;
        }
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("workId", id);
        confirmDialogFragment.setArguments(bundle);
        confirmDialogFragment.show(getFragmentManager(), "confirm");
        dismiss();
    }

    @Event(R.id.share_ll_edit)
    private void workEdit(View view) {
        if (!permission) {
            Toast.makeText(getActivity(), "您无法更改他人的作品", Toast.LENGTH_SHORT).show();
            return;
        }
        dismiss();
        Intent intent = new Intent(getActivity(), FillInformationActivity.class);
        intent.putExtra("workId", id);
        startActivity(intent);
    }


}
