package tinker.cn.timemanager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tinker.cn.timemanager.R;

/**
 * Created by tiankui on 2/21/17.
 */

public class TabIndicatorItemView extends LinearLayout {

    private TextView indicatorTextView;

    public TabIndicatorItemView(Context context) {
        super(context);
        initView(null);
    }

    public TabIndicatorItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.wi_bottom_button_indicator, this);
        ImageView indicatorButtonImageView = (ImageView) findViewById(R.id.iv_indicator_image);
        indicatorTextView = (TextView) findViewById(R.id.tv_indicator_text);
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TabIndicatorItemView, 0, 0);
        Drawable drawable = typedArray.getDrawable(R.styleable.TabIndicatorItemView_IndicatorButton);
        CharSequence text = typedArray.getText(R.styleable.TabIndicatorItemView_IndicatorText);
        indicatorButtonImageView.setImageDrawable(drawable);
        indicatorTextView.setText(text);

        typedArray.recycle();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            indicatorTextView.setTextColor(getResources().getColor(R.color.tabIndicatorItemTextColor));
        }else{
            indicatorTextView.setTextColor(getResources().getColor(R.color.tabIndicatorItemTextColorNormal));
        }
    }
}
