package cn.com.jdsc;

public class UserData {

	private String userName;
	private String userPwd;
	private String userPhone;
	private String userCard;
	private String userPassword;
	private int userId;
	private double userMoney;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserPhone(){
		return userPhone;
	}
	
	public void setUserPhone(String userPhone){
		this.userPhone=userPhone;
	}
	
	public String getUserCard(){
		return userCard;
	}
	
	public void setUserCard(String userCard){
		this.userCard=userCard;
	}
	
	public String getUserPassword(){
		return userPassword;
	}
	
	public void setUserPassword(String userPassword){
		this.userPassword=userPassword;
	}
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public double getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(int userMoney) {
		this.userMoney = userMoney;
	}

	public UserData(String userName, String userPwd, String 

userPhone, String userCard, String userPassword, int userId, double userMoney) {
		super();
		this.userName = userName;
		this.userPwd = userPwd;
		this.userPhone=userPhone;
		this.userCard=userCard;
		this.userPassword=userPassword;
		this.userId = userId;
		this.userMoney = userMoney;
	}

	public UserData(String userName, String userPwd, String 

userPhone, String userCard,String userPassword, double userMoney) {
		super();
		this.userName = userName;
		this.userPwd = userPwd;
		this.userPhone=userPhone;
		this.userCard=userCard;
		this.userPassword=userPassword;
		this.userMoney = userMoney;
	}
	
	public UserData(String userName, String userPwd, int userId) {
		super();
		this.userName = userName;
		this.userPwd = userPwd;
		this.userId = userId;
	}

	public UserData(String userName, String userPwd) {
		super();
		this.userName = userName;
		this.userPwd = userPwd;
	}

}
