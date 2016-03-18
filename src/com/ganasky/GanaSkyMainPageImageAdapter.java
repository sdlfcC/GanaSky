package com.ganasky;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class GanaSkyMainPageImageAdapter extends SimpleAdapter {
  	// 单价：
   	private TextView flightList_publicPriceText;
   	// 机票数
   	private TextView flightList_AvailableSeat;

    public GanaSkyMainPageImageAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) 
    {
        super(context, data, resource, from, to);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        //图片控件
       ImageView imageView = (ImageView)view.findViewById(R.id.flightList_CompanyCode);
       imageView.setImageDrawable((Drawable)((Map)getItem(position)).get("CompanyCode"));
 
       if(imageView.getDrawable()!=null)
       {
    	   
    	   flightList_publicPriceText = (TextView)view.findViewById(R.id.flightList_publicPriceText);
   		   flightList_AvailableSeat = (TextView)view.findViewById(R.id.flightList_AvailableSeat);	
			flightList_publicPriceText.setText("单价：");			
			flightList_AvailableSeat.setText("机票数:");		
       }
       return view;
    }
    

}


