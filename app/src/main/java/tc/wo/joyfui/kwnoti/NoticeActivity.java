package tc.wo.joyfui.kwnoti;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class NoticeActivity extends AppCompatActivity {
	private String url = "https://www.kw.ac.kr/ko/life/notice.do";
	private WebView webView = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);

		webView = findViewById(R.id.web);
		webView.setWebViewClient(new WebViewClient());  // 링크 클릭 시 새창 안뜨게
		webView.getSettings().setJavaScriptEnabled(true);   // 자바스크립트 활성화
		webView.loadUrl(url);   // 공지사항 페이지 접속
	}

	@Override
	public void onBackPressed() {
		if (isMain()) { // 메인 페이지면
			super.onBackPressed();  // 액티비티 뒤로가기
		} else if (webView.canGoBack()) {    // 페이지 히스토리가 남아 있으면
			webView.goBack();   // 페이지 뒤로가기
		} else {
			super.onBackPressed();  // 액티비티 뒤로가기
		}
	}

	private boolean isMain() {  // 메인 페이지인지 확인하는 함수
		String url = webView.getOriginalUrl();
		if (url.contains(this.url)) {
			if (url.contains("articleNo")) {
				return false;
			}
			if (url.contains("article.offset") && !url.contains("article.offset=0")) {
				return false;
			}
			return true;
		}
		return false;
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.close:    // 닫기
				finish();
				break;

			case R.id.open: // 브라우저에서 열기
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(webView.getOriginalUrl()));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				break;
		}
	}
}
