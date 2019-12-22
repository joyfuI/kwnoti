package tc.wo.joyfui.kwnoti.u_campus;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.*;

public class Lecture extends AsyncTask<String, Void, Boolean> {
	public interface OnLectureListener {
		void onSuccess(String content);
		void onFailure(String message);
	}

	private OnLectureListener callback = null;
	private String content = "";
	private String message = "";

	public void setOnLectureListener(OnLectureListener callback) {
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(String... strings) {
		OkHttpClient client = Requests.getInstance();
		Request request;
		Response response;

		try {
			request = new Request.Builder()
					.url("https://info.kw.ac.kr/webnote/lecture/captcha.php")
					.build();
			response = client.newCall(request).execute();	// 캡챠를 한번 방문해야 접속 가능

			request = new Request.Builder()
					.url("https://info.kw.ac.kr/webnote/lecture/" + strings[0])
					.build();
			response = client.newCall(request).execute();
			if (!response.isSuccessful()) {	// 접속 실패
				message = response.message();
				return false;
			}
			String html = new String(response.body().bytes(), "euc-kr");
			Document doc = Jsoup.parse(html);

			content += "<html><head>";
			content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/style/style.css\">";
			content += "</head><body>";
			Elements elements = doc.select("form");
			elements.select("table:last-child").remove();	// 목록 버튼 지우기
			content += elements.html();
			content += "</body></html>";
		} catch (IOException e) {
			e.printStackTrace();
			message = e.getMessage();
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean aBoolean) {
		if (callback == null) {
			return;
		}
		if (aBoolean) {
			callback.onSuccess(content);	// 강의계획서 가져오기 성공
		} else {
			callback.onFailure(message);	// 강의계획서 가져오기 실패
		}
	}
}
