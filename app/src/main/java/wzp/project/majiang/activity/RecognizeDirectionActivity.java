package wzp.project.majiang.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

import wzp.project.majiang.R;
import wzp.project.majiang.activity.base.BaseActivity;
import wzp.project.majiang.helper.constant.ProjectConstants;
import wzp.project.majiang.helper.widget.MyApplication;
import wzp.project.majiang.util.CRC16;
import wzp.project.majiang.util.CalculateUtil;

public class RecognizeDirectionActivity extends BaseActivity {

	private ImageButton ibtnBack;

	private TextView tvTip;
	private Button btnNorth;
	private Button btnSouth;
	private Button btnWest;
	private Button btnEast;

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ibtn_back:
					onBackPressed();
					break;

				case R.id.btn_east:
					if (MyApplication.btClientHelper.isBluetoothConnected()) {
						byte[] sendMsg = new byte[ProjectConstants.SEND_MSG_LENGTH];
						sendMsg[0] = (byte) 0xfe;
						sendMsg[1] = (byte) 0xd1;
						sendMsg[2] = (byte) 0x01;
						byte[] crc = CRC16.getCrc16(sendMsg, ProjectConstants.SEND_MSG_LENGTH - 2);
						sendMsg[ProjectConstants.CRC_HIGH] = crc[0];
						sendMsg[ProjectConstants.CRC_LOW] = crc[1];

						MyApplication.btClientHelper.write(sendMsg);
					}
					break;

				case R.id.btn_south:
					if (MyApplication.btClientHelper.isBluetoothConnected()) {
						byte[] sendMsg = new byte[ProjectConstants.SEND_MSG_LENGTH];
						sendMsg[0] = (byte) 0xfe;
						sendMsg[1] = (byte) 0xd2;
						sendMsg[2] = (byte) 0x01;
						byte[] crc = CRC16.getCrc16(sendMsg, ProjectConstants.SEND_MSG_LENGTH - 2);
						sendMsg[ProjectConstants.CRC_HIGH] = crc[0];
						sendMsg[ProjectConstants.CRC_LOW] = crc[1];

						MyApplication.btClientHelper.write(sendMsg);
					}
					break;

				case R.id.btn_west:
					if (MyApplication.btClientHelper.isBluetoothConnected()) {
						byte[] sendMsg = new byte[ProjectConstants.SEND_MSG_LENGTH];
						sendMsg[0] = (byte) 0xfe;
						sendMsg[1] = (byte) 0xd3;
						sendMsg[2] = (byte) 0x01;
						byte[] crc = CRC16.getCrc16(sendMsg, ProjectConstants.SEND_MSG_LENGTH - 2);
						sendMsg[ProjectConstants.CRC_HIGH] = crc[0];
						sendMsg[ProjectConstants.CRC_LOW] = crc[1];

						MyApplication.btClientHelper.write(sendMsg);
					}
					break;

				case R.id.btn_north:
					if (MyApplication.btClientHelper.isBluetoothConnected()) {
						byte[] sendMsg = new byte[ProjectConstants.SEND_MSG_LENGTH];
						sendMsg[0] = (byte) 0xfe;
						sendMsg[1] = (byte) 0xd4;
						sendMsg[2] = (byte) 0x01;
						byte[] crc = CRC16.getCrc16(sendMsg, ProjectConstants.SEND_MSG_LENGTH - 2);
						sendMsg[ProjectConstants.CRC_HIGH] = crc[0];
						sendMsg[ProjectConstants.CRC_LOW] = crc[1];

						MyApplication.btClientHelper.write(sendMsg);
					}
					break;

				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recognize_direction);

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

		tvTip = (TextView) findViewById(R.id.tv_tip);
		btnNorth = (Button) findViewById(R.id.btn_north);
		btnSouth = (Button) findViewById(R.id.btn_south);
		btnWest = (Button) findViewById(R.id.btn_west);
		btnEast = (Button) findViewById(R.id.btn_east);

		ibtnBack.setOnClickListener(listener);

		btnNorth.setOnClickListener(listener);
		btnSouth.setOnClickListener(listener);
		btnWest.setOnClickListener(listener);
		btnEast.setOnClickListener(listener);
	}

	/**
	 * 启动Activity
	 *
	 * @param context
	 */
	public static void myStartActivity(Context context) {
		Intent intent = new Intent(context, RecognizeDirectionActivity.class);
		context.startActivity(intent);
	}


	@Override
	protected void onBluetoothDataReceived(byte[] recvData) {
		final int direction = CalculateUtil.byteToInt(recvData[1]);
		if (direction >= 0xd1 && direction <= 0xd4 && CalculateUtil.byteToInt(recvData[2]) == 0x01) {
			int state = CalculateUtil.byteToInt(recvData[3]);
			final StringBuilder strTip = new StringBuilder();
			if (state == 0x00) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						strTip.append("正在识别");
						switch (direction) {
							case 0xd1:
								strTip.append("东方位...");
								break;

							case 0xd2:
								strTip.append("南方位...");
								break;

							case 0xd3:
								strTip.append("西方位...");
								break;

							case 0xd4:
								strTip.append("北方位...");
								break;

							default:
								break;
						}
						tvTip.setText(strTip);
					}
				});
			} else if (state == 0x01) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						switch (direction) {
							case 0xd1:
								strTip.append("东方位");
								break;

							case 0xd2:
								strTip.append("南方位");
								break;

							case 0xd3:
								strTip.append("西方位");
								break;

							case 0xd4:
								strTip.append("北方位");
								break;

							default:
								break;
						}
						strTip.append("识别成功!");
						tvTip.setText(strTip);
					}
				});
			}
		}
	}
}