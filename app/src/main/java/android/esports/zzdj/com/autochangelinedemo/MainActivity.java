package android.esports.zzdj.com.autochangelinedemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ItemBean> itemBeanArrayList;
    private LinearLayout ll_parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initdata();
        initAutoChangeLine();
    }
    private void initView() {
        ll_parent = findViewById(R.id.ll_parent);
    }

    private void initdata() {
        itemBeanArrayList = new ArrayList<>();
        ItemBean itemBean;
        for(int i=0;i<20;i++){
            itemBean = new ItemBean();
            itemBean.setSelect(false);
            if(i==0){
                itemBean.setContent("英雄");
            }else if(i==5){
                itemBean.setContent("I LOVE YOU");
            }else if(i==7){
                itemBean.setContent("你是否喜欢波多野吉呀哈哈哈！");
            }else{
                itemBean.setContent("王者荣耀"+i);
            }
            itemBeanArrayList.add(itemBean);
        }
    }
    //动态加载且换行
    private void initAutoChangeLine() {
        //每一行zhongTextView
        LinearLayout rowLL = new LinearLayout(this);
        LinearLayout.LayoutParams rowLP =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置margin的值 行高是10dp
        float rowMargin = dipToPx(10);
        //某一项的布局
        rowLP.setMargins(0, (int) rowMargin, 0, 0);
        //一行的整体布局
        rowLL.setLayoutParams(rowLP);

        //是否是新的布局
        boolean isNewLayout = false;
        //取宽度的最大值 通过得到屏幕的宽度
        float maxWidth = getScreenWidth();
        //剩下的宽度
        float elseWidth = maxWidth;
        //TextView的布局
        LinearLayout.LayoutParams textViewLP =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        //距离左边8dp
        textViewLP.setMargins((int) dipToPx(8), 0, 0, 0);
        //遍历数据，进行设置以及添加数据
        for (int i = 0; i < itemBeanArrayList.size(); i++) {
            //若当前为新起的一行，先添加旧的那行
            //然后重新创建布局对象，设置参数，将isNewLayout判断重置为false
            if (isNewLayout) {
                ll_parent.addView(rowLL);
                rowLL = new LinearLayout(this);
                rowLL.setLayoutParams(rowLP);
                isNewLayout = false;
            }
            //设置textView的内容
            final TextView textView = (TextView) getLayoutInflater().inflate(R.layout.text_view, null);
            textView.setText(itemBeanArrayList.get(i).getContent());
            if(itemBeanArrayList.get(i).isSelect()){
                textView.setBackgroundResource(R.drawable.tv_select_bg);
            }
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemBeanArrayList.get(finalI).isSelect()){
                        textView.setBackgroundResource(R.drawable.tv_bg);
                        itemBeanArrayList.get(finalI).setSelect(false);
                    }else{
                        textView.setBackgroundResource(R.drawable.tv_select_bg);
                        itemBeanArrayList.get(finalI).setSelect(true);
                    }
                    Toast.makeText(MainActivity.this,itemBeanArrayList.get(finalI).getContent(),Toast.LENGTH_SHORT).show();
                }
            });
            //计算是否需要换行
            //获取宽高
            textView.measure(0, 0);
            // 若是一整行都放不下这个文本框，添加旧的那行，新起一行添加这个文本框
            if (maxWidth < textView.getMeasuredWidth()) {
                ll_parent.addView(rowLL);
                rowLL = new LinearLayout(this);
                rowLL.setLayoutParams(rowLP);
                rowLL.addView(textView);
                isNewLayout = true;
                continue;
            }
            //若是剩下的宽度小于文本框的宽度（放不下了）
            //添加旧的那行，新起一行，但是i要-1，因为当前的文本框还未添加
            if (elseWidth < textView.getMeasuredWidth()) {
                isNewLayout = true;
                i--;
                //重置剩余宽度
                elseWidth = maxWidth;
                continue;
            } else {
            // 剩余宽度减去文本框的宽度+间隔=新的剩余宽度
                elseWidth -= textView.getMeasuredWidth() + dipToPx(8);
                //获取每一行下面的字空间，如果等于0直接添加
                if (rowLL.getChildCount() == 0) {
                    rowLL.addView(textView);
                } else {//如果不等于0,需要设置一下间隔
                    textView.setLayoutParams(textViewLP);
                    rowLL.addView(textView);
                }
            }
        }
        //添加最后一行，但要防止重复添加
        ll_parent.addView(rowLL);
    }
    //调用系统api转换单位 获得转换后的px值
    private float dipToPx(int dipValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dipValue,this.getResources().getDisplayMetrics());
    }
    //获得宽度
    private float getScreenWidth() {
        return this.getResources().getDisplayMetrics().widthPixels;
    }
}
