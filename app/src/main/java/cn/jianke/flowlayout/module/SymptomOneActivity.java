package cn.jianke.flowlayout.module;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.jianke.flowlayout.R;
import cn.jianke.flowlayout.widget.FlowLayout;

/**
 * @className: SymptomOneActivity
 * @classDescription: 症状样式1
 * @author: leibing
 * @createTime: 2016/11/1
 */
public class SymptomOneActivity extends AppCompatActivity {
    // 标签云父布局
    private FlowLayout mFlowLayout;
    // 标签名称列表
    private List<String> labelNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_one);
        // findView
        mFlowLayout = (FlowLayout) findViewById(R.id.fly_symptom_one);
        // 获取数据
        getData();
        // 添加子标签
        addChildLabel();
    }

    /**
     * 获取数据
     * @author leibing
     * @createTime 2016/11/1
     * @lastModify 2016/11/1
     * @param
     * @return
     */
    private void getData() {
        labelNameList = new ArrayList<>();
        labelNameList.add("感冒");
        labelNameList.add("头疼");
        labelNameList.add("头晕");
        labelNameList.add("反胃");
        labelNameList.add("肚子疼");
        labelNameList.add("脖子痛");
        labelNameList.add("腰痛");
        labelNameList.add("脑袋痛");
        labelNameList.add("脚痛");
    }

    /**
     * 添加子标签
     * @author leibing
     * @createTime 2016/11/1
     * @lastModify 2016/11/1
     * @param
     * @return
     */
    private void addChildLabel() {
        // 数据源为空则返回
        if (labelNameList == null || labelNameList.size() == 0){
            return;
        }
        // 遍历数据
        for (String labelName: labelNameList){
            // 指定子标签布局
            final View labelView = LayoutInflater.from(this).inflate(R.layout.layout_child_selected_label, null);
            // 症状名称
            TextView symptomSelectedNameTv = (TextView) labelView.findViewById(R.id.tv_symptom_selected_name);
            // 清除事件
            labelView.findViewById(R.id.iv_symptom_selected_clear).setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    if (mFlowLayout != null && labelView != null)
                        mFlowLayout.removeView(labelView);
                }
            });
                if (symptomSelectedNameTv != null && labelName != null && !labelName.trim().equals("")){
                    symptomSelectedNameTv.setText(labelName);
                }
                if (mFlowLayout != null && labelView != null){
                    mFlowLayout.addView(labelView);
                }
        }
        // 刷新界面
        mFlowLayout.requestLayout();
        mFlowLayout.invalidate();
    }
}
