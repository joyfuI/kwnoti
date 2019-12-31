package tc.wo.joyfui.kwnoti.u_campus

import android.os.AsyncTask
import okhttp3.Request
import org.jsoup.Jsoup
import java.nio.charset.Charset

class Lecture : AsyncTask<String, Void, Boolean>() {
	interface OnLectureListener {
		fun onSuccess(content: String)
		fun onFailure(message: String)
	}

	var onLectureListener: OnLectureListener? = null
	private var content = ""
	private var message = ""

	override fun doInBackground(vararg p0: String?): Boolean {
		val client = Requests.getInstance()

		var request = Request.Builder()
			.url("https://info.kw.ac.kr/webnote/lecture/captcha.php")
			.build()
		client.newCall(request).execute()	// 캡챠를 한번 방문해야 접속 가능

		request = Request.Builder()
			.url("https://info.kw.ac.kr/webnote/lecture/" + p0[0])
			.build()
		val response = client.newCall(request).execute()
		if (!response.isSuccessful) {	// 접속 실패
			message = response.message
			return false
		}
		val html = String(response.body!!.bytes(), Charset.forName("euc-kr"))
		val doc = Jsoup.parse(html)

		content += "<html><head>"
		content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/style/style.css\">"
		content += "</head><body>"
		val elements = doc.select("form")
		elements.select("table:last-child").remove()	// 목록 버튼 지우기
		content += elements.html()
		content += "</body></html>"
		return true
	}

	override fun onPostExecute(result: Boolean?) {
		onLectureListener?.let {
			if (result!!) {
				it.onSuccess(content)	// 강의계획서 가져오기 성공
			} else {
				it.onFailure(message)	// 강의계획서 가져오기 실패
			}
		}
	}
}
