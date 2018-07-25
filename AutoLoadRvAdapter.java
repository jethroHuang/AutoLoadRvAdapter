package com.cqgynet.collegecircle.Global;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

/**
 * 自动追加数据的adapter
 *
 *
 * @param <M> 数据类型
 */
public abstract class AutoLoadRvAdapter<VH extends RecyclerView.ViewHolder, M> extends RecyclerView.Adapter<VH> {

    private int bottomPosition = 10; //指定点 (默认倒数第10条)
    private OnToBottomCallBack callBack; //到指定点时的回调
    private List<M> list; //储存数据的list
    public int page_size; //每一页数据的数量
    private boolean hasNext; //是否有下一页

    /**
     * 设置底部回调位置 position
     * 例如设置倒数第一个
     * setBottomPosition(1);
     * 当尝试设置倒数第0个时,将不会执行回调(默认为倒数第10个)
     */
    public void setBottomPosition(int position) {
        this.bottomPosition = position;
    }

    /**
     * 设置到底部的回调
     *
     * @param callBack 回调
     */
    public void setOnToBottomCallBack(OnToBottomCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * @param dataList  数据列表
     * @param callBack  回调
     * @param page_size 页面大小(当新插入的数据大小小于 page_size 的时候,停止调用 callBack)
     */
    public AutoLoadRvAdapter(List<M> dataList, int page_size, OnToBottomCallBack callBack) {
        this.callBack = callBack;
        this.list = dataList;
        this.page_size = page_size;
        hasNext = list.size() >= page_size;
    }

    public List<M> getList() {
        return list;
    }

    public M getData(int position) {
        return list.get(position);
    }

    public void setList(List<M> list) {
        this.list = list;
        hasNext = list.size() >= page_size;
        notifyDataSetChanged();
    }

    /**
     * 将数据添加到现有数据列表
     *
     * @param list 待添加的数据
     */
    public void addList(List<M> list) {
        if (list != null && list.size() > 0) {
            int start = this.list.size();
            this.list.addAll(list);
            hasNext = list.size() >= page_size;
            notifyItemRangeInserted(start, list.size());
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (hasNext && (callBack != null) && (bottomPosition + position == list.size())) {
            callBack.onToBottom(getData(position));
        }
        onBindViewHolder(holder, position, list.get(position));
    }

    public abstract void onBindViewHolder(@NonNull VH holder, int position, M data);


    /**
     * 当渲染到底部时,将调用此回调
     */
    public interface OnToBottomCallBack {
        void onToBottom(Object object);
    }
}
