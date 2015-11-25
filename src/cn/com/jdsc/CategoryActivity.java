package cn.com.jdsc;

import com.logic.util.HttpListener;
import com.logic.util.HttpRequest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class CategoryActivity  extends Activity implements HttpListener{
	private TextView display;
	private HttpRequest req;
  @Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_category);
	display = (TextView)findViewById (R.id.content);
	
	req = new HttpRequest();
	req.setListener(this);
	String param = "";
	req.sendGet("getCatalog", param);
	
	
}
@Override
public void succToRequire(String msg, String data) {
	// TODO Auto-generated method stub
	JsonClass js = new JsonClass(data);
	//ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences���� 
	SharedPreferences sharedPreferences= getSharedPreferences("test", 
	Activity.MODE_PRIVATE); 
	// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ 
	String userBalance =sharedPreferences.getString("userBalance", "");
	
	display.setText("֧����:"+js.getString("payer")+"�տ:"+js.getString("receiver")+"���:"+js.getString("money")+"���:"+userBalance);
}
@Override
public void failToRequire(String msg, String data) {
	// TODO Auto-generated method stub
	
}
@Override
public void netWorkError(String msg, String data) {
	// TODO Auto-generated method stub
	
}

protected void onResume() {
	req = new HttpRequest();
	req.setListener(this);
	String param = "";
	req.sendGet("getCatalog", param);
	
	super.onResume();
}
}


