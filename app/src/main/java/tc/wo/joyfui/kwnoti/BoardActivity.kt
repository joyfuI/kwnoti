package tc.wo.joyfui.kwnoti

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_board.*
import tc.wo.joyfui.kwnoti.u_campus.Board
import tc.wo.joyfui.kwnoti.u_campus.MyAdapter
import tc.wo.joyfui.kwnoti.u_campus.MyItem
import tc.wo.joyfui.kwnoti.u_campus.MyItemDecoration
import tc.wo.joyfui.kwnoti.u_campus.ass_pds.AssPdsList
import tc.wo.joyfui.kwnoti.u_campus.notice.NoticeList
import tc.wo.joyfui.kwnoti.u_campus.report.ReportList

class BoardActivity : AppCompatActivity(), MyAdapter.OnClickListener {
	private lateinit var postList: List<MyItem>
	private lateinit var itemId: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_board)

		val intent = intent
		itemId = intent.getStringExtra("ItemId")!!
		val p_subj = intent.getStringExtra("p_subj")
		val p_year = intent.getStringExtra("p_year")
		val p_subjseq = intent.getStringExtra("p_subjseq")
		val p_class = intent.getStringExtra("p_class")

		subject_title.text = intent.getStringExtra("title")

		val board = Board()
		board.onBoardListener = object : Board.OnBoardListener {
			override fun onSuccess(list: List<MyItem>) {	// 글목록 가져오기 성공
				postList = list

				if (postList.isEmpty()) {	// 글이 없으면
					Toast.makeText(this@BoardActivity, "글이 없습니다.", Toast.LENGTH_LONG).show()
					return
				}

				val adapter = MyAdapter(postList)
				adapter.onClickListener = this@BoardActivity

				list_view.layoutManager = LinearLayoutManager(this@BoardActivity)
				list_view.adapter = adapter
				list_view.addItemDecoration(MyItemDecoration())
			}

			override fun onFailure(message: String) {	// 글목록 가져오기 실패
				Toast.makeText(this@BoardActivity, "글 목록을 가져오는데 실패했습니다.\n$message", Toast.LENGTH_SHORT).show()
				finish()	// 닫기

			}
		}

		when (itemId) {
			"공지사항" -> board.listStrategy = NoticeList()
			"강의자료실" -> board.listStrategy = AssPdsList()
			"과제게시판" -> board.listStrategy = ReportList()
		}
		board.execute(p_subj, p_year, p_subjseq, p_class)
	}

	override fun onClick(view: View, position: Int) {	// 리스트 클릭
		val post = postList[position]

		val intent = Intent(this, PostActivity::class.java)

		intent.putExtra("ItemId", itemId)
		intent.putExtra("p_bdseq", post.p_bdseq)
		intent.putExtra("p_ordseq", post.p_ordseq)
		intent.putExtra("p_subj", post.p_subj)
		intent.putExtra("p_year", post.p_year)
		intent.putExtra("p_subjseq", post.p_subjseq)
		intent.putExtra("p_class", post.p_class)

		startActivity(intent)
	}
}
