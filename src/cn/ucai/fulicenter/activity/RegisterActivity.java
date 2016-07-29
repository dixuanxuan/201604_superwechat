/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.FuLiCenterServerApplication;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.listener.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.Utils;

import com.easemob.exceptions.EaseMobException;

import java.io.File;

/**
 * 注册页
 * 
 */
public class RegisterActivity extends BaseActivity {
	private static  final String TAG=RegisterActivity.class.getSimpleName();
	private EditText userNameEditText;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;
	private RelativeLayout mLayoutRegister;
	private  ImageView ivAvatar;
	private  EditText metNick;
	private  OnSetAvatarListener mOnSetAvatarListener;
	private  String avatarName;
	 ProgressDialog pd;
	 String  nick;
	 String username ;
	 String pwd ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(cn.ucai.fulicenter.R.layout.activity_register);
		initView();
		setListener();

	}

	private void setListener() {
		findViewById(R.id.btnLogin).setOnClickListener(
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findViewById(R.id.btnRegister).setOnClickListener(
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				register();
			}
		});
		mLayoutRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnSetAvatarListener= new OnSetAvatarListener(RegisterActivity.
						this,R.id.layout_register,getUserName(),I.AVATAR_TYPE_USER_PATH);
			}
		});


	}

	private String getUserName() {
		avatarName = String.valueOf(System.currentTimeMillis());
		return avatarName;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode!=RESULT_OK){
			return;
		}
		mOnSetAvatarListener.setAvatar(requestCode,data,ivAvatar);

	}

	private void initView() {
		ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
		metNick = (EditText) findViewById(R.id.Nick);
		mLayoutRegister = (RelativeLayout) findViewById(R.id.layouutRegister);
		userNameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(cn.ucai.fulicenter.R.id.password);
		confirmPwdEditText = (EditText) findViewById(cn.ucai.fulicenter.R.id.confirm_password);
	}

	/**
	 * 注册
	 *
	 */
	private void register() {
		  nick=metNick.getText().toString().trim();
		 username = userNameEditText.getText().toString().trim();
		 pwd = passwordEditText.getText().toString().trim();
		String confirm_pwd = confirmPwdEditText.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, getResources().getString(cn.ucai.fulicenter.R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		}
		else if (!username.matches("[\\w][\\w\\d_]+")) {
			Toast.makeText(this, getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		}
		else if (TextUtils.isEmpty(nick)) {
			Toast.makeText(this, getResources().getString(R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
			metNick.requestFocus();
			return;
		}else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, getResources().getString(cn.ucai.fulicenter.R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			passwordEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(this, getResources().getString(cn.ucai.fulicenter.R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			confirmPwdEditText.requestFocus();
			return;
		} else if (!pwd.equals(confirm_pwd)) {
			Toast.makeText(this, getResources().getString(cn.ucai.fulicenter.R.string.Two_input_password), Toast.LENGTH_SHORT).show();
			return;
		}

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
			 pd = new ProgressDialog(this);
			pd.setMessage(getResources().getString(cn.ucai.fulicenter.R.string.Is_the_registered));
			pd.show();
			registerAppServer();

		}
	}

	private  void  unRegisterServer(){
		final OkHttpUtils2<Result> utile=new OkHttpUtils2<>();
		utile.setRequestUrl(I.REQUEST_UNREGISTER)
				.addParam(I.User.USER_NAME,username)
				.addParam(I.User.PASSWORD,pwd)
				.targetClass(Result.class)
				.execute(new OkHttpUtils2.OnCompleteListener<Result>() {
					@Override
					public void onSuccess(Result result) {
						Log.i(TAG,"result ..."+result);


					}

					@Override
					public void onError(String error) {
						Log.i(TAG,"register fail ..."+error);




					}
				});
	}
	private void registerAppServer() {

		File file=new File(OnSetAvatarListener.getAvatarPath(RegisterActivity.this,
				I.AVATAR_TYPE_USER_PATH),avatarName+I.AVATAR_SUFFIX_JPG);
		final OkHttpUtils2<Result> utils2=new OkHttpUtils2<>();
		utils2.setRequestUrl(I.REQUEST_REGISTER)
				.addParam(I.User.USER_NAME,username)
				.addParam(I.User.PASSWORD,pwd)
				.addParam(I.User.NICK,nick)
				.targetClass(Result.class)
				.addFile(file)
				.execute(new OkHttpUtils2.OnCompleteListener<Result>() {
					@Override
					public void onSuccess(Result result) {
						Log.e(TAG,"result="+result);
						if (result.isRetMsg()){
							registerEMServer();

						}else {
							Log.e(TAG,"register fail ..."+result.getRetCode());
							pd.dismiss();
							Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.fulicenter.R.string.Registration_failed)+
									Utils.getResourceString(RegisterActivity.this,result.getRetCode()), Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onError(String error) {

						Log.i(TAG,"register fail ..."+error);
						pd.dismiss();

					}
				});


	}

	private   void registerEMServer(){
		new Thread(new Runnable() {
			public void run() {
				try {
					// 调用sdk注册方法
					EMChatManager.getInstance().createAccountOnServer(username, pwd);
					runOnUiThread(new Runnable() {
						public void run() {
							if (!RegisterActivity.this.isFinishing())
								pd.dismiss();
							// 保存用户名
							FuLiCenterServerApplication.getInstance().setUserName(username);
							Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.fulicenter.R.string.Registered_successfully), Toast.LENGTH_LONG).show();
							finish();
						}
					});
				} catch (final EaseMobException e) {
					unRegisterServer();
					runOnUiThread(new Runnable() {
						public void run() {
							if (!RegisterActivity.this.isFinishing())
								pd.dismiss();
							int errorCode=e.getErrorCode();
							if(errorCode==EMError.NONETWORK_ERROR){
								Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.fulicenter.R.string.network_anomalies), Toast.LENGTH_SHORT).show();
							}else if(errorCode == EMError.USER_ALREADY_EXISTS){
								Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.fulicenter.R.string.User_already_exists), Toast.LENGTH_SHORT).show();
							}else if(errorCode == EMError.UNAUTHORIZED){
								Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.fulicenter.R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
							}else if(errorCode == EMError.ILLEGAL_USER_NAME){
								Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.fulicenter.R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.fulicenter.R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		}).start();
	}
	public void back(View view) {
		finish();
	}

}
