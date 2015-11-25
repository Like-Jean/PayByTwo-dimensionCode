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
	//同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象 
	SharedPreferences sharedPreferences= getSharedPreferences("test", 
	Activity.MODE_PRIVATE); 
	// 使用getString方法获得value，注意第2个参数是value的默认值 
	String userBalance =sharedPreferences.getString("userBalance", "");
	
	display.setText("支付方:"+js.getString("payer")+"收款方:"+js.getString("receiver")+"金额:"+js.getString("money")+"余额:"+userBalance);
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


