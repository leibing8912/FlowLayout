package cn.jianke.flowlayout.module;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import cn.jianke.flowlayout.R;

/**
 * @className: MainActivity
 * @classDescription: 标签云样式选择页面
 * @author: leibing
 * @createTime: 2016/11/1
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Onclick
        findViewById(R.id.btn_symptom_one).setOnClickListener(this);
        findViewById(R.id.btn_symptom_two).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_symptom_one:
                // 跳转到症状样式1
                turnToNewPagers(SymptomOneActivity.class);
                break;
            case R.id.btn_symptom_two:
                // 跳转到症状样式2
                turnToNewPagers(SymptomTwoActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到新页面
     * @author leibing
     * @createTime 2016/11/1
     * @lastModify 2016/11/1
     * @param cls activity页面
     * @return
     */
    private void turnToNewPagers(Class<? extends Activity> cls){
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }
}
