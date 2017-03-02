package pt.ptinovacao.arqospocket.views;

import java.util.HashMap;

import pt.ptinovacao.arqospocket.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class SuperTextView extends TextView
{

	int selected_color;
	int default_color;

	boolean hasdefault_color, hasselected_color;

	int textresource = -1;

	static HashMap<String, Typeface> fontCache = new HashMap<String, Typeface>();

	public SuperTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		init(attrs);

	}

	public SuperTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(attrs);
	}

	public SuperTextView(Context context)
	{
		super(context);

		init(null);
	}

	void init(AttributeSet attrs)
	{
		if (attrs != null)
		{
			int[] attrsArray = new int[]
			{ android.R.attr.textColor, android.R.attr.text, };

			TypedArray ta = getContext().obtainStyledAttributes(attrs, attrsArray);

			if (ta.hasValue(0))
			{
				hasdefault_color = true;
				default_color = ta.getColor(0, -1);
			}

			ta.recycle();

			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SuperTextView, 0, 0);

			String str = a.getString(R.styleable.SuperTextView_font);

			if (str != null)
			{

				Typeface customtypeface = fontCache.get(str);

				if (customtypeface == null)
				{
					customtypeface = Typeface.createFromAsset(getContext().getAssets(), str);
					fontCache.put(str, customtypeface);
				}

				if (customtypeface != null)
				{
					setTypeface(customtypeface);
				}

			}

			if (a.hasValue(R.styleable.SuperTextView_color_selected))
			{
				hasselected_color = true;
				selected_color = a.getColor(R.styleable.SuperTextView_color_selected, -1);
			}

			a.recycle();
		}
	}

	public void setActive(boolean selected)
	{
		if (selected)
		{
			if (hasselected_color)
			{
				setTextColor(selected_color);
			}

		}
		else
		{
			if (hasdefault_color)
			{
				setTextColor(default_color);
			}
		}
	}

}