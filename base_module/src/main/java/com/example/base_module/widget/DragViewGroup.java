package com.example.base_module.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Scroller;

public class DragViewGroup extends LinearLayout {
    private boolean openDrag = true;

    // 记录手指上次触摸的坐标
    private float mLastPointX;
    private float mLastPointY;

    //用于识别最小的滑动距离
    private int mSlop;
    // 用于标识正在被拖拽的 child，为 null 时表明没有 child 被拖拽
    private View mDragView;

    // 状态分别空闲、拖拽两种
    enum State {
        IDLE,
        DRAGGING
    }

    State mCurrentState;

    private VelocityTracker velocityTracker;

    private float childViewWidth;
    private float menuWidth;
    private float sumDragWidth = 0;
    private float touchWidth = 0;
    private float maxTouchWidth = 0;
    private Scroller mScroller;

    public DragViewGroup(Context context) {
        this(context, null);
    }

    public DragViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public DragViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSlop = ViewConfiguration.getWindowTouchSlop() / 2;
        mScroller = new Scroller(context);
    }

    public void setOpenDrag(boolean openDrag) {
        this.openDrag = openDrag;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (openDrag) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (isPointOnViews(event)) {
                        //标记状态为拖拽，并记录上次触摸坐标
                        mCurrentState = State.DRAGGING;
                        mLastPointX = event.getX();
                        mLastPointY = event.getY();
                        childViewWidth = mDragView.getWidth();
                    }
//                    return true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int deltaX = (int) -(event.getX() - mLastPointX);
                    touchWidth += deltaX;
                    if (touchWidth > maxTouchWidth){
                        maxTouchWidth = touchWidth;
                    }
//                    Log.e("Move状态", getScrollX() + "   " + sumDragWidth + "    " + deltaX + "    " + Math.abs(sumDragWidth - deltaX));
                    if (mCurrentState == State.DRAGGING && mDragView != null && Math.abs(deltaX) > mSlop && Math.abs(sumDragWidth) <= menuWidth && !mScroller.computeScrollOffset()) {//总滑动长度小于菜单内容宽度时
                        // 如果符合条件则对被拖拽的 child 进行位置移动
                        if (getScrollX() <= 0 && deltaX < 0) {   //deltax<0 代表要向右滑动  getScrollX<=0 代表此时内容View已经是初始化或者向右滑动状态（此时禁止滑动）
                        } else {
                            if (Math.abs(sumDragWidth - deltaX) > menuWidth) {
                                deltaX = (int) (menuWidth - Math.abs(sumDragWidth));
                            }
                            scrollBy(deltaX, 0);
                            sumDragWidth += (-deltaX);
                        }
                        mLastPointX = event.getX();
                        mLastPointY = event.getY();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (mCurrentState == State.DRAGGING) {
                        int scrollX = getScrollX();
                        int scrollY = getScrollY();
//                        Log.e("Up状态", Math.abs(sumDragWidth) + "   " + scrollX + "   " + menuWidth);
                        if (scrollX >0){  //scrollX 大于0   结果为向左滑动的scrollX距离 然后根据滑动距离计算是否展示
                            if (scrollX>= menuWidth || scrollX >= childViewWidth/2 ){
//                            Log.e("Up状态", "显示菜单" + "      " +  (int) (Math.abs(scrollX)-menuWidth));
                                mScroller.startScroll(scrollX,scrollY, (int) (menuWidth-Math.abs(scrollX)),0,500);
                                sumDragWidth = -menuWidth;
                            } else {
//                                Log.e("Up状态", "移除菜单1" + "      " + scrollX);
                                mScroller.startScroll(scrollX,scrollY, -scrollX,0,500);
                                sumDragWidth = 0;
                            }
                        } else{   //scrollY 小于0  代表向右滑动了scrollX 要隐藏
//                            Log.e("Up状态", "移除菜单2" + "      " +  scrollX);
                            mScroller.startScroll(scrollX,scrollY, -scrollX,0,500);
                            sumDragWidth = 0;
                        }
                        invalidate();
                        // 标记状态为空闲，并将 mDragView 变量置为 null
                        mCurrentState = State.IDLE;
                        mDragView = null;
                        if (Math.abs(maxTouchWidth) >= mSlop*5) {  //在点击允许触碰距离外  消耗touch事件
                            touchWidth = 0;
                            return true;
                        }
                        touchWidth = 0;
                        break;
                    }
            }
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        } else {

        }
    }

    /**
     * 判断触摸的位置是否落在 child 身上
     */
    private boolean isPointOnViews(MotionEvent ev) {
        boolean result = false;
        Rect rect = new Rect();
        menuWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {  //只获取第一个的
            View view = getChildAt(i);
            rect.set((int) view.getX(), (int) view.getY(), (int) view.getX() + (int) view.getWidth()
                    , (int) view.getY() + view.getHeight());
            if (i == 0 && rect.contains((int) ev.getX(), (int) ev.getY())) {
                //标记被拖拽的child
                mDragView = view;
                result = true;
            } else {
                menuWidth += view.getWidth();
            }
        }

        return result && mCurrentState != State.DRAGGING;
    }
}