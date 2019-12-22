package tc.wo.joyfui.kwnoti;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tc.wo.joyfui.kwnoti.bachelor.Calendar;

public class BachelorActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bachelor);

		Calendar calendar = new Calendar();
		calendar.setOnLoginListener(new Calendar.OnCalendarListener() {
			@SuppressLint("SetJavaScriptEnabled")
			@Override
			public void onSuccess(String content) {	// 가져오기 성공
				WebView webView = findViewById(R.id.web);

				webView.getSettings().setJavaScriptEnabled(true);	// 자바스크립트 활성화
				webView.loadDataWithBaseURL("https://www.kw.ac.kr/", content, "text/html", "UTF-8", null);	// 웹뷰 출력 (상대주소 대응)
			}

			@Override
			public void onFailure(String message) {	// 가져오기 실패
				Toast.makeText(BachelorActivity.this, "학사일정을 가져오는데 실패했습니다.\n" + message, Toast.LENGTH_SHORT).show();
				finish();	// 닫기
			}
		});
		calendar.execute();	// 학사일정 가져오기
	}
}
