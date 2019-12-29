package tc.wo.joyfui.kwnoti.bachelor

import android.os.AsyncTask
import okhttp3.*
import org.jsoup.Jsoup
import java.util.*

class Calendar : AsyncTask<Void, Void, Boolean>() {
	interface OnCalendarListener {
		fun onSuccess(content: String)
		fun onFailure(message: String)
	}

	var callback: OnCalendarListener? = null
	var content = ""
	var message = ""

	fun setOnLoginListener(callback: OnCalendarListener) {
		this.callback = callback
	}

	override fun doInBackground(vararg p0: Void?): Boolean {
		val client = OkHttpClient.Builder()
			.connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))	// 최신 TLS로 접속 실패하면 이전 TLS로 접속 시도
			.build()
		val request: Request
		val response: Response

		request = Request.Builder()
			.url("https://www.kw.ac.kr/ko/life/bachelor_calendar.do")
			.build()
		response = client.newCall(request).execute()
		if (!response.isSuccessful) {	// 접속 실패
			message = response.message
			return false
		}
		val html = response.body!!.string()
		val doc = Jsoup.parse(html)

		content += "<html><head>"
		content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/_res/ko/_css/user.css\">"
		content += "<script src=\"/_common/js/jquery/jquery-1.9.1.js\"></script>"
		content += "<script src=\"/_custom/kw/resource/js/board.bachelor_calendar_view.js\"></script>"
		content += "</head><body>"
		content += "<div class=\"contents-wrap-in\">"
		content += "<div class=\"ko bachelor_calendar\">"
		content += doc.select(".ko > input:nth-child(5)").outerHtml()
		content += doc.select(".ko > textarea:nth-child(6)").outerHtml()
		content += doc.select(".schedule-this-year").outerHtml()
		content += "</div>"
		content += "</div>"
		content += "</body></html>"
		return true
	}

	override fun onPostExecute(result: Boolean?) {
		callback?.let {
			if (result!!) {
				it.onSuccess(content)	// 학사일정 가져오기 성공
			} else {
				it.onFailure(message)	// 학사일정 가져오기 실패
			}
		}
	}
}
