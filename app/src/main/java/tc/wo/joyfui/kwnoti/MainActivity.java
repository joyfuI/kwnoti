package tc.wo.joyfui.kwnoti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {
	private SharedPreferences sharePref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Point point = new Point();
		getWindowManager().getDefaultDisplay().getSize(point);	// 화면 크기 구하기

		GridLayout gridLayout = findViewById(R.id.grid_layout);
		gridLayout.getLayoutParams().height = point.x * gridLayout.getRowCount() / gridLayout.getColumnCount();	// 화면 크기에 따라 높이 조절

		sharePref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean welcome = sharePref.getBoolean("welcome", true);	// 첫 실행 여부 가져오기
		if (welcome) {	// 첫 실행이면
			Intent intent = new Intent(this, WelcomeActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		boolean welcome = sharePref.getBoolean("welcome", true);	// 첫 실행 여부 가져오기
		if (welcome) {	// 만약 웰컴 액티비티를 완료하지 않고 그냥 닫았다면
			finish();
		}
	}

	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
			case R.id.student_id:	// 모바일학생증
				intent = getPackageManager().getLaunchIntentForPackage("idoit.slpck.kwangwoon");
				if (intent == null) {	// 중앙도서관 앱이 설치되어 있지 않으면 플레이스토어 실행
					intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id=idoit.slpck.kwangwoon"));
				}
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				Toast.makeText(this, "모바일학생증 기능은 중앙도서관 앱을 이용해주세요.", Toast.LENGTH_SHORT).show();
				break;

			case R.id.u_campus:	// U-CAMPUS
				intent.setClass(this, UcampusActivity.class);
				startActivity(intent);
				break;

			case R.id.notice:	// 공지사항
				intent.setClass(this, NoticeActivity.class);
				startActivity(intent);
				break;

			case R.id.bachelor:	// 학사일정
				intent.setClass(this, BachelorActivity.class);
				startActivity(intent);
				break;

			case R.id.setting:	// 설정
				intent.setClass(this, SettingsActivity.class);
				startActivity(intent);
				break;

			default:
				Toast.makeText(this, "미완성된 기능입니다.", Toast.LENGTH_SHORT).show();
				break;
		}
	}
}
