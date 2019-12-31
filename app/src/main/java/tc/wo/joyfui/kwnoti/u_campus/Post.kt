package tc.wo.joyfui.kwnoti.u_campus

import android.os.AsyncTask
import okhttp3.FormBody
import okhttp3.Request
import org.jsoup.Jsoup

class Post : AsyncTask<String, Void, Boolean>() {
	interface OnPostListener {
		fun onSuccess(title: String?, content: String?)
		fun onFailure(message: String?)
	}

	var onPostListener: OnPostListener? = null
	lateinit var postStrategy: PostStrategy
	private var title = ""
	private var content = ""
	private var message = ""

	override fun doInBackground(vararg p0: String?): Boolean {
		val client = Requests.getInstance()

		val insertPage = p0[1]!!.contains(".")
		val formBody = FormBody.Builder()
			.add("p_bdseq", p0[0]!!)
			.add("p_ordseq", p0[1]!!)
			.add("p_grcode", "N000003")
			.add("p_subj", p0[2]!!)
			.add("p_year", p0[3]!!)
			.add("p_subjseq", p0[4]!!)
			.add("p_class", p0[5]!!)
			.build()
		val request = Request.Builder()
			.url("https://info2.kw.ac.kr/servlet/controller.learn." + postStrategy.getServlet() + "?p_process=" + (if (insertPage) "insertPage" else "view"))
			.post(formBody)
			.build()
		val response = client.newCall(request).execute()
		if (!response.isSuccessful) {	// 접속 실패
			message = response.message
			return false
		}
		val html = response.body!!.string()
		val doc = Jsoup.parse(html)

		title = postStrategy.getTitle(doc)	// 제목 가져오기
		content = postStrategy.getContent(doc)	// 내용 가져오기
		return true
	}

	override fun onPostExecute(result: Boolean?) {
		onPostListener?.let {
			if (result!!) {
				it.onSuccess(title, content)	// 글 가져오기 성공
			} else {
				it.onFailure(message)	// 글 가져오기 실패
			}
		}
	}
}
