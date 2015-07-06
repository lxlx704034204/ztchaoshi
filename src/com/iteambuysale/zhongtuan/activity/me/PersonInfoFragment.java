package com.iteambuysale.zhongtuan.activity.me;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.HomeActivity;
import com.iteambuysale.zhongtuan.background.FileUpLoadAsync;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.utilities.FileUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;

public class PersonInfoFragment extends Fragment implements OnClickListener, NetAsyncListener {
	private ImageView iv_touxiang;
	private TextView tv_user_zhanghao;
	private TextView tv_user_name;
	private TextView tv_sex;
	private TextView tv_birth_data;
	private static final int REQUES_CAMERA = 1001;
	private static final int REQUES_ALBUM = 1002;
	private static final String AVATAR_FILENAME = "avatar.png";
	private int changeStatus;
	private static final int SAX=0;
	private static final int NICHENG=1;
	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_personinfo_l);
		initview();
		initdata();
		
	}*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.me_personinfo_l, null);
		initview(view);
		initdata();
		return view;
	}
	public void initview(View view){
		LinearLayout ll_touxiang =(LinearLayout) view.findViewById(R.id.ll_touxiang);
		LinearLayout ll_zhanghao =(LinearLayout) view.findViewById(R.id.ll_zhanghao);
		LinearLayout ll_nicheng =(LinearLayout) view.findViewById(R.id.ll_nicheng);
		LinearLayout ll_sex =(LinearLayout) view.findViewById(R.id.ll_sex);
		LinearLayout ll_birth =(LinearLayout) view.findViewById(R.id.ll_birth);
		LinearLayout ll_address =(LinearLayout) view.findViewById(R.id.ll_address);
		LinearLayout ll_usersafe =(LinearLayout) view.findViewById(R.id.ll_usersafe);
		ll_touxiang.setOnClickListener(this);
		ll_zhanghao.setOnClickListener(this);
		ll_nicheng.setOnClickListener(this);
		ll_sex.setOnClickListener(this);
		ll_birth.setOnClickListener(this);
		ll_address.setOnClickListener(this);
		ll_usersafe.setOnClickListener(this);
		iv_touxiang = (ImageView)view.findViewById(R.id.iv_touxiang);
		tv_user_zhanghao = (TextView) view.findViewById(R.id.tv_user_zhanghao);
		tv_user_name = (TextView)view.findViewById(R.id.tv_user_name);
		tv_sex = (TextView)view.findViewById(R.id.tv_sex);
		tv_birth_data = (TextView)view.findViewById(R.id.tv_birth_data);
		TextView tv_header_tittle=(TextView) view.findViewById(R.id.tv_header_tittle);
		tv_header_tittle.setText("我的账户");
		ImageView iv_login_finish=(ImageView) view.findViewById(R.id.iv_login_finish);
		iv_login_finish.setOnClickListener(this);
	}
	public void initdata(){
		String uid = ZhongTuanApp.getInstance().getAppSettings().uid;
		user = Model.load(new User(), uid);
		ImageUtilities.loadBitMap(user.getAvatar(), iv_touxiang);
		tv_user_zhanghao.setText(user.getMobile());
		tv_user_name.setText(user.getNickname());
		tv_birth_data.setText(user.getBirthday());
		if(user.getSex()==null){
			return ;
		}
		switch (user.getSex()) {
		case "1":
			tv_sex.setText("男");
			break;
		case "2":
			tv_sex.setText("女");
			break;
		default:
			tv_sex.setText("保密");
			break;
		
		}
	
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_touxiang:
			showchangetouxiangdialog();
			
			break;
		case R.id.ll_zhanghao:
			
			break;
		case R.id.ll_nicheng:
			changenichengdialog();
			break;
		case R.id.ll_sex:
			changeSaxDialog();
			break;
		case R.id.ll_birth:
			
			break;
		case R.id.ll_address:
			
			break;
		case R.id.ll_usersafe:
			
			break;
		case R.id.iv_login_finish:
			((HomeActivity)getActivity()).getHomeActor().changeTab(3);
			break;
		default:
			break;
		}
		
	}
	private void changeSaxDialog() {
		View view=View.inflate(getActivity(), R.layout.change_sax_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.show();
		RelativeLayout rl_male=(RelativeLayout) view.findViewById(R.id.rl_male);
		RelativeLayout rl_female=(RelativeLayout) view.findViewById(R.id.rl_female);
		RelativeLayout rl_secret=(RelativeLayout) view.findViewById(R.id.rl_secret);
		ImageView iv_male_right=(ImageView)view.findViewById(R.id.iv_male_right);
		ImageView iv_female_right=(ImageView)view.findViewById(R.id.iv_female_right);
		ImageView iv_secret=(ImageView)view.findViewById(R.id.iv_secret);
		if(user.getSex()==null){
		     user.setSex("0");
		}
		switch (user.getSex()) {
		case "1":
			iv_male_right.setVisibility(View.VISIBLE);
			iv_female_right.setVisibility(View.INVISIBLE);
			iv_secret.setVisibility(View.INVISIBLE);
			break;
		case "2":
			tv_sex.setText("女");
			iv_male_right.setVisibility(View.INVISIBLE);
			iv_female_right.setVisibility(View.VISIBLE);
			iv_secret.setVisibility(View.INVISIBLE);
			break;
		default:
			tv_sex.setText("保密");
			iv_male_right.setVisibility(View.INVISIBLE);
			iv_female_right.setVisibility(View.INVISIBLE);
			iv_secret.setVisibility(View.VISIBLE);
			break;
		
		}
		rl_male.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			  changeSax("1");
			  dialog.dismiss();
				
			}
		});
		rl_female.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			  changeSax("2");
			  dialog.dismiss();
				
			}
		});
		
		rl_secret.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeSax("0");
				dialog.dismiss();
				
			}
		});
	}
	private String nicheng;
	private User user;
	private void changenichengdialog() {
		View view=View.inflate(getActivity(), R.layout.change_nichen_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.show();
		TextView tv_sure_nicheng=(TextView) view.findViewById(R.id.tv_sure_nicheng);
		final EditText et_nicheng =(EditText) view.findViewById(R.id.et_nicheng);
		tv_sure_nicheng.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nicheng = et_nicheng.getText().toString();
				postEdit(nicheng);
				changeStatus=NICHENG;
				dialog.dismiss();
			}
		});
	}
	
	private void showchangetouxiangdialog() {
		View view = View.inflate(getActivity(), R.layout.touxiang_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.show();
		TextView tv_select_by_photo=(TextView) view.findViewById(R.id.tv_select_by_photo);
		TextView tv_select_by_ulbum=(TextView) view.findViewById(R.id.tv_select_by_ulbum);
		tv_select_by_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(FileUtilities.getImagePath() + AVATAR_FILENAME);
				Uri imageUri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, REQUES_CAMERA);
				dialog.dismiss();
				
			}
		});
		
		tv_select_by_ulbum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),AlbumActivity.class);
				startActivityForResult(intent,REQUES_ALBUM);
				
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUES_CAMERA:
			if (resultCode == getActivity().RESULT_OK) {
				updateAvatar();
			} else {
			}
			break;
		case REQUES_ALBUM:
			if (resultCode == getActivity().RESULT_OK) {
				Bitmap bitmap=data.getParcelableExtra("bitmap");
				updateAvatar(bitmap);
			} else {
			}
		
			break;
		default:
			break;
		}
	}
	/**
	 * 更新用户头像 
	 * Anddward.Liao <Anddward@gmail.com> 20142014-12-12上午11:45:27
	 */
	private void updateAvatar() {
		Bitmap bitmap = FileUtilities.readImageWithFileName("avatar.png", 102,
				102);
		Bitmap squareBitmap = ImageUtilities.cropSquareBitmap(bitmap);
		Bitmap circleBitmap = ImageUtilities.cropCircleAvatar(squareBitmap);
		ImageUtilities.compressBitmap(circleBitmap, "avatar.png", 100, 10);
		updateAvata();
		uploadFile();
	}

	/**
	 * 用户更新头像带路径（相册上传）
	 * 
	 * @param path
	 */
	private void updateAvatar(Bitmap bitmap) {
		if(bitmap==null){
			return;
		}
		Bitmap squareBitmap = ImageUtilities.cropSquareBitmap(bitmap);
		Bitmap circleBitmap = ImageUtilities.cropCircleAvatar(squareBitmap);
		ImageUtilities.compressBitmap(circleBitmap, "avatar.png", 100, 10);
		iv_touxiang.setImageBitmap(circleBitmap);
	    updateAvata();
		uploadFile();

	}
	public void updateAvata() {
		// 删除旧的头像
		User user = Model.load(new User(), ZhongTuanApp.getInstance().getAppSettings().uid);
		ImageUtilities.clearImageCache(user.getAvatar(), iv_touxiang);
		//ImageUtilities.loadBitMap("avatar.png", $iv("avatar"),"avatar.png");//以前的版本
		
	}
	/**
	 * 上传头像到服务器
	 * Anddward.Liao <Anddward@gmail.com> 20142014-12-11下午3:40:05
	 */
	private void uploadFile() {
		LogUtilities.Log(D.DEBUG, "did enter uploadFile()",D.DEBUG_DEBUG);
		FileUpLoadAsync upload_task = new FileUpLoadAsync(D.API_MY_SETAVATAR,
				new String[] { AVATAR_FILENAME }, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				String picUrl = JsonUtilities.parseModelByType(elData,
						String.class);
				String uid = ZhongTuanApp.getInstance().getAppSettings().uid;
				User currentUser = Model.load(new User(), uid);
				currentUser.setAvatar(picUrl);
				currentUser.save();
				return null;
			}

			@Override
			public InputStream processBitmapBeforeUpload(File file) throws FileNotFoundException {
				return ImageUtilities.compressBitmap(file, 100, 10);
			}

			@Override
			public void beforeRequestInBackground(
					ArrayList<NameValuePair> mParams) {
				mParams.add(new BasicNameValuePair("avaname", AVATAR_FILENAME));
			}
		};
		upload_task.execute();
	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		 switch (reqUrl) {
         case D.API_MY_SETAVATAR:
 			Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();

		default:
			break;
		}
		Toast.makeText(getActivity(), errMsg, 0).show();
	}
	@Override
	public void onResultSuccess(String reqUrl, Object data) {
         switch (reqUrl) {
         case D.API_MY_SETAVATAR:
 			Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
         case D.API_USER_EDITUSER:
        	 switch (changeStatus) {
			case NICHENG:
				 Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
	        	 tv_user_name.setText(nicheng);
				break;
			case SAX:	
				Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
				switch (((User)data).getSex()) {
				case "1":
					tv_sex.setText("男");
					break;
				case "2":
                   tv_sex.setText("女");
                   break;
				default:
				   tv_sex.setText("保密");
					break;
				}
				break;

			default:
				break;
			}
        	
        	 break;
		default:
			break;
		}
		
	}
	@Override
	public void onTokenTimeout() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 修改昵称
	 */
	public void postEdit(final String nicheng ){
		NetAsync task_postEdit = new NetAsync(D.API_USER_EDITUSER,this) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				User user= JsonUtilities.parseModelByType(elData, User.class);
				user.save();
				return user;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_NICKNAME, nicheng));
				/*params.add(new BasicNameValuePair(D.ARG_EDITUSER_SEX, getSex()));
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_BIRTHDAY, getBirthday()));
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_EMAIL, getEmail()));
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_SIGNATE, getSignate()));*/
			}
		};
		task_postEdit.execute();
	}
	/**
	 * 修改昵称
	 */
	public void changeSax(final String sex ){
		NetAsync task_postEdit = new NetAsync(D.API_USER_EDITUSER,this) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				User user= JsonUtilities.parseModelByType(elData, User.class);
				user.save();
				return user;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				//params.add(new BasicNameValuePair(D.ARG_EDITUSER_NICKNAME, nicheng));
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_SEX, sex+""));
				/*params.add(new BasicNameValuePair(D.ARG_EDITUSER_BIRTHDAY, getBirthday()));
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_EMAIL, getEmail()));
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_SIGNATE, getSignate()));*/
			}
		};
		task_postEdit.execute();
	}
}
