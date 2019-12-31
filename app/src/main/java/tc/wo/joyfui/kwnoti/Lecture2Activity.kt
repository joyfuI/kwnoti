package tc.wo.joyfui.kwnoti

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_lecture2.*
import tc.wo.joyfui.kwnoti.u_campus.Lecture

class Lecture2Activity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_lecture2)

		val intent = intent
		val url = intent.getStringExtra("URL")

		val lecture = Lecture()
		lecture.onLectureListener = object : Lecture.OnLectureListener {
			override fun onSuccess(content: String) {
				web.loadDataWithBaseURL("https://info.kw.ac.kr/", content, "text/html", "EUC-KR", null)	// 웹뷰 출력 (상대주소 대응)
			}

			override fun onFailure(message: String) {
				Toast.makeText(this@Lecture2Activity, "강의계획서를 가져오는데 실패했습니다.\n$message", Toast.LENGTH_SHORT).show()
				finish()	// 닫기
			}
		}
		lecture.execute(url)
	}
}
