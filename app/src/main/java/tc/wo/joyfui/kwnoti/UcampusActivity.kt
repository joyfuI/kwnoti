package tc.wo.joyfui.kwnoti

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_ucampus.*
import tc.wo.joyfui.kwnoti.u_campus.*

class UcampusActivity : AppCompatActivity(), Subject.OnSubjectListener, DialogInterface.OnClickListener {
	private lateinit var subjectList: List<MyItem>
	private var position = -1

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

	override fun onSuccess(list: List<MyItem>) {	// 과목 가져오기 성공
		subjectList = list

		val adapter = MyAdapter(subjectList)
		adapter.onClickListener = object : MyAdapter.OnClickListener {
			override fun onClick(view: View, position: Int) {
				this@UcampusActivity.position = position
				AlertDialog.Builder(view.context)
					.setItems(R.array.ucampus_menu, this@UcampusActivity)
					.show()
			}
		}

		list_view.layoutManager = LinearLayoutManager(this)
		list_view.adapter = adapter
		list_view.addItemDecoration(MyItemDecoration())
	}

	override fun onFailure(message: String) {	// 과목 가져오기 실패
		Toast.makeText(this, "수강 과목을 가져오는데 실패했습니다.\n$message", Toast.LENGTH_SHORT).show()
		finish()	// 닫기
	}

	fun onClick(@Suppress("UNUSED_PARAMETER") view: View) {	// 강의계획서 검색
		val intent = Intent(this, LectureActivity::class.java)
		startActivity(intent)
	}

	override fun onClick(p0: DialogInterface?, p1: Int) {	// AlertDialog 클릭 리스너
		val arr = resources.getStringArray(R.array.ucampus_menu)
		val subject = subjectList[position]

		val intent = Intent(this, BoardActivity::class.java)

		intent.putExtra("title", subject.name)
		intent.putExtra("ItemId", arr[p1])
		intent.putExtra("p_subj", subject.p_subj)
		intent.putExtra("p_year", subject.p_year)
		intent.putExtra("p_subjseq", subject.p_subjseq)
		intent.putExtra("p_class", subject.p_class)

		startActivity(intent)
	}
}
