package tc.wo.joyfui.kwnoti.u_campus

import android.os.AsyncTask
import okhttp3.FormBody
import okhttp3.Request
import java.nio.charset.Charset

class Login : AsyncTask<String, Void, Boolean>() {
	interface OnLoginListener {
		fun onSuccess()
		fun onFailure(message: String)
	}

	var onLoginListener: OnLoginListener? = null
	private var message = ""

	override fun doInBackground(vararg p0: String?): Boolean {
		val client = Requests.getInstance()

		var request = Request.Builder()
			.url("https://info.kw.ac.kr/webnote/login/logout.php")	// 로그아웃
			.build()
		client.newCall(request).execute()

		val formBody = FormBody.Builder()
			.add("login_type", "2")
			.add("redirect_url", "https://info.kw.ac.kr/")
			.add("gubun_code", "11")
			.add("member_no", p0[0]!!)
			.add("password", p0[1]!!)
			.add("p_language", "KOREAN")
			.build()
		request = Request.Builder()
			.url("https://info.kw.ac.kr/webnote/login/login_proc.php")	// 로그인
			.post(formBody)
			.build()
		val response = client.newCall(request).execute()
		if (!response.isSuccessful) {	// 접속 실패
			message = response.message
			return false
		}
		val html = String(response.body!!.bytes(), Charset.forName("euc-kr"))

		return html.contains("location.replace(\"https://info.kw.ac.kr/\");")
	}

	override fun onPostExecute(result: Boolean?) {
		onLoginListener?.let {
			if (result!!) {
				it.onSuccess()	// 로그인 성공
			} else {
				it.onFailure(message)	// 로그인 실패
			}
		}
	}
}
