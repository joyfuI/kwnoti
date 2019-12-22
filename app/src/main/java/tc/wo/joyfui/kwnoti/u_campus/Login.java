package tc.wo.joyfui.kwnoti.u_campus;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.*;

public class Login extends AsyncTask<String, Void, Boolean> {
	public interface OnLoginListener {
		void onSuccess();
		void onFailure(String message);
	}

	private OnLoginListener callback = null;
	private String message = "";

	public void setOnLoginListener(OnLoginListener callback) {
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(String... strings) {
		OkHttpClient client = Requests.getInstance();
		Request request;
		RequestBody formBody;
		Response response;

		try {
			request = new Request.Builder()
					.url("https://info.kw.ac.kr/webnote/login/logout.php")	// 로그아웃
					.build();
			client.newCall(request).execute();

			formBody = new FormBody.Builder()
					.add("login_type", "2")
					.add("redirect_url", "https://info.kw.ac.kr/")
					.add("gubun_code", "11")
					.add("member_no", strings[0])
					.add("password", strings[1])
					.add("p_language", "KOREAN")
					.build();
			request = new Request.Builder()
					.url("https://info.kw.ac.kr/webnote/login/login_proc.php")	// 로그인
					.post(formBody)
					.build();
			response = client.newCall(request).execute();
			if (!response.isSuccessful()) {	// 접속 실패
				message = response.message();
				return false;
			}
			String html = new String(response.body().bytes(), "euc-kr");

			return html.contains("location.replace(\"https://info.kw.ac.kr/\");");
		} catch (IOException e) {
			e.printStackTrace();
			message = e.getMessage();
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean aBoolean) {
		if (callback == null) {
			return;
		}
		if (aBoolean) {
			callback.onSuccess();	// 로그인 성공
		} else {
			callback.onFailure(message);	// 로그인 실패
		}
	}
}
