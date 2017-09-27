package pt.ptinovacao.arqospocket.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.util.HashMap;

import pt.ptinovacao.arqospocket.R;

public class ArQosTextView extends android.support.v7.widget.AppCompatTextView {

    private int selected_color;
    private int default_color;

    private boolean hasDefaultColor, hasSelectedColor;

    private final static HashMap<String, Typeface> fontCache = new HashMap<String, Typeface>();

    public ArQosTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public ArQosTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ArQosTextView(Context context) {
        super(context);
        init(null);
    }

    void init(AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = new int[]
                    {android.R.attr.textColor, android.R.attr.text,};

            TypedArray ta = getContext().obtainStyledAttributes(attrs, attrsArray);

            if (ta.hasValue(0)) {
                hasDefaultColor = true;
                default_color = ta.getColor(0, -1);
            }

            ta.recycle();
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArQosTextView, 0, 0);
            String str = a.getString(R.styleable.ArQosTextView_font);

            if (str != null) {

                Typeface typeface = fontCache.get(str);

                if (typeface == null) {
                    typeface = Typeface.createFromAsset(getContext().getAssets(), str);
                    fontCache.put(str, typeface);
                }

                if (typeface != null) {
                    setTypeface(typeface);
                }
            }

            if (a.hasValue(R.styleable.ArQosTextView_color_selected)) {
                hasSelectedColor = true;
                selected_color = a.getColor(R.styleable.ArQosTextView_color_selected, -1);
            }
            a.recycle();
        }
    }

    public void setActive(boolean selected) {
        if (selected) {
            if (hasSelectedColor) {
                setTextColor(selected_color);
            }

        } else {
            if (hasDefaultColor) {
                setTextColor(default_color);
            }
        }
    }
}