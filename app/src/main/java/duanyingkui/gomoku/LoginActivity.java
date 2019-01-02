package duanyingkui.gomoku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    public int pwdresetFlag=0;
    private EditText et_name;                        //用户名编辑
    private EditText et_password;                            //密码编辑
    private Button btn_send;                   //注册按钮
    private Button btn_login;                      //登录按钮
//    private Button mCancleButton;                     //注销按钮
//    private CheckBox mRememberCheck;

    private SharedPreferences login_sp;
    private String userNameValue,passwordValue;

    private View loginView;                           //登录
    private View loginSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;
    private UserDataManager mUserDataManager;         //用户数据管理类


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //通过id找到相应的控件
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_send = (Button) findViewById(R.id.btn_send);

        btn_send.setOnClickListener(mListener);                      //采用OnClickListener方法设置不同按钮按下之后的监听事件
        btn_login.setOnClickListener(mListener);

        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
    }
    View.OnClickListener mListener = new View.OnClickListener() {                  //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send:                            //登录界面的注册按钮
                    Intent btn_send = new Intent(LoginActivity.this,RegisterActivity.class) ;    //切换Login Activity至User Activity
                    startActivity(btn_send);
                    finish();
                    break;
                case R.id.btn_login:                              //登录界面的登录按钮
                    login();
                    break;

            }
        }
    };

    public void login() {                                              //登录按钮监听事件
        if (isUserNameAndPwdValid()) {
            String userName = et_name.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = et_password.getText().toString().trim();
            SharedPreferences.Editor editor =login_sp.edit();
            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if(result==1){                                             //返回1说明用户名和密码均正确
                //保存用户名和密码
                editor.putString("USER_NAME", userName);
                editor.putString("PASSWORD", userPwd);

//                editor.putBoolean("mRememberCheck", false);
                editor.commit();

//                Intent intent = new Intent(LoginActivity.this,MainActivity.class) ;    //切换Login Activity至User Activity
//                startActivity(intent);
//                finish();
                Toast.makeText(this, "登录成功",Toast.LENGTH_SHORT).show();//登录成功提示
                Intent intent = new Intent(LoginActivity.this,MainActivity.class) ;    //切换Login Activity至User Activity
                startActivity(intent);
                finish();
            }else if(result==0){
                Toast.makeText(this, "登录失败",Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }
    }
//    public void cancel() {           //注销
//        if (isUserNameAndPwdValid()) {
//            String userName = et_name.getText().toString().trim();    //获取当前输入的用户名和密码信息
//            String userPwd = et_password.getText().toString().trim();
//            int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
//            if(result==1){                                             //返回1说明用户名和密码均正确
//
//                Toast.makeText(this, getString(R.string.cancel_success),Toast.LENGTH_SHORT).show();//登录成功提示
//                et_name.setText("");
//                et_password.setText("");
//                mUserDataManager.deleteUserDatabyname(userName);
//            }else if(result==0){
//                Toast.makeText(this, getString(R.string.cancel_fail),Toast.LENGTH_SHORT).show();  //登录失败提示
//            }
//        }
//
//    }

    public boolean isUserNameAndPwdValid() {
        if (et_name.getText().toString().trim().equals("")) {
            Toast.makeText(this, "账户名为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_password.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码为空",
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
