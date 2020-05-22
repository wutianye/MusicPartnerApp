package cn.tianyu_studio.musicpartnerapp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决布局状态错乱问题
 */
public class TItem<T> {

    private T object;
    private boolean checked = false;

    public TItem(T object) {
        this.object = object;
        this.checked = false;
    }

    public static <E> List<TItem<E>> toTItemList(List<E> list) {
        List<TItem<E>> res = new ArrayList<>(list.size());
        for (E e : list) {
            res.add(new TItem<>(e));
        }
        return res;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
