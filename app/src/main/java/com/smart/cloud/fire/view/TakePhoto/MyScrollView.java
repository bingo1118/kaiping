package com.smart.cloud.fire.view.TakePhoto;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

    //ScrollView如果嵌套了ListView、GridView或者RecyclerView
    //当这些子控件加载完之后ScrollView就会自动滑动到底部
    //可以重写ScrollView中的computeScrollDeltaToGetChildRectOnScreen方法来解决这个问题

    public MyScrollView(Context context) {
        super(context); }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs); }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle); }

    @Override protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0; }
}
