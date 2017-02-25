package tinker.cn.timemanager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

import tinker.cn.timemanager.R;

/**
 * Created by tiankui on 2/21/17.
 */

public class AddImageButton extends ImageButton{

    public AddImageButton(Context context) {
        super(context);
    }

    public AddImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,R.styleable.AddImageButton,0,0);
        Drawable backgroundDrawable=typedArray.getDrawable(R.styleable.AddImageButton_AddButtonBackground);
        Drawable imageDrawable=typedArray.getDrawable(R.styleable.AddImageButton_AddButtonImageSrc);
        setImageDrawable(imageDrawable);
        setBackground(backgroundDrawable);

        typedArray.recycle();
    }
}
