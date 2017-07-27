package com.fengniao.remind.ui.wediget;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private OnItemTouchCallbackListener mOnItemTouchCallbackListener;

    //是否可以拖拽
    private boolean isCanDrag = false;


    //是否可以滑动
    private boolean isCanSwipe = false;


    public ItemTouchHelperCallback(OnItemTouchCallbackListener mOnItemTouchCallbackListener) {
        this.mOnItemTouchCallbackListener = mOnItemTouchCallbackListener;
    }

    public void setSwipeEnable(boolean canSwipe) {
        isCanSwipe = canSwipe;
    }

    public void setDragEnable(boolean canDrag) {
        isCanDrag = canDrag;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isCanDrag;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isCanSwipe;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            //如果frag值为0，相当于这个功能被关闭
            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlag = 0;
            return makeMovementFlags(dragFlag, swipeFlag);
        } else if (layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();
             int dragFlag = 0;
            int swipeFlag = 0;

            if (orientation == LinearLayoutManager.HORIZONTAL){
                swipeFlag  =ItemTouchHelper.UP|ItemTouchHelper.DOWN;
                dragFlag = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
            } else if (orientation == LinearLayoutManager.VERTICAL){
                dragFlag  =ItemTouchHelper.UP|ItemTouchHelper.DOWN;
                swipeFlag = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (mOnItemTouchCallbackListener!=null){
            return mOnItemTouchCallbackListener.onMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mOnItemTouchCallbackListener!=null){
            mOnItemTouchCallbackListener.onSwiped(viewHolder.getAdapterPosition());
        }
    }

    public interface OnItemTouchCallbackListener {

        //某个item被滑动删除
        void onSwiped(int positon);

        /**
         * 两个item位置互换
         *
         * @param srcPos    拖拽item位置
         * @param targetPos 目标item位置
         * @return
         */
        boolean onMove(int srcPos, int targetPos);

    }
}
