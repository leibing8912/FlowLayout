package cn.jianke.flowlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import cn.jianke.flowlayout.R;

/**
 * @className: FlowLayout
 * @classDescription: 云标签
 * @author: leibing
 * @createTime: 2016/11/1
 */
public class FlowLayout extends ViewGroup {
    // 存储行的集合（管理行）
    private List<LineManager> mLineList = new ArrayList<>();
    // 水平间距
    private float mVerticalSpace;
    // 竖直间距
    private float mHorizontalSpace;
    // 当前行的指针
    private LineManager mCurrentLine;
    // 行的最大宽度，除去边距的宽度
    private int mMaxWidth;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        // 设置水平间距
        mHorizontalSpace = array.getDimension(R.styleable.FlowLayout_width_space,0);
        // 设置竖直间距
        mVerticalSpace =  array.getDimension(R.styleable.FlowLayout_height_space,0);
        // 回收自定义属性，节约内存，避免内存泄漏
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 每次测量之前都先清空行集合
        mLineList.clear();
        mCurrentLine = null;
        // 获取总宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 计算最大的宽度
        mMaxWidth = width - getPaddingLeft() - getPaddingRight();
        // ******************** 测量孩子 ********************
        // 遍历获取孩子
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 测量孩子
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            // 测量完需要将孩子添加到管理行的孩子的集合中，将行添加到管理行的集合中
            if (mCurrentLine == null) {
                // 初次添加第一个孩子的时候
                mCurrentLine = new LineManager(mMaxWidth, mHorizontalSpace);
                // 添加孩子
                mCurrentLine.addView(childView);
                // 添加行
                mLineList.add(mCurrentLine);
            } else {
                // 行中有孩子的时候，判断时候能添加
                if (mCurrentLine.canAddView(childView)) {
                    // 继续往该行里添加
                    mCurrentLine.addView(childView);
                } else {
                    //  添加到下一行
                    mCurrentLine = new LineManager(mMaxWidth, mHorizontalSpace);
                    mCurrentLine.addView(childView);
                    mLineList.add(mCurrentLine);
                }
            }
        }

        // ******************** 测量自己 *********************
        // 测量自己只需要计算高度，宽度肯定会被填充满的
        int height = getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < mLineList.size(); i++) {
            // 所有行的高度
            height += mLineList.get(i).height;
        }
        // 所有竖直的间距
        height += (mLineList.size() - 1) * mVerticalSpace;
        // 测量
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 这里只负责高度的位置，具体的宽度和子孩子的位置让具体的行去管理
        l = getPaddingLeft();
        t = getPaddingTop();
        for (int i = 0; i < mLineList.size(); i++) {
            // 获取行
            LineManager line = mLineList.get(i);
            // 管理
            line.layout(t, l);
            // 更新高度
            t += line.height;
            if (i != mLineList.size() - 1) {
                // 不是最后一条就添加间距
                t += mVerticalSpace;
            }
        }
    }

    /**
     * @className:
     * @classDescription: 内部类，行管理器，管理每一行的孩子
     * @author: leibing
     * @createTime: 2016/11/1
     */
    public static class LineManager {
        // 定义一个行的集合来存放子View
        private List<View> views = new ArrayList<>();
        // 行的最大宽度
        private int maxWidth;
        // 行中已经使用的宽度
        private int usedWidth;
        // 行的高度
        private int height;
        // 孩子之间的距离
        private float space;

        /**
         * 通过构造初始化最大宽度和边距
         * @author leibing
         * @createTime 2016/11/1
         * @lastModify 2016/11/1
         * @param maxWidth 行的最大宽度
         * @param horizontalSpace 孩子之间的距离
         * @return
         */
        public LineManager(int maxWidth, float horizontalSpace) {
            this.maxWidth = maxWidth;
            this.space = horizontalSpace;
        }

        /**
         * 往集合里添加孩子
         * @author leibing
         * @createTime 2016/11/1
         * @lastModify 2016/11/1
         * @param
         * @return
         */
        public void addView(View view) {
            // 获取孩子测量宽度
            int childWidth = view.getMeasuredWidth();
            // 获取孩子测量高度
            int childHeight = view.getMeasuredHeight();
            // 更新行的使用宽度和高度
            if (views.size() == 0) {
                // 集合里没有孩子的时候
                if (childWidth > maxWidth) {
                    usedWidth = maxWidth;
                    height = childHeight;
                } else {
                    usedWidth = childWidth;
                    height = childHeight;
                }
            } else {
                usedWidth += childWidth + space;
                height = childHeight > height ? childHeight : height;
            }
            // 添加孩子到集合
            views.add(view);
        }

        /**
         * 判断当前的行是否能添加孩子
         * @author leibing
         * @createTime 2016/11/1
         * @lastModify 2016/11/1
         * @param view
         * @return
         */
        public boolean canAddView(View view) {
            // 集合里没有数据可以添加
            if (views.size() == 0) {
                return true;
            }
            // 最后一个孩子的宽度大于剩余宽度就不添加
            if (view.getMeasuredWidth() > (maxWidth - usedWidth - space)) {
                return false;
            }
            // 默认可以添加
            return true;
        }

        /**
         * 指定孩子显示的位置
         * @author leibing
         * @createTime 2016/11/1
         * @lastModify 2016/11/1
         * @param t 离顶部距离
         * @param l 离右边距离
         * @return
         */
        public void layout(int t, int l) {
            // 循环指定孩子位置
            for (View view : views) {
                // 获取宽高
                int measuredWidth = view.getMeasuredWidth();
                int measuredHeight = view.getMeasuredHeight();
                // 重新测量
                view.measure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
                // 重新获取宽度值
                measuredWidth = view.getMeasuredWidth();
                int top = t;
                int left = l;
                int right = measuredWidth + left;
                int bottom = measuredHeight + top;
                // 指定位置
                view.layout(left, top, right, bottom);
                // 更新数据
                l += measuredWidth + space;
            }
        }
    }
}
