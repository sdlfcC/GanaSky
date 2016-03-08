package com.ganasky;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 主界面数据适配器实现
 * @author 
 */
public class GanaSkyImageAdapter extends BaseAdapter  
{
	// 定义Context
	private Context		mContext;
	// 定义整型数组 即图片源
	private Integer[]	mImageIds	= 
	{ 
			R.drawable.button1, 
			R.drawable.button2, 
			R.drawable.button3 
	};
   /*构造方法*/
	public GanaSkyImageAdapter(Context c)
	{
		mContext = c;
	}

	// 获取图片的个数
	@Override
	public int getCount()
	{
		return mImageIds.length;
	}

	// 获取图片在库中的位置
	@Override
	public Object getItem(int position)
	{
		return position;
	}
	// 获取图片ID
	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			// 给ImageView设置资源
			imageView = new ImageView(mContext);
			// 设置布局 图片85×85显示
			imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
			// 设置显示比例类型
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		}
		else
		{
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(mImageIds[position]);		
		return imageView;
	}	
}


