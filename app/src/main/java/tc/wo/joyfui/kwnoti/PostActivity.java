package tc.wo.joyfui.kwnoti;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tc.wo.joyfui.kwnoti.u_campus.Post;
import tc.wo.joyfui.kwnoti.u_campus.ass_pds.AssPdsPost;
import tc.wo.joyfui.kwnoti.u_campus.notice.NoticePost;
import tc.wo.joyfui.kwnoti.u_campus.report.ReportPost;

public class PostActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);

		Intent intent = getIntent();
		String p_bdseq = intent.getStringExtra("p_bdseq");
		String p_ordseq = intent.getStringExtra("p_ordseq");
		String p_subj = intent.getStringExtra("p_subj");
		String p_year = intent.getStringExtra("p_year");
		String p_subjseq = intent.getStringExtra("p_subjseq");
		String p_class = intent.getStringExtra("p_class");

		Post post = new Post();
		post.setOnPostListener(new Post.OnPostListener() {
			@Override
			public void onSuccess(String title, String content) {	// 글 가져오기 성공
				WebView webView = findViewById(R.id.web);

				((TextView)findViewById(R.id.post_title)).setText(title);
				webView.loadDataWithBaseURL("https://info2.kw.ac.kr/", content, "text/html", "UTF-8", null);	// 웹뷰 출력 (상대주소 대응)
			}

			@Override
			public void onFailure(String message) {	// 글 가져오기 실패
				Toast.makeText(PostActivity.this, "글을 가져오는데 실패했습니다.\n" + message, Toast.LENGTH_SHORT).show();
				finish();   // 닫기
			}
		});

		switch (intent.getIntExtra("ItemId", 0)) {
			case R.id.notice:	// 공지사항
				post.setPostStrategy(new NoticePost());
				break;

			case R.id.ass_pds:	// 강의자료실
				post.setPostStrategy(new AssPdsPost());
				break;

			case R.id.report:	// 과제게시판
				post.setPostStrategy(new ReportPost());
				break;
		}
		post.execute(p_bdseq, p_ordseq, p_subj, p_year, p_subjseq, p_class);
	}
}
