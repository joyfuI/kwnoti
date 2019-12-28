package tc.wo.joyfui.kwnoti

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	lateinit var sharePref: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val point = Point()
		windowManager.defaultDisplay.getSize(point)	// 화면 크기 구하기

		grid_layout.layoutParams.height = point.x * grid_layout.rowCount / grid_layout.columnCount	// 화면 크기에 따라 높이 조절

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
		val intent = Intent()
		when (view) {
			setting -> {	// 설정
				intent.setClass(this, SettingsActivity::class.java)
				startActivity(intent)
			}

			else -> Toast.makeText(this, "미완성된 기능입니다.", Toast.LENGTH_SHORT).show()
		}
	}
}
