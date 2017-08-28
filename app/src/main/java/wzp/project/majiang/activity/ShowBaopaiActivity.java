package wzp.project.majiang.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.Arrays;

import wzp.project.majiang.R;
import wzp.project.majiang.activity.base.BaseActivity;
import wzp.project.majiang.helper.widget.MyApplication;
import wzp.project.majiang.util.CalculateUtil;

public class ShowBaopaiActivity extends BaseActivity {

	private ImageButton ibtnBack;
	private ImageButton ibtnMoreFun;

	private TextView tvId;
	private LinearLayout linearMajiang;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_baopai);

		initParam();
		initWidget();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (MyApplication.btClientHelper == null
				|| !MyApplication.btClientHelper.isBluetoothConnected()) {
			showToast("蓝牙设备尚未连接");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initParam() {

	}

	private void initWidget() {
		ibtnBack = (ImageButton) findViewById(R.id.ibtn_back);
		ibtnMoreFun = (ImageButton) findViewById(R.id.ibtn_moreFun);

		tvId = (TextView) findViewById(R.id.tv_id);
		linearMajiang = (LinearLayout) findViewById(R.id.linear_majiang);

		ibtnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	/**
	 * 启动Activity
	 *
	 * @param context
	 */
	public static void myStartActivity(Context context) {
		Intent intent = new Intent(context, ShowBaopaiActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 更新麻将牌
	 *
	 * @param recvData
	 */
	private void updateMajiang(byte[] recvData) {
		final byte[] copyRecvData = Arrays.copyOf(recvData, recvData.length);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvId.setText(Integer.toHexString(CalculateUtil.byteToInt(copyRecvData[1])));

				int num = CalculateUtil.byteToInt(copyRecvData[2]);
				// num最多不能超过17
				if (num > 19) {
					Log.e(LOG_TAG, "麻将牌数目异常：" + num);
					return;
				}

				if(num > linearMajiang.getChildCount()) {
					int addedChildCount = num - linearMajiang.getChildCount();

//					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//							LinearLayout.LayoutParams.WRAP_CONTENT,
//							LinearLayout.LayoutParams.WRAP_CONTENT);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							(int) getResources().getDimensionPixelSize(R.dimen.a_majiang_width),
							(int) getResources().getDimensionPixelSize(R.dimen.a_majiang_height));
//					params.setMargins(2, 0, 0, 0);
					ImageView ivMajiang = null;
					for (int i = 0; i < addedChildCount; i++) {
						ivMajiang = new ImageView(ShowBaopaiActivity.this);
						ivMajiang.setLayoutParams(params);

						linearMajiang.addView(ivMajiang);
					}
				} else {
					int delChidCount = linearMajiang.getChildCount() - num;
					for (int i = 0; i < delChidCount; i++) {
						linearMajiang.removeViewAt(linearMajiang.getChildCount() - 1);
					}
				}

				Bitmap majiangBitmap = null;
				for (int i = 0; i < num; i++) {
					majiangBitmap = BitmapFactory.decodeResource(getResources(),
							CalculateUtil.getMajiangImage(CalculateUtil.byteToInt(copyRecvData[3 + i])));
					((ImageView) linearMajiang.getChildAt(i)).setImageBitmap(majiangBitmap);
				}
			}
		});
	}


	@Override
	protected void onBluetoothDataReceived(byte[] recvData) {
		int funCode = CalculateUtil.byteToInt(recvData[1]);
		if (funCode >= 0xc1 && funCode <= 0xcf) {
			updateMajiang(recvData);
		}
	}
}