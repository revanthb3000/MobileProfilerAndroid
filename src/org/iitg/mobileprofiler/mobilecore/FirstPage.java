package org.iitg.mobileprofiler.mobilecore;

import java.util.ArrayList;

import org.iitg.mobileprofiler.db.DatabaseConnector;
import org.iitg.mobileprofiler.p2p.peer.UserNodePeer;
import org.iitg.mobileprofiler.p2p.tools.UtilityFunctions;

import com.iitg.mobileprofiler.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class FirstPage extends Activity{

	EditText ipAddressEditText,portEditText;

	SharedPreferences Sh_pref;
	
	String Sh_name="mobile_profiler";
	
	public static UserNodePeer userNodePeer;	

	public static String phoneName = Build.ID + Build.MODEL;
	
	private static final int PEER_PORT_NUMBER = 5689;
	
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
		String ipAddress = ipAddressEditText.getText().toString(); 
		String portNumber = portEditText.getText().toString();
				
		
		SharedPreferences.Editor editor = Sh_pref.edit();
		editor.putString("ip", ipAddress);
		editor.putString("port", portNumber);
		editor.commit();
		
		//Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();

		try{
			DatabaseConnector databaseConnector = new DatabaseConnector();
			ArrayList<Integer> userClassContents = databaseConnector.getNumberOfDocuments(0, databaseConnector.getNumberOfClasses(), true);
			databaseConnector.closeDBConnection();
			userNodePeer = new UserNodePeer(UtilityFunctions.getHexDigest(phoneName), phoneName, PEER_PORT_NUMBER , userClassContents, ipAddress + ":" + portNumber, null, 0);
			userNodePeer.joinToBootstrapPeer();	
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e("MainActivityError", "Error in getting classcontents/connecting to bootstrap");
		}
		
		Intent a = null;
		a = new Intent(this,MainActivity.class);
		
		a.putExtra("ip", ipAddress);
		a.putExtra("port", portNumber);
		a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(a);
		this.finish();
		
	}
	
}
