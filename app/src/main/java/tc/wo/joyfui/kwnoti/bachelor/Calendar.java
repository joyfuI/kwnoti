package tc.wo.joyfui.kwnoti.bachelor;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.*;

public class Calendar extends AsyncTask<Void, Void, Boolean> {
	public interface OnCalendarListener {
		void onSuccess(String content);
		void onFailure(String message);
	}

	private OnCalendarListener callback = null;
	private String content = "";
	private String message = "";

	public void setOnLoginListener(OnCalendarListener callback) {
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(Void... voids) {
		OkHttpClient client = new OkHttpClient.Builder()
				.connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))	// 최신 TLS로 접속 실패하면 이전 TLS로 접속 시도
				.build();
		Request request;
		Response response;

		try {
			request = new Request.Builder()
					.url("https://www.kw.ac.kr/ko/life/bachelor_calendar.do")
					.build();
			response = client.newCall(request).execute();
			if (!response.isSuccessful()) {	// 접속 실패
				message = response.message();
				return false;
			}
			String html = response.body().string();
			Document doc = Jsoup.parse(html);

			content += "<html><head>";
			content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/_res/ko/_css/user.css\">";
			content += "<script src=\"/_common/js/jquery/jquery-1.9.1.js\"></script>";
			content += "<script src=\"/_custom/kw/resource/js/board.bachelor_calendar_view.js\"></script>";
			content += "</head><body>";
			content += "<div class=\"contents-wrap-in\">";
			content += "<div class=\"ko bachelor_calendar\">";
			content += doc.select(".ko > input:nth-child(5)").outerHtml();
			content += doc.select(".ko > textarea:nth-child(6)").outerHtml();
			content += doc.select(".schedule-this-year").outerHtml();
			content += "</div>";
			content += "</div>";
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
			callback.onSuccess(content);	// 학사일정 가져오기 성공
		} else {
			callback.onFailure(message);	// 학사일정 가져오기 실패
		}
	}
}
