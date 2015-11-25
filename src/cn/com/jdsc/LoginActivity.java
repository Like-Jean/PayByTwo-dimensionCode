package cn.com.jdsc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText mAccount;
	private EditText mPwd;
	private Button mRegisterButton;
	private Button mLoginButton;
	private Button mCancleButton;
	private View loginView;
	private View loginSuccessView;
	private TextView loginSuccessShow;
	private UserDataManager mUserDataManager;
	private int count;
	
	private RSAUtil rsa;
	
	
	
	byte[] code;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);

		mAccount = (EditText) findViewById(R.id.login_edit_account);
		mPwd = (EditText) findViewById(R.id.login_edit_pwd);
		mRegisterButton = (Button) findViewById(R.id.login_btn_register);
		mLoginButton = (Button) findViewById(R.id.login_btn_login);
		mCancleButton = (Button) findViewById(R.id.login_btn_cancle);
		loginView=findViewById(R.id.login_view);
		loginSuccessView=findViewById(R.id.login_success_view);
		loginSuccessShow=(TextView) findViewById(R.id.login_success_show);
		
		mRegisterButton.setOnClickListener(mListener);
		mLoginButton.setOnClickListener(mListener);
		mCancleButton.setOnClickListener(mListener);
		
		count = 0;
		rsa = new RSAUtil();
		//生成公钥
		try {
			codeKey.setRSAPublicKey(rsa.getRSAPublicKey());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//生成私钥
		try {
			codeKey.setRSAPrivateKey(rsa.getRSAPrivateKey());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		mAccount.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
		      public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(!hasFocus){
                    String userName=mAccount.getText().toString().trim();
                    if(userName.length()<4){
                        Toast.makeText(LoginActivity.this, "用户名不能小于4个字符", Toast.LENGTH_SHORT).show();
                    }
                }
            }
             
        });

        mPwd.setOnFocusChangeListener(new OnFocusChangeListener()
        {
 
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(!hasFocus){
                    String userPwd=mPwd.getText().toString().trim();
                    if(userPwd.length()<6){
                        Toast.makeText(LoginActivity.this, "密码不能小于6个字符", Toast.LENGTH_SHORT).show();
                    }
                }
            }
             });
		
		
		if (mUserDataManager == null) {
			mUserDataManager = new UserDataManager(this);
			mUserDataManager.openDataBase();
        }
		
	}

	OnClickListener mListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_btn_register:
				register();
				break;
			case R.id.login_btn_login:
				login();
				break;
			case R.id.login_btn_cancle:
				cancle();
				break;
			}
		}
	};

	public void login() {
		if (isUserNameAndPwdValid()) {
			String userName = mAccount.getText().toString().trim();
			String userPwd = mPwd.getText().toString().trim();
			String s;
			
			try {
				//String s1 = new String(codeKey.getRSAPublicKey().getEncoded());
				//Log.d("contentString", s1);
				code = rsa.encrypt(codeKey.getRSAPublicKey(),userPwd.getBytes());
				String s1 = new String(rsa.encrypt(codeKey.getRSAPublicKey(),userPwd.getBytes()));
				userPwd = s1;
				Log.d("en",userPwd);
				s1 = new String(codeKey.getRSAPublicKey().getEncoded());
				Log.d("pub1", s1);
				s1 = RSAHelper.getKeyString(codeKey.getRSAPublicKey());
				Log.d("pub2", s1);
				s1 = new String(RSAHelper.getPublicKey(s1).getEncoded());
				Log.d("pub3", s1);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				s = new String(rsa.decrypt(codeKey.getRSAPrivateKey(),code));
				userPwd = s;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
			if(result==1){
				//login success
				loginView.setVisibility(View.GONE);
				loginSuccessView.setVisibility(View.VISIBLE);
				loginSuccessShow.setText(getString(R.string.user_login_sucess, userName));
				Toast.makeText(this, getString(R.string.login_sucess),
						Toast.LENGTH_SHORT).show();
				
				//实例化SharedPreferences对象（第一步） 
				SharedPreferences mySharedPreferences= getSharedPreferences("test", 
				Activity.MODE_PRIVATE); 
				//实例化SharedPreferences.Editor对象（第二步） 
				SharedPreferences.Editor editor = mySharedPreferences.edit(); 
				//用putString的方法保存数据 
				editor.putString("userName", userName); 
				editor.putString("userPwd", userPwd);
				//提交当前数据 
				editor.commit(); 
				
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, JdscActivity.class);
				startActivity(intent);
			}else if(result==0){
				count++;
				if(count<3){
				//login failed,user does't exist
				Toast.makeText(this, getString(R.string.login_fail),
						Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(this, "密码错误三次，账号被锁定。",
							Toast.LENGTH_SHORT).show();
					mLoginButton.setEnabled(false);
				}
			} 
		}
	}

	public void register() {
		if (isUserNameAndPwdValid()) {
			String userName = mAccount.getText().toString().trim();
			if(userName.length()<4){
                Toast.makeText(LoginActivity.this, "用户名不能小于4个字符", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, getString(R.string.register_fail),
						Toast.LENGTH_SHORT).show();
                return;
			}
			String userPwd = mPwd.getText().toString().trim();
			if(userPwd.length()<6){
                Toast.makeText(LoginActivity.this, "密码不能小于6个字符", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, getString(R.string.register_fail),
						Toast.LENGTH_SHORT).show();
                return;
			}
			//check if user name is already exist
			int count=mUserDataManager.findUserByName(userName);
			if(count>0){
				Toast.makeText(this, getString(R.string.name_already_exist, userName),
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			try {
				code = rsa.encrypt(codeKey.getRSAPublicKey(),userPwd.getBytes());
				String s = new String(rsa.encrypt(codeKey.getRSAPublicKey(),userPwd.getBytes()));
				userPwd = s;
				Log.d("en",userPwd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String s;
			try {
				s = new String(rsa.decrypt(codeKey.getRSAPrivateKey(),code));
				userPwd = s;
				Log.d("de",s);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			UserData mUser = new UserData(userName, userPwd);
			mUserDataManager.openDataBase();
			long flag = mUserDataManager.insertUserData(mUser);
			if (flag == -1) {
				Toast.makeText(this, getString(R.string.register_fail),
						Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, getString(R.string.register_sucess),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void cancle() {
		mAccount.setText("");
		mPwd.setText("");
	}

	public boolean isUserNameAndPwdValid() {
		if (mAccount.getText().toString().trim().equals("")) {
			Toast.makeText(this, getString(R.string.account_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (mPwd.getText().toString().trim().equals("")) {
			Toast.makeText(this, getString(R.string.pwd_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
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
}
