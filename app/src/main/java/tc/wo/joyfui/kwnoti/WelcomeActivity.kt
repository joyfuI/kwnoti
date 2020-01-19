package tc.wo.joyfui.kwnoti

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
	private lateinit var sharePref: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_welcome)

		sharePref = PreferenceManager.getDefaultSharedPreferences(this)
	}

	fun onClick(@Suppress("UNUSED_PARAMETER") view: View) {
		val editer = sharePref.edit()
		editer.putString("id", id.text.toString())
		editer.putString("passwd", passwd.text.toString())
		editer.putBoolean("welcome", false)
		editer.apply()	// 설정 저장
		finish()
	}
}
