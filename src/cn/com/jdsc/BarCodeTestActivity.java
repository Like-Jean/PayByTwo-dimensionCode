package cn.com.jdsc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.WriterException;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BarCodeTestActivity extends Activity {
    /** Called when the activity is first created. */
	public int i=1;
	private TextView resultTextView;
	private EditText nameEditText;
	private EditText moneyEditText;
	private ImageView qrImgImageView;
	private Bitmap qrCodeBitmap;
	
	private RSAUtil rsa;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);
        
        resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
        nameEditText = (EditText) this.findViewById(R.id.et_name);
        moneyEditText = (EditText) this.findViewById(R.id.et_money);
        
//        Button btn  = (Button) this.findViewById(R.id.iv_qr_image);
        qrImgImageView = (ImageView)this.findViewById(R.id.iv_qr_image);
//        	if(view instanceof ImageView){
//        		qrImgImageView = (ImageView)view ; 
//        	}
        rsa = new RSAUtil();
        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
        scanBarCodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//鎵撳紑鐓х浉鏈鸿繘琛屾壂鐮�
				Intent openCameraIntent = new Intent(BarCodeTestActivity.this,CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});
        
        Button generateQRCodeButton = (Button) this.findViewById(R.id.btn_add_qrcode);
        generateQRCodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					String contentString = nameEditText.getText().toString() + ";" + moneyEditText.getText().toString();
					if (!contentString.equals("")) {
						try {
							//用私钥产生数字签名的散列码
							byte[] hash = rsa.encrypt(codeKey.getRSAPrivateKey(),contentString.getBytes("ISO-8859-1"));
							String h = new String(hash);
							
							//用公钥进行加密
							byte[] code = rsa.encrypt(codeKey.getRSAPublicKey(),contentString.getBytes("ISO-8859-1"));
							String s = new String(code,"ISO-8859-1");
							contentString = RSAHelper.getKeyString(codeKey.getRSAPrivateKey()) + "，" + s + "，" +h;
							
							Log.d("contentString", contentString);
						
							String test = RSAHelper.getKeyString(codeKey.getRSAPrivateKey());
							String r = new String(rsa.decrypt(RSAHelper.getPrivateKey(test), s.getBytes("ISO-8859-1")));
							Log.d("message",r);
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//鏍规嵁鏂囨湰鍐呭鐢熸垚浜岀淮鐮�
						qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
						qrImgImageView.setImageBitmap(qrCodeBitmap);
					}else {
						Toast.makeText(BarCodeTestActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
					}
					
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				AlertDialog.Builder builder=new AlertDialog.Builder(BarCodeTestActivity.this);
				 if(!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
					 Toast.makeText(BarCodeTestActivity.this,"storage is full" , Toast.LENGTH_LONG).show();
					 return;
				 }
				final EditText in=new EditText(BarCodeTestActivity.this);
				builder.setTitle("保存该二维码图片?");
				builder.setView(in);
				builder.setNegativeButton("取消", null);
				builder.setPositiveButton("确定",
		                new DialogInterface.OnClickListener() {

		                    public void onClick(DialogInterface dialog, int which) {
		                        String input = in.getText().toString();
		                    	File file = new File("/sdcard/QRCode/"); 
		        				
		        				if (!file.exists()) { 
		        					file.mkdirs(); 
		        				} 
		        				File imageFile = new File(file, "用户"+input+".png"); 
		        				
		        				try {
		        					imageFile.createNewFile();
		        					FileOutputStream fos = new FileOutputStream(imageFile); 
		        					qrImgImageView.setDrawingCacheEnabled(true);
		        					Bitmap obmp = Bitmap.createBitmap(qrImgImageView.getDrawingCache());
		        					qrImgImageView.setDrawingCacheEnabled(false);
		        					obmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
		        					fos.flush();
		        					fos.close();
		        					Toast.makeText(BarCodeTestActivity.this,"image_save_success"+imageFile , Toast.LENGTH_LONG).show();
		        					Log.i("1", "meicuo");
		        				
		        				} catch (IOException e) {
		        					// TODO Auto-generated catch block
		        					Log.i("1", "catch");
		        					e.printStackTrace();
		        				} 
		                    }
		                });
				builder.show();
			
			
			}
		});
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			resultTextView.setText(scanResult);
		}
	}
}