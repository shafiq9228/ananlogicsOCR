package com.analogics.utils;
/**
 * @author ANIL REDDY GANTLA
 * <p>
 * Added Method Alertwithtwobuttons
 */


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.analogics.R;


public class AlertMessage {

    String ret = null;

    public void alertMessage(Context context, String Header, String Body) {


        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.status_view);
        // dialog.setTitle("ALARM DATA");
        dialog.setCancelable(false);
        TextView TV_displaydata = (TextView) dialog
                .findViewById(R.id.TV_displaydata);
        TextView Header_text = (TextView) dialog
                .findViewById(R.id.Header_text);
        Header_text
                .setText(Header);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);


        TV_displaydata.setText(Body);


        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing())
            dialog.show();



		/*	Toast.makeText(InstallNewModemActivity.this,
				"DATA CONNECTION NOT AVAILABLE",
				Toast.LENGTH_LONG).show();*/

    }

//
//	public String alertwithtwobuttons(Context context,String Body) {
//
//
//		final Dialog dialog =new Dialog(context);
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setContentView(R.layout.status_view_withtwobuttons);
//		dialog.setCancelable(false);
//		dialog.show();
//		TextView TV_displaydata = (TextView) dialog
//				.findViewById(R.id.TV_displaydata);
//	
//	
//		TV_displaydata.setText(Body);
//		Button yes = (Button) dialog.findViewById(R.id.Btn_yes);
//		Button No  =(Button)  dialog.findViewById(R.id.Btn_no);
//
//		yes.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ret= "YES";	
//				dialog.dismiss();
//				
//			}
//		});
//
//		No.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ret= "NO";
//				dialog.dismiss();
//				
//			}
//		});
//	
//		
//		
//		return ret;
//	}

}
