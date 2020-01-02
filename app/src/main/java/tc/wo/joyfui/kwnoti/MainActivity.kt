package tc.wo.joyfui.kwnoti

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	private lateinit var sharePref: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		sharePref = PreferenceManager.getDefaultSharedPreferences(this)
		val welcome = sharePref.getBoolean("welcome", true)	// 첫 실행 여부 가져오기
		if (welcome) {	// 첫 실행이면
			val intent = Intent(this, WelcomeActivity::class.java)
			startActivity(intent)
		}
	}

	override fun onRestart() {
		super.onRestart()

		val welcome = sharePref.getBoolean("welcome", true)	// 첫 실행 여부 가져오기
		if (welcome) {	// 만약 웰컴 액티비티를 완료하지 않고 그냥 닫았다면
			finish()
		}
	}

	fun onClick(view: View) {
		var intent = Intent()
		when (view) {
			student_id -> {	// 모바일학생증
				intent = packageManager.getLaunchIntentForPackage("idoit.slpck.kwangwoon") ?: Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=idoit.slpck.kwangwoon"))	// 중앙도서관 앱이 설치되어 있지 않으면 플레이스토어 실행
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				startActivity(intent)
				Toast.makeText(this, "모바일학생증 기능은 중앙도서관 앱을 이용해주세요.", Toast.LENGTH_SHORT).show()
			}

			u_campus -> {	// U-CAMPUS
				intent.setClass(this, UcampusActivity::class.java)
				startActivity(intent)
			}

			notice -> {	// 공지사항
				intent.setClass(this, NoticeActivity::class.java)
				startActivity(intent)
			}

			bachelor -> {	// 학사일정
				intent.setClass(this, BachelorActivity::class.java)
				startActivity(intent)
			}

			setting -> {	// 설정
				intent.setClass(this, SettingsActivity::class.java)
				startActivity(intent)
			}

			else -> Toast.makeText(this, "미완성된 기능입니다.", Toast.LENGTH_SHORT).show()
		}
	}
}
