package com.iteambuysale.zhongtuan.activity.me;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.SuperActivity;
import com.iteambuysale.zhongtuan.actor.me.PersonInfoActor;
import com.iteambuysale.zhongtuan.background.FileUpLoadAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.me.PersonInfoListener;
import com.iteambuysale.zhongtuan.model.DateTime;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.utilities.FileUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class PersonInfoActivity extends SuperActivity implements
		OnDateSetListener, PersonInfoListener, OnClickListener {
	private PersonInfoActor mActor;
	private CustomProgressDialog mProgressDialog;
	private static final String AVATAR_FILENAME = "avatar.png";
	private static final int REQUES_CAMERA = 1001;
	private static final int REQUES_ALBUM = 1002;
	TextView bg;
	Button albumBtn, cameraBtn, cancelBtn, back;
	PopupWindow popupWindow;
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, false);
		setContentView(R.layout.me_personinfo);
		iv = (ImageView) findViewById(R.id.avatar);
		mActor = new PersonInfoActor(this);
		mActor.initViews();
	}

	/* ==================== Button Click Events ================= */
	/**
	 * 修改信息按钮被点击
	 * 
	 * @param v
	 */
	public void onClickEditBtn(View v) {
		mActor.changeStateEdit();
	}

	/**
	 * 提交修改信息按钮被点击
	 * 
	 * @param v
	 */
	public void onClickPostBtn(View v) {
		mProgressDialog = CustomProgressDialog.createDialog(this);
		mProgressDialog.show();
		mActor.postEdit();
	}

	/**
	 * 生日区域被点击
	 * 
	 * @param v
	 */
	public void onClickBirthdayArea(View v) {
		DateTime date = mActor.getBirthDateTime();
		new DatePickerDialog(this, this, date.getYear(), date.getMonth(),
				date.getDay()).show();
	}

	/**
	 * 点击头像
	 * @param v
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-11下午3:58:15
	 */
	public void onClickAvatar(View v) {
		showDialog();
	}
	
	/**
	 * 显示选择相机相册的Popupwindow
	 * 
	 * 2015-1-15
	 * lforxeverc
	 */
	private void showDialog() {
		back = (Button) findViewById(R.id.back);
		View view = getLayoutInflater().inflate(R.layout.popup_dialog, null,
				false);
		bg = (TextView) view.findViewById(R.id.bg);
		albumBtn = (Button) view.findViewById(R.id.albumBtn);
		cameraBtn = (Button) view.findViewById(R.id.cameraBtn);
		cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
		bg.setOnClickListener(this);
		albumBtn.setOnClickListener(this);
		cameraBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popupWindow.showAsDropDown(back);
	}

	/* =================== status Events ==================== */
	@Override
	protected void onActivityResult(int reqCode, int responCode, Intent data) {
		switch (reqCode) {
		case REQUES_CAMERA:
			if (responCode == RESULT_OK) {
				updateAvatar();
			} else {
			}
			break;
		case REQUES_ALBUM:
			if (responCode == RESULT_OK) {
				Bitmap bitmap=data.getParcelableExtra("bitmap");
				updateAvatar(bitmap);
			} else {
			}
		
			break;
		default:
			break;
		}
		// you should't come here
		// why??
	}

	/* 日期选择 */
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		DateTime date = new DateTime(year, monthOfYear, dayOfMonth);
		mActor.setBirthday(date.toString());
	}

	/* 网络接口回调 */
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		switch (reqUrl) {
		case D.API_USER_EDITUSER:
			mProgressDialog.dismiss();
			Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show();
			mActor.changeStateDisplay();
			break;
		case D.API_MY_SETAVATAR:
			Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show();
		default:
			break;
		}

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_USER_EDITUSER:
			mProgressDialog.dismiss();
			mActor.changeStateDisplay();
			finish();
			break;
		case D.API_MY_SETAVATAR:
			Toast.makeText(this, "上传成功", Toast.LENGTH_LONG).show();
		default:
			break;
		}
	}

	@Override
	public void onTokenTimeout() {
		mProgressDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,true);
	}

	/**
	 * 启动相机 
	 * Anddward.Liao <Anddward@gmail.com> 20142014-12-11上午10:16:10
	 */
	private void setUpCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(FileUtilities.getImagePath() + AVATAR_FILENAME);
		Uri imageUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, REQUES_CAMERA);
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
		mActor.updateAvata();
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
		iv.setImageBitmap(circleBitmap);
		mActor.updateAvata();
		uploadFile();

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
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bg:
			popupWindow.dismiss();
			break;
		case R.id.cameraBtn:
			popupWindow.dismiss();
			setUpCamera();
			break;
		case R.id.albumBtn:
			popupWindow.dismiss();
			Intent intent=new Intent(PersonInfoActivity.this,AlbumActivity.class);
			startActivityForResult(intent,REQUES_ALBUM);			
			break;
		case R.id.cancelBtn:
			popupWindow.dismiss();
			break;

		}

	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(popupWindow!=null&&popupWindow.isShowing()){
			popupWindow.dismiss();
		}
	}
}
