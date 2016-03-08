package com.ganasky;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * ����������������ʵ��
 * @author 
 */
public class GanaSkyImageAdapter extends BaseAdapter  
{
	// ����Context
	private Context		mContext;
	// ������������ ��ͼƬԴ
	private Integer[]	mImageIds	= 
	{ 
			R.drawable.button1, 
			R.drawable.button2, 
			R.drawable.button3 
	};
   /*���췽��*/
	public GanaSkyImageAdapter(Context c)
	{
		mContext = c;
	}

	// ��ȡͼƬ�ĸ���
	@Override
	public int getCount()
	{
		return mImageIds.length;
	}

	// ��ȡͼƬ�ڿ��е�λ��
	@Override
	public Object getItem(int position)
	{
		return position;
	}
	// ��ȡͼƬID
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
			// ��ImageView������Դ
			imageView = new ImageView(mContext);
			// ���ò��� ͼƬ85��85��ʾ
			imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
			// ������ʾ��������
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


