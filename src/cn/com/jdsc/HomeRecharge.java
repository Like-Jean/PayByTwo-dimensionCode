package cn.com.jdsc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

public class HomeRecharge extends Activity{

	private String userSum;
	private String userCard;
	private String userPwd;
	private String userBalance;
	private TextView mBalance;
	
	private Button confirm;
	private Button cancel;
	
	private UserDataManager DM;
	
	private RSAUtil rsacoder;
	
	byte[] code;
	   @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_home1);
		
		rsacoder = new RSAUtil();
		
		confirm = (Button)findViewById (R.id.button1);
		cancel = (Button)findViewById (R.id.button2);
		
		mBalance = (TextView)findViewById (R.id.balance_display);

		confirm.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				EditText money = (EditText)findViewById (R.id.recharge_moneydisplay);
				EditText card = (EditText)findViewById (R.id.login_input_name);
				EditText password = (EditText)findViewById (R.id.Password_display);
				userSum = money.getText().toString().trim();
				userCard = card.getText().toString().trim();
				userPwd = password.getText().toString().trim();
				//同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象 
				SharedPreferences sharedPreferences= getSharedPreferences("test", 
				Activity.MODE_PRIVATE); 
				// 使用getString方法获得value，注意第2个参数是value的默认值 
				String userPhone =sharedPreferences.getString("userPhone", "");

						//实例化SharedPreferences.Editor对象（第二步） 
				SharedPreferences.Editor editor = sharedPreferences.edit(); 
				editor.putString("userPwd", userPwd);
				editor.putString("userCard", userCard);
				
				editor.commit(); 
				
				try {
					code = rsacoder.encrypt(codeKey.getRSAPublicKey(),userPwd.getBytes());
					String s = new String(rsacoder.encrypt(codeKey.getRSAPublicKey(),userPwd.getBytes()));
					userPwd = s;
					Log.d("en",userPwd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String s;
				try {
					s = new String(rsacoder.decrypt(codeKey.getRSAPrivateKey(),code));
					userPwd = s;
					Log.d("de",s);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int result=DM.findUserByNecessary(userCard, userPhone, userPwd);
				if(result==1){
					if(Integer.parseInt(userSum)<1000){
					userBalance = DM.fetchUserByNecessary(userCard, userPhone, userPwd).getString(DM.fetchUserByNecessary(userCard, userPhone, userPwd).getColumnIndex("user_money"));
					int money_now = Integer.parseInt(userBalance)+Integer.parseInt(userSum);
					userBalance = String.valueOf( money_now);

					mBalance.setText(userBalance);
					editor.putString("userBalance", userBalance);
					editor.commit();
					
					if(DM.updateUserDataByNecessary("user_money", userCard, userPhone, userPwd, userBalance))
					{
						success(); 
					}
					
					else{
						failed();
					}
				}
					else{
						over();
					}
				}
				
			}
		});
		
		cancel.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				HomeRecharge.this.finish();
			}
		});
	   }
	   
	private void success(){
		Toast.makeText(this,"充值成功",Toast.LENGTH_LONG).show();
	}

	private void failed(){
		Toast.makeText(this,"充值失败，请重试",Toast.LENGTH_LONG).show();
	}
	
	private void over(){
		Toast.makeText(this,"充值金额要小于1000",Toast.LENGTH_LONG).show();
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
}