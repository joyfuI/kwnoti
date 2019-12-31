package tc.wo.joyfui.kwnoti

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_ucampus.*
import tc.wo.joyfui.kwnoti.u_campus.Login
import tc.wo.joyfui.kwnoti.u_campus.MyAdapter
import tc.wo.joyfui.kwnoti.u_campus.MyItem
import tc.wo.joyfui.kwnoti.u_campus.Subject

class UcampusActivity : AppCompatActivity(), Subject.OnSubjectListener {
	private lateinit var subjectList: List<MyItem>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_ucampus)

		val sharePref = PreferenceManager.getDefaultSharedPreferences(this)
		val ID = sharePref.getString("id", "")
		val PASSWD = sharePref.getString("passwd", "")

		if (ID.isNullOrEmpty() || PASSWD.isNullOrEmpty()) {	// id 비밀번호가 없으면
			Toast.makeText(this, "개인정보가 설정되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
			finish()
			return
		}

		val login = Login()
		login.onLoginListener = object : Login.OnLoginListener {
			override fun onSuccess() {	// 로그인 성공
				val subject = Subject()
				subject.onSubjectListener = this@UcampusActivity
				subject.execute()	// 과목 가져오기
			}

			override fun onFailure(message: String) {	// 로그인 실패
				Toast.makeText(this@UcampusActivity, "유캠퍼스 로그인에 실패했습니다.\n$message", Toast.LENGTH_SHORT).show()
				finish()	// 닫기
			}
		}
		login.execute(ID, PASSWD)	// 유캠퍼스 로그인
	}

	@Suppress("UNUSED_ANONYMOUS_PARAMETER")
	override fun onSuccess(list: List<MyItem>) {	// 과목 가져오기 성공
		subjectList = list
		val adapter = MyAdapter(this, subjectList)

		list_view.adapter = adapter
		list_view.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, p2, p3 ->	// 클릭 리스너
			registerForContextMenu(list_view)
			openContextMenu(p1)    // 컨텍스트 메뉴 띄우기
			unregisterForContextMenu(list_view)
		}
	}

	override fun onFailure(message: String) {	// 과목 가져오기 실패
		Toast.makeText(this, "수강 과목을 가져오는데 실패했습니다.\n$message", Toast.LENGTH_SHORT).show()
		finish()	// 닫기
	}

	fun onClick(@Suppress("UNUSED_PARAMETER") view: View) {	// 강의계획서 검색
		val intent = Intent(this, LectureActivity::class.java)
		startActivity(intent)
	}

	override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {	// 컨텍스트 메뉴를 띄울 때
		super.onCreateContextMenu(menu, v, menuInfo)

		val inflater = menuInflater
		inflater.inflate(R.menu.ucampus_menu, menu)	// 메뉴 생성
	}

	override fun onContextItemSelected(item: MenuItem): Boolean {	// 컨텍스트 메뉴를 선택했을 때
		val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
		val subject = subjectList[info.position]

		val intent = Intent(this, BoardActivity::class.java)

		intent.putExtra("title", subject.name)
		intent.putExtra("ItemId", item.itemId)
		intent.putExtra("p_subj", subject.p_subj)
		intent.putExtra("p_year", subject.p_year)
		intent.putExtra("p_subjseq", subject.p_subjseq)
		intent.putExtra("p_class", subject.p_class)

		startActivity(intent)
		return super.onContextItemSelected(item)
	}
}
