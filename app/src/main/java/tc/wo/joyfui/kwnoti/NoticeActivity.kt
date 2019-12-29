package tc.wo.joyfui.kwnoti

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_notice.*

class NoticeActivity : AppCompatActivity() {
	val url = "https://www.kw.ac.kr/ko/life/notice.do"

	@SuppressLint("SetJavaScriptEnabled")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_notice)

		web.webViewClient = WebViewClient()	// 링크 클릭 시 새창 안뜨게
		web.settings.javaScriptEnabled = true	// 자바스크립트 활성화
		web.loadUrl(url)	// 공지사항 페이지 접속
	}

	override fun onBackPressed() {
		super.onBackPressed()
	}

	fun isMain(): Boolean {	// 메인 페이지인지 확인하는 함수
		val url = web.originalUrl
		if (url.contains(this.url)) {
			if (url.contains("articleNo")) {
				return false
			}
			if (url.contains("article.offset") && !url.contains("article.offset=0")) {
				return false
			}
			return true
		}
		return false
	}

	fun onClick(view: View) {
		when (view) {
			close -> finish()	// 닫기

			open -> {	// 브라우저에서 열기
				val intent = Intent()
				intent.action = Intent.ACTION_VIEW
				intent.data = Uri.parse(web.originalUrl)
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				startActivity(intent)
			}
		}
	}
}
