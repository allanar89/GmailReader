package cu.slam.lectorgmail;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class GAM extends Activity {
	
	String rutagm = "/data/data/com.google.android.gm/databases/";
	String rutagms = "/data/data/com.google.android.gms/databases/";
	
	SQLiteDatabase sqLiteDatabase;
	Cursor cursor;
	File[] belcro=new File[20];
	File[] files;
	File file;
	Process p;
	InputStream is;
	OutputStream os;
	TextView txtCuentas;
	LinkedList<File> cuentas = new LinkedList<File>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gam);
		txtCuentas = (TextView)findViewById(R.id.textView1);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gam, menu);		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:			
			AccesoRootyCPDirs();
			OpcionB();
			break;
		default:
			break;
		}	
		return super.onOptionsItemSelected(item);
	}
	
	public void OpcionB(){
		File ruta1 = new File(Environment.getDownloadCacheDirectory()+"/dbgm/\n");		
		File ruta2 = new File(Environment.getDownloadCacheDirectory()+"/dbgms/\n");
		FilenameFilter ff=new FilenameFilter() {				
			@Override
			public boolean accept(File dir, String filename) {
				return (filename.contains("mailstore")&& filename.endsWith(".db"))?true:false;
			}
		};
		if(ruta1.exists() && ruta1.isDirectory()){
			String[] be = ruta1.list(ff);
			String tt="";
			for (String name : be) {
				tt+=name+"\n";
			}
				Toast.makeText(getApplicationContext(), tt+" es legal", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getApplicationContext(), ruta1.getAbsolutePath()+"la ruta no es legal", Toast.LENGTH_LONG).show();/*
		File[] r1 = ruta1.listFiles(ff);
		File[] r2 = ruta2.listFiles(ff);
		for (File f : r1) {
			cuentas.add(f);
		}
		for (File f : r2) {
			cuentas.add(f);
		}*/
			}
	}
	public void Buscador(File[] ffi){
		if(ffi.length<=0 || ffi==null)
			return ;
		File[] ffiles = ffi;
		for (File f : ffiles) {
			if(f.isFile()){
				if(f.getName().contains("mailstore") && f.getName().endsWith(".db"))
					cuentas.add(f);
			}else if(f.isDirectory()){
				Buscador(f.listFiles());
			}
		}	
	}
	public void AccesoRootyCPDirs(){
		try{
			
			String cuentas="";
			Process p = Runtime.getRuntime().exec("su");			
			OutputStream oss = p.getOutputStream();			
			//la ruta es: /cache
			oss.write(("mkdir "+Environment.getDownloadCacheDirectory()+"/dbgm"+"\n").getBytes());
			oss.write(("mkdir "+Environment.getDownloadCacheDirectory()+"/dbgms"+"\n").getBytes());
			oss.write(("cp -R "+rutagm+"*.db "+Environment.getDownloadCacheDirectory()+"/dbgm/"+"\n").getBytes());			
			oss.write(("cp -R "+rutagms+"*.db "+Environment.getDownloadCacheDirectory()+"/dbgms/"+"\n").getBytes());
			oss.close();						
			p.destroy();							
		}catch (Exception e) {			
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			AlertDialog d= new AlertDialog.Builder(getApplicationContext()).create();
			d.setTitle("INFO");
			d.setMessage("Error: "+e.getMessage());
			d.show();
			e.printStackTrace();		
		}
	}

}
