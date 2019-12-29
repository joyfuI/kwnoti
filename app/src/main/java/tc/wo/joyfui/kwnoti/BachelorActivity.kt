package tc.wo.joyfui.kwnoti

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bachelor.*
import tc.wo.joyfui.kwnoti.bachelor.Calendar

class BachelorActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_bachelor)

		val calendar = Calendar()
		calendar.setOnLoginListener(object : Calendar.OnCalendarListener {
			@SuppressLint("SetJavaScriptEnabled")
			override fun onSuccess(content: String) {	// 가져오기 성공
				web.settings.javaScriptEnabled = true	// 자바스크립트 활성화
				web.loadDataWithBaseURL("https://www.kw.ac.kr/", content, "text/html", "UTF-8", null)	// 웹뷰 출력 (상대주소 대응)
			}

			override fun onFailure(message: String) {	// 가져오기 실패
				Toast.makeText(this@BachelorActivity, "학사일정을 가져오는데 실패했습니다.\n$message", Toast.LENGTH_SHORT).show()
				finish()	// 닫기
			}
		})
		calendar.execute()	// 학사일정 가져오기
	}
}
