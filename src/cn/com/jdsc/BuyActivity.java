package cn.com.jdsc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnClickListener;


public class BuyActivity extends Activity{
	private Button mCancel; 
	private Button mOk;
	private Button mQrocode;
	private EditText mCard;
	private EditText mPhone;
	private EditText mPassword;
	private View information_completedView;
	private View information_completedSuccessView;
	private TextView information_completedSuccessShow;
	private UserDataManager mUserDataManager;
	private RSAUtil rsacoder;
	
	byte[] code;


   @Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_buy0);

	mCancel = (Button) findViewById (R.id.btn_cancel);
	mOk = (Button) findViewById (R.id.btn_ok);
	mQrocode = (Button) findViewById (R.id.button3);
	mCard = (EditText) findViewById (R.id.Card_display);
	mPhone = (EditText) findViewById (R.id.telephone_display);
	mPassword = (EditText) findViewById (R.id.paypay_display);
	
	mCancel.setOnClickListener(mListener);
	mOk.setOnClickListener(mListener);
	mQrocode.setOnClickListener(mListener);
	
	rsacoder = new RSAUtil();

	mCard.setOnFocusChangeListener(new OnFocusChangeListener()
	{
	public void onFocusChange(View v, boolean hasFocus){
	if(!hasFocus){
	String userCard=mCard.getText().toString().trim();
	if(userCard.length()!=19){
	Toast.makeText(BuyActivity.this,"���Ų����Ϲ���", Toast.LENGTH_SHORT).show();
	}
	}
	}
	});
	
	mPhone.setOnFocusChangeListener(new OnFocusChangeListener()
	{
	public void onFocusChange(View v, boolean hasFocus){
	if(!hasFocus){
	String userPhone=mPhone.getText().toString().trim();
	if(userPhone.length()!=11){
	Toast.makeText(BuyActivity.this,"�ֻ��Ų����Ϲ���", Toast.LENGTH_SHORT).show();
	}
	}
	}
	});
	
	mPassword.setOnFocusChangeListener(new OnFocusChangeListener()
	{
	public void onFocusChange(View v, boolean hasFocus){
	if(!hasFocus){
	String userPassword=mPassword.getText().toString().trim();
	if(userPassword.length()!=6){
	Toast.makeText(BuyActivity.this,"��������λ����", Toast.LENGTH_SHORT).show();
	}
	}
	}
	});

	if (mUserDataManager == null) {
			mUserDataManager = new UserDataManager(this);
			mUserDataManager.openDataBase();
        }
	}
	
	OnClickListener mListener = new OnClickListener(){
	public void onClick(View v){
	switch (v.getId()){
	case R.id.btn_ok:
	  information_completed();
	  break;
	case R.id.btn_cancel:
	  BuyActivity.this.finish();
	  break;
	case R.id.button3:
		Intent intent = new Intent();
		intent.setClass(BuyActivity.this, BarCodeTestActivity.class);
		startActivity(intent);
	}
	}
	};

	public void information_completed(){
	if (isUserAllValid()){  
		String userCard = mCard.getText().toString().trim();
		String userPhone = mPhone.getText().toString().trim();
		String userPassword = mPassword.getText().toString().trim();
		
		try {
			code = rsacoder.encrypt(codeKey.getRSAPublicKey(),userPassword.getBytes());
			String s = new String(rsacoder.encrypt(codeKey.getRSAPublicKey(),userPassword.getBytes()));
			userPassword = s;
			Log.d("en",userPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String s;
		try {
			s = new String(rsacoder.decrypt(codeKey.getRSAPrivateKey(),code));
			userPassword = s;
			Log.d("de",s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int result=mUserDataManager.findUserByNecessary(userCard,userPhone,userPassword);
		if(result==1){
		//information completed
		information_completedView.setVisibility(View.GONE);
		information_completedSuccessView.setVisibility(View.VISIBLE);
		information_completedSuccessShow.setText(getString(R.string.information_completed));
		Toast.makeText(this,getString(R.string.information_completed),Toast.LENGTH_SHORT).show();
		 
		//ʵ����SharedPreferences���󣨵�һ���� 
		SharedPreferences mySharedPreferences= getSharedPreferences("test", 
		Activity.MODE_PRIVATE); 
		//ʵ����SharedPreferences.Editor���󣨵ڶ����� 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		//��putString�ķ����������� 
		editor.putString("userPhone", userPhone); 
		//�ύ��ǰ���� 
		editor.commit(); 
		//ʹ��toast��Ϣ��ʾ����ʾ�ɹ�д������ 
		Toast.makeText(this, "���ݳɹ�д��SharedPreferences��" , 
		Toast.LENGTH_LONG).show(); 

		}else if(result==0){

		//ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences���� 
		SharedPreferences sharedPreferences= getSharedPreferences("test", 
		Activity.MODE_PRIVATE);
		
		String userName = sharedPreferences.getString("userName", "");
		String userPwd = sharedPreferences.getString("userPwd", "");
		
		try {
			code = rsacoder.encrypt(codeKey.getRSAPublicKey(),userPassword.getBytes());
			s = new String(rsacoder.encrypt(codeKey.getRSAPublicKey(),userPassword.getBytes()));
			userPassword = s;
			Log.d("en",userPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			s = new String(rsacoder.decrypt(codeKey.getRSAPrivateKey(),code));
			userPassword = s;
			Log.d("de",s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(mUserDataManager.updateUserData(userName, userPwd, userCard, userPhone, userPassword)){
			Toast.makeText(this, "��Ϣ���³ɹ�" , 
					Toast.LENGTH_LONG).show();
			//ʵ����SharedPreferences���󣨵�һ���� 
			SharedPreferences mySharedPreferences= getSharedPreferences("test", 
			Activity.MODE_PRIVATE); 
			//ʵ����SharedPreferences.Editor���󣨵ڶ����� 
			SharedPreferences.Editor editor = mySharedPreferences.edit(); 
			//��putString�ķ����������� 
			editor.putString("userPhone", userPhone); 
			//�ύ��ǰ���� 
			editor.commit(); 
			//ʹ��toast��Ϣ��ʾ����ʾ�ɹ�д������
		}
		}
	}
	}
		
		public boolean isUserAllValid(){
		if(mCard.getText().toString().trim().length()!=19){
		Toast.makeText(this,"���Ų����Ϲ���",Toast.LENGTH_SHORT).show();
		//Toast.makeText(this,getString(R.string.card_error)
		return false;
		} else if (mPhone.getText().toString().trim().length()!=11){
		Toast.makeText(this,"�ֻ��Ų����Ϲ���",Toast.LENGTH_SHORT).show();
		//Toast.makeText(this,getString(R.string.phone_error)
		return false;
		} else if (mPassword.getText().toString().trim().length()!=6){
		Toast.makeText(this,"���벻���Ϲ���",Toast.LENGTH_SHORT).show();
		//Toast.makeText(this,getString(R.string.password_error)
		return false;
		}
		return true;
		}
	
	protected void onResume() {
		if (mUserDataManager == null) {
			mUserDataManager = new UserDataManager(this);
			mUserDataManager.openDataBase();
        }
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mUserDataManager != null) {
			mUserDataManager.closeDataBase();
			mUserDataManager = null;
        }
		super.onPause();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			new AlertDialog.Builder(this).setTitle(scanResult);
			//resultTextView.setText(scanResult);
		}
	}
}
	