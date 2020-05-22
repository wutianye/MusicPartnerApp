package cn.tianyu_studio.musicpartnerapp.fragment.music;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.tianyu_studio.musicpartnerapp.R;
import cn.tianyu_studio.musicpartnerapp.dialog_fragment.BigPicDialogFragment;
import cn.tianyu_studio.musicpartnerapp.util.BaseFragment;


@ContentView(R.layout.fragment_geci)
public class GeciFragment extends BaseFragment implements OnBannerListener {

   @ViewInject(R.id.geci_banner)
    com.youth.banner.Banner banner;
    List<String> images = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        init(url);
    }

    private void init(String url){
        images.clear();
        String[] u = url.split(",");
        banner.setImageLoader(new GlideImageLoader());
        for (String anU : u) {
            images.add(anU);
            Log.d("图片", anU);
        }
        banner.setImages(images);
        banner.isAutoPlay(false);
        banner.setBannerAnimation(Transformer.Default);

        //click
        banner.setOnBannerListener(this);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void OnBannerClick(int position) {
        BigPicDialogFragment bigPicDialogFragment = new BigPicDialogFragment();
        bigPicDialogFragment.setUrl(images.get(position), 2);
        bigPicDialogFragment.show(getActivity().getFragmentManager(), "pic");
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /*
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */

            Glide
                    .with(context)
                    .load(path)
                    .into(imageView);


        }
    }
}
