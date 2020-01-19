package tc.wo.joyfui.kwnoti

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_lecture.*
import tc.wo.joyfui.kwnoti.u_campus.LectureListView
import tc.wo.joyfui.kwnoti.u_campus.LectureSpinner
import tc.wo.joyfui.kwnoti.u_campus.MyItem

class LectureActivity : AppCompatActivity(), LectureSpinner.OnLectureSpinnerListener {
	private lateinit var lectureList: List<MyItem>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_lecture)

		val lectureSpinner = LectureSpinner()
		lectureSpinner.onLectureSpinnerListener = this@LectureActivity
		lectureSpinner.execute()	// 강의계획서 조회 정보 가져오기
	}

	override fun onSuccess(map: SparseArray<List<MyItem>>) {	// 강의계획서 조회 정보 가져오기 성공
		var arrayAdapter: ArrayAdapter<MyItem>

		arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, map[R.id.this_year])
		this_year.adapter = arrayAdapter	// 검색년도

		arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, map[R.id.hakgi])
		hakgi.adapter = arrayAdapter	// 검색학기

		arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, map[R.id.fsel1])
		fsel1.adapter = arrayAdapter	// 공통과목
		fsel1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
				if (p2 != 0) {
					fsel2.setSelection(0)	// 공통과목 선택 시 학과/전공 초기화
				}
			}

			override fun onNothingSelected(p0: AdapterView<*>?) { }
		}

		arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, map[R.id.fsel2])
		fsel2.adapter = arrayAdapter	// 학과/전공
		fsel2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
				if (p2 != 0) {
					fsel1.setSelection(0)	// 학과/전공 선택 시 공통과목 초기화
				}
			}

			override fun onNothingSelected(p0: AdapterView<*>?) { }
		}

		button.isEnabled = true	// 로딩 후 버튼 활성화
	}

	override fun onFailure(message: String) {	// 강의계획서 조회 정보 가져오기 실패
		Toast.makeText(this, "강의계획서 조회 정보를 가져오는 데 실패했습니다.\n$message", Toast.LENGTH_SHORT).show()
		finish()	// 닫기
	}

	fun onClick(@Suppress("UNUSED_PARAMETER") view: View) {	// 검색!
		val this_year = (this_year.selectedItem as MyItem).time
		val hakgi = (hakgi.selectedItem as MyItem).time
		val hh = hh.text.toString()
		val prof_name = prof_name.text.toString()
		val fsel1 = (fsel1.selectedItem as MyItem).time
		val fsel2 = (fsel2.selectedItem as MyItem).time

		val lectureListView = LectureListView()
		lectureListView.onLectureListViewListener = object : LectureListView.OnLectureListViewListener {
			@Suppress("UNUSED_ANONYMOUS_PARAMETER")
			override fun onSuccess(list: List<MyItem>) {	// 강의계획서 목록 가져오기 성공
				lectureList = list

				if (lectureList.isEmpty()) {	// 검색 결과가 없으면
					Toast.makeText(this@LectureActivity, "검색된 Data가 없습니다.", Toast.LENGTH_LONG).show()
					return
				}

				val arrayAdapter = ArrayAdapter(this@LectureActivity, android.R.layout.simple_list_item_1, lectureList)

				list_view.adapter = arrayAdapter
				list_view.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, p2, p3 ->	// 클릭 리스너
					val intent = Intent(this@LectureActivity, Lecture2Activity::class.java)

					intent.putExtra("URL", lectureList[p2].time)

					startActivity(intent)
				}
			}

			override fun onFailure(message: String) {	// 강의계획서 목록 가져오기 실패
				Toast.makeText(this@LectureActivity, "검색 결과를 가져오는데 실패했습니다.\n$message", Toast.LENGTH_SHORT).show()
			}
		}
		lectureListView.execute(this_year, hakgi, hh, prof_name, fsel1, fsel2)
	}
}
