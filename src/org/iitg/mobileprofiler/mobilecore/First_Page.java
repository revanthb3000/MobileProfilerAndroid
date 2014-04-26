package org.iitg.mobileprofiler.mobilecore;

import com.iitg.mobileprofiler.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class First_Page extends Activity{

	EditText ipAddressEditText,portEditText;
	SharedPreferences Sh_pref;
	String Sh_name="mobile_profiler";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_page);
	
		ipAddressEditText = (EditText)findViewById(R.id.et_ip);
		portEditText = (EditText)findViewById(R.id.et_port);
		
		Sh_pref = getSharedPreferences(Sh_name, 0);
		String ip = Sh_pref.getString("ip", "");
		String port = Sh_pref.getString("port", "");
		
		ipAddressEditText.setText(ip);
		portEditText.setText(port);
		
	
	
	}
	
	public void Submit(View v){
		String ip_entered = ipAddressEditText.getText().toString(); 
		String port_enterd = portEditText.getText().toString();
				
		
		SharedPreferences.Editor editor = Sh_pref.edit();
		editor.putString("ip", ip_entered);
		editor.putString("port", port_enterd);
		editor.commit();
		
		//Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
		
		Intent a = null;
		a = new Intent(this,MainActivity.class);
		
		a.putExtra("ip", ip_entered);
		a.putExtra("port", port_enterd);
		a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(a);
		this.finish();
		
	}
	
}
