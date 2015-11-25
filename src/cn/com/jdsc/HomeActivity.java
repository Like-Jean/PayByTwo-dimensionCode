package cn.com.jdsc;

import com.logic.util.HttpListener;
import com.logic.util.HttpRequest;
import com.zxing.activity.CaptureActivity;

import java.security.Key;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class HomeActivity extends Activity implements HttpListener{
	private Button mRechargeButton;
	private Button mPayButton;
	private RSAUtil rsa;
	private UserDataManager DM;
	private String money;
	private String userCard;
	private String userPhone;
	private String userPwd;

   @Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_home);

	mRechargeButton = (Button) findViewById (R.id.recharge);
	mPayButton = (Button) findViewById (R.id.pay);
	rsa = new RSAUtil();
	money = new String();
	userCard = new String();
	userPhone = new String();
	userPwd = new String();
	mRechargeButton.setOnClickListener(new Button.OnClickListener(){
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(HomeActivity.this, HomeRecharge.class);
			startActivity(intent);
		}
	});
	
	mPayButton.setOnClickListener(new Button.OnClickListener(){
		public void onClick(View v)
		{
			Intent openCameraIntent = new Intent(HomeActivity.this,CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
		}
	});
   }
   
   @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			String strm[] = scanResult.split("，");
			String decodeStr = scanResult;
			try {
				Log.d("key",strm[0]);
				Log.d("message",strm[1]);
				Key key= RSAHelper.getPrivateKey(strm[0]);
				String s = new String(key.getEncoded());
				Log.d("key",s);
				decodeStr = new String(rsa.decrypt(key, (strm[1]).getBytes("ISO-8859-1")));
				
				//验证数字签名
				String hashStr = new String(rsa.encrypt(key, decodeStr.getBytes("ISO-8859-1")));
				Log.d("hash1",strm[2] );
				Log.d("hash2",hashStr );
				if(hashStr.equals(strm[2])){
					String info[] = decodeStr.split(";");
					final String user = info[0];
					money = info[1];
					new AlertDialog.Builder(this).setMessage("账号："+user + "," + "金额" + money).setPositiveButton("确认付款", new DialogInterface.OnClickListener() {  
						  
                        private HttpRequest req;

						@Override  
                        public void onClick(DialogInterface dialog,  
                                int which) {
                        	//同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象 
            				SharedPreferences sharedPreferences= getSharedPreferences("test", 
            				Activity.MODE_PRIVATE); 
            				// 使用getString方法获得value，注意第2个参数是value的默认值 
            				String userName = sharedPreferences.getString("userName", "");
            				userPwd = sharedPreferences.getString("userPwd", "");
            				userPhone = sharedPreferences.getString("userPhone", "");
            				userCard = sharedPreferences.getString("userCard", "");

                        	String userBalance = DM.fetchUserByNecessary(userCard, userPhone, userPwd).getString(DM.fetchUserByNecessary(userCard, userPhone, userPwd).getColumnIndex("user_money"));
                        	SharedPreferences.Editor editor = sharedPreferences.edit(); 
            				editor.putString("userBalance", userBalance);
        					int money_now = Integer.parseInt(userBalance)-Integer.parseInt(money);
        					userBalance = String.valueOf( money_now);
        					req = new HttpRequest();
        					req.setListener(HomeActivity.this);
        					String param = "payer="+userName+"&"+"receiver="+user+"&money="+money;
        					req.sendGet("payFor", param);
        					if(DM.updateUserDataByNecessary("user_money", userCard, userPhone, userPwd, userBalance))
        					{
        						success();
        						
        						//实例化SharedPreferences对象（第一步） 
        						SharedPreferences mySharedPreferences= getSharedPreferences("test", 
        						Activity.MODE_PRIVATE); 
        						//实例化SharedPreferences.Editor对象（第二步） 
        						SharedPreferences.Editor editor1 = mySharedPreferences.edit(); 
        						//用putString的方法保存数据 
        						editor1.putString("payer", userCard);
        						editor1.putString("receiver", user);
        						editor1.putString("money", money);
        						//提交当前数据 
        						editor1.commit(); 
        					}
        					
        					else{
        						failed();
        					}
                        }  
                    } 
						  ).setNegativeButton("取消", null).show();
				}
				
				else{
					new AlertDialog.Builder(this).setMessage("二维码信息被篡改，请重扫").show();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

			
		}
	}
   
   private void success(){
		Toast.makeText(this,"支付成功",Toast.LENGTH_LONG).show();
	}

	private void failed(){
		Toast.makeText(this,"支付失败，请重试",Toast.LENGTH_LONG).show();
	}
	
	protected void onResume() {
		if (DM == null) {
			DM = new UserDataManager(this);
			DM.openDataBase();
        }
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (DM != null) {
			DM.closeDataBase();
			DM = null;
        }
		super.onPause();
	}

	@Override
	public void succToRequire(String msg, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failToRequire(String msg, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void netWorkError(String msg, String data) {
		// TODO Auto-generated method stub
		
	}
}