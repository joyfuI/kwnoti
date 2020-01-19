package tc.wo.joyfui.kwnoti

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_post.*
import tc.wo.joyfui.kwnoti.u_campus.Post
import tc.wo.joyfui.kwnoti.u_campus.ass_pds.AssPdsPost
import tc.wo.joyfui.kwnoti.u_campus.notice.NoticePost
import tc.wo.joyfui.kwnoti.u_campus.report.ReportPost

class PostActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_post)

		val intent = intent
		val p_bdseq = intent.getStringExtra("p_bdseq")
		val p_ordseq = intent.getStringExtra("p_ordseq")
		val p_subj = intent.getStringExtra("p_subj")
		val p_year = intent.getStringExtra("p_year")
		val p_subjseq = intent.getStringExtra("p_subjseq")
		val p_class = intent.getStringExtra("p_class")

		val post = Post()
		post.onPostListener = object : Post.OnPostListener {
			override fun onSuccess(title: String, content: String) {	// 글 가져오기 성공
				post_title.text = title
				web.loadDataWithBaseURL("https://info2.kw.ac.kr/", content, "text/html", "UTF-8", null)	// 웹뷰 출력 (상대주소 대응)
			}

			override fun onFailure(message: String) {	// 글 가져오기 실패
				Toast.makeText(this@PostActivity, "글을 가져오는데 실패했습니다.\n$message", Toast.LENGTH_SHORT).show()
				finish()	// 닫기
			}
		}

		when (intent.getStringExtra("ItemId")) {
			"공지사항" -> post.postStrategy = NoticePost()
			"강의자료실" -> post.postStrategy = AssPdsPost()
			"과제게시판" -> post.postStrategy = ReportPost()
		}
		post.execute(p_bdseq, p_ordseq, p_subj, p_year, p_subjseq, p_class)
	}
}
