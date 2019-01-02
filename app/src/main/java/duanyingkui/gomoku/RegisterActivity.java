package duanyingkui.gomoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_name;                        //用户名编辑
    private EditText et_password;                            //密码编辑
    private Button btn_send;                   //注册按钮
    private UserDataManager mUserDataManager;         //用户数据管理类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //通过id找到相应的控件
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(m_register_Listener);      //注册界面按钮的监听事件
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
    }
    View.OnClickListener m_register_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send:                       //确认按钮的监听事件
                    register_check();
                    break;
            }
        }
    };
    public void register_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            String userName = et_name.getText().toString().trim();
            String userPwd = et_password.getText().toString().trim();
            //检查用户是否存在
            int count =mUserDataManager.findUserByName(userName);
            //用户已经存在时返回，给出提示文字
            if(count>0){
                Toast.makeText(this, "用户已存在！",Toast.LENGTH_SHORT).show();
                return ;
            }else {
                UserData mUser = new UserData(userName, userPwd);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mUser); //新建用户信息
                if (flag == -1) {
                    Toast.makeText(this, "注册失败",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent_Register_to_Login = new Intent(RegisterActivity.this,LoginActivity.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                }
            }
        }
    }
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
}
