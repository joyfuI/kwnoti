package tc.wo.joyfui.kwnoti.u_campus;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import okhttp3.*;

public class Post extends AsyncTask<String, Void, Boolean> {
	public interface OnPostListener {
		void onSuccess(String title, String content);
		void onFailure(String message);
	}

	private OnPostListener callback = null;
	private PostStrategy postStrategy = null;
	private String title = "";
	private String content = "";
	private String message = "";

	public void setOnPostListener(OnPostListener callback) {
		this.callback = callback;
	}

	public void setPostStrategy(PostStrategy postStrategy) {
		this.postStrategy = postStrategy;
	}

	@Override
	protected Boolean doInBackground(String... strings) {
		OkHttpClient client = Requests.getInstance();
		Request request;
		RequestBody formBody;
		Response response;

		try {
			boolean insertPage = strings[1].contains(".");
			formBody = new FormBody.Builder()
					.add("p_bdseq", strings[0])
					.add("p_ordseq", strings[1])
					.add("p_grcode", "N000003")
					.add("p_subj", strings[2])
					.add("p_year", strings[3])
					.add("p_subjseq", strings[4])
					.add("p_class", strings[5])
					.build();
			request = new Request.Builder()
					.url("https://info2.kw.ac.kr/servlet/controller.learn." + postStrategy.getServlet() + "?p_process=" + ((insertPage) ? "insertPage" : "view"))
					.post(formBody)
					.build();
			response = client.newCall(request).execute();
			if (!response.isSuccessful()) {	// 접속 실패
				message = response.message();
				return false;
			}
			String html = response.body().string();
			Document doc = Jsoup.parse(html);

			title = postStrategy.getTitle(doc);	// 제목 가져오기
			content = postStrategy.getContent(doc);	// 내용 가져오기
		} catch (IOException e) {
			e.printStackTrace();
			message = e.getMessage();
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean aBoolean) {
		if (callback == null || postStrategy == null) {
			return;
		}
		if (aBoolean) {
			callback.onSuccess(title, content);	// 글 가져오기 성공
		} else {
			callback.onFailure(message);	// 글 가져오기 실패
		}
	}
}
