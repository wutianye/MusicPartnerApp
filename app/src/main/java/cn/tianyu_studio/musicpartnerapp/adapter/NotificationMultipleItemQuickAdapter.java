package cn.tianyu_studio.musicpartnerapp.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.entity.Dynamic;
import cn.tianyu_studio.musicpartnerapp.entity.Notification;

public class NotificationMultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int ITEM_TYPE_NOTIFICATION = 0;
    public static final int ITEM_TYPE_COMMENT = 1;

    private Context context;

    public NotificationMultipleItemQuickAdapter(Context content, List<MultiItemEntity> data) {
        super(data);
        this.context = content;
        addItemType(ITEM_TYPE_NOTIFICATION, R.layout.item_notification_notification);
        addItemType(ITEM_TYPE_COMMENT, R.layout.item_notification_comment);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_NOTIFICATION:
                Notification notification = (Notification) item;
                TextView tv_unread1 = helper.getView(R.id.item_message_unread);
                if (notification.getIsRead() == 1)
                    tv_unread1.setVisibility(View.GONE);
                helper.setText(R.id.item_notification_notification_content, notification.getContent())
                        .setText(R.id.item_notification_notification_time, notification.getGmtCreate().replace('T', ' '));
                ImageView imageView = helper.getView(R.id.item_notification_notification_headImg);
                imageView.setImageResource(R.mipmap.notification);
//                x.image().bind(imageView, notification.getImgUrl(), new ImageOptions.Builder().setCircular(true).build());
                break;
            case ITEM_TYPE_COMMENT:
                Dynamic dynamic = (Dynamic)item;
                TextView content =  helper.getView(R.id.item_notification_comment_content);
                TextView title = helper.getView(R.id.item_notification_comment_title);
                title.setText("");
                TextView tv_unread = helper.getView(R.id.item_notification_unread);
                helper.setText(R.id.item_notification_comment_time, dynamic.getGmtCreate().replace('T', ' '));
                //处理评论内容，类型为赞是评论为空。
                if(dynamic.getType() == 1|| dynamic.getType() == 2)
                    content.setVisibility(View.GONE);
                else
                    content.setText(dynamic.getContent());

                if (dynamic.getIsRead() == 1)
                    tv_unread.setVisibility(View.GONE);
               //处理评论头
                SpannableString userName = new SpannableString(dynamic.getSenderNickname());
                userName.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(context, "clicked" + userName.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(context.getResources().getColor(R.color.clickable_text));
                        ds.setUnderlineText(false);
                    }
                }, 0, userName.length(), Spanned.SPAN_MARK_MARK);
                title.append(userName);
                String temp = DynamicType(dynamic.getType());
                title.append(temp);
                SpannableString workName = new SpannableString("「" + dynamic.getWorkName() + "」");
                workName.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(context, "clicked" + workName.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(context.getResources().getColor(R.color.clickable_text));
                        ds.setUnderlineText(false);
                    }
                }, 0, workName.length(), Spanned.SPAN_MARK_MARK);
                title.append(workName);
                title.setMovementMethod(LinkMovementMethod.getInstance());

                ImageView commentUserHeadImg = helper.getView(R.id.item_notification_comment_userHeadImg);
                x.image().bind(commentUserHeadImg, dynamic.getSenderHeadImgUrl(), new ImageOptions.Builder().setCircular(true).build());
              //  ImageView commentImg = helper.getView(R.id.item_notification_comment_img);
               // x.image().bind(commentImg, comment.getImgUrl());
                break;
        }
    }

    String DynamicType(int type){
        switch (type){
            case 0:return "评论了你的作品";
            case 1:return "点赞了你的作品";
            case 2:return "点赞了你的评论";
            case 3:return "引用了你的评论";
        }
        return "";
    }

}
