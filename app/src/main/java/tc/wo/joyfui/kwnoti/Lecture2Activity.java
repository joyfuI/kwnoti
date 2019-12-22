package tc.wo.joyfui.kwnoti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import tc.wo.joyfui.kwnoti.u_campus.Lecture;

public class Lecture2Activity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture2);

		Intent intent = getIntent();
		final String url = intent.getStringExtra("URL");
		SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(this);
		final String ID = sharePref.getString("id", "");
		final String  PASSWD = sharePref.getString("passwd", "");

		Lecture lecture = new Lecture();
		lecture.setOnLectureListener(new Lecture.OnLectureListener() {
			@Override
			public void onSuccess(String content) {
				WebView webView = findViewById(R.id.web);
				webView.loadDataWithBaseURL("https://info.kw.ac.kr/", content, "text/html", "EUC-KR", null);	// 웹뷰 출력 (상대주소 대응)
			}

			@Override
			public void onFailure(String message) {
				Toast.makeText(Lecture2Activity.this, "강의계획서를 가져오는데 실패했습니다.\n" + message, Toast.LENGTH_SHORT).show();
				finish();   // 닫기
			}
		});
		lecture.execute(url);
	}
}
