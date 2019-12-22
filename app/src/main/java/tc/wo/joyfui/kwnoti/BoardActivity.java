package tc.wo.joyfui.kwnoti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import tc.wo.joyfui.kwnoti.u_campus.Board;
import tc.wo.joyfui.kwnoti.u_campus.MyAdapter;
import tc.wo.joyfui.kwnoti.u_campus.MyItem;
import tc.wo.joyfui.kwnoti.u_campus.ass_pds.AssPdsList;
import tc.wo.joyfui.kwnoti.u_campus.notice.NoticeList;
import tc.wo.joyfui.kwnoti.u_campus.report.ReportList;

public class BoardActivity extends AppCompatActivity {
	private List<MyItem> postList = null;
	private int itemId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);

		Intent intent = getIntent();
		itemId = intent.getIntExtra("ItemId", 0);
		String p_subj = intent.getStringExtra("p_subj");
		String p_year = intent.getStringExtra("p_year");
		String p_subjseq = intent.getStringExtra("p_subjseq");
		String p_class = intent.getStringExtra("p_class");

		((TextView)findViewById(R.id.subject_title)).setText(intent.getStringExtra("title"));

		Board board = new Board();
		board.setOnBoardListener(new Board.OnBoardListener() {
			@Override
			public void onSuccess(List<MyItem> list) {	// 글목록 가져오기 성공
				postList = list;

				if (postList.size() == 0) {	// 글이 없으면
					Toast.makeText(BoardActivity.this, "글이 없습니다.", Toast.LENGTH_LONG).show();
					return;
				}

				ListView listView = findViewById(R.id.list);
				MyAdapter adapter = new MyAdapter(BoardActivity.this, postList);

				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {	// 클릭 리스너
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
						MyItem post = postList.get(i);

						Intent intent = new Intent(BoardActivity.this, PostActivity.class);

						intent.putExtra("ItemId", itemId);
						intent.putExtra("p_bdseq", post.getP_bdseq());
						intent.putExtra("p_ordseq", post.getP_ordseq());
						intent.putExtra("p_subj", post.getP_subj());
						intent.putExtra("p_year", post.getP_year());
						intent.putExtra("p_subjseq", post.getP_subjseq());
						intent.putExtra("p_class", post.getP_class());

						startActivity(intent);
					}
				});
			}

			@Override
			public void onFailure(String message) {	// 글목록 가져오기 실패
				Toast.makeText(BoardActivity.this, "글 목록을 가져오는데 실패했습니다.\n" + message, Toast.LENGTH_SHORT).show();
				finish();	// 닫기
			}
		});

		switch (itemId) {
			case R.id.notice:   // 공지사항
				board.setListStrategy(new NoticeList());
				break;

			case R.id.ass_pds:  // 강의자료실
				board.setListStrategy(new AssPdsList());
				break;

			case R.id.report:   // 과제게시판
				board.setListStrategy(new ReportList());
				break;
		}
		board.execute(p_subj, p_year, p_subjseq, p_class);
	}
}
