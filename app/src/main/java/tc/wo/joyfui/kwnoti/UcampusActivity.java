package tc.wo.joyfui.kwnoti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.List;

import tc.wo.joyfui.kwnoti.u_campus.*;

public class UcampusActivity extends AppCompatActivity implements Subject.OnSubjectListener {
	private List<MyItem> subjectList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ucampus);

		SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(this);
		final String ID = sharePref.getString("id", "");
		final String  PASSWD = sharePref.getString("passwd", "");

		if (ID.isEmpty() || PASSWD.isEmpty()) { // id 비밀번호가 없으면
			Toast.makeText(this, "개인정보가 설정되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		Login login = new Login();
		login.setOnLoginListener(new Login.OnLoginListener() {
			@Override
			public void onSuccess() {   // 로그인 성공
				Subject subject = new Subject();
				subject.setOnSubjectListener(UcampusActivity.this);
				subject.execute();  // 과목 가져오기
			}

			@Override
			public void onFailure(String message) { // 로그인 실패
				Toast.makeText(UcampusActivity.this, "유캠퍼스 로그인에 실패했습니다.\n" + message, Toast.LENGTH_SHORT).show();
				finish();  // 닫기
			}
		});
		login.execute(ID, PASSWD);  // 유캠퍼스 로그인
	}

	@Override
	public void onSuccess(List<MyItem> list) { // 과목 가져오기 성공
		subjectList = list;
		final ListView listView = findViewById(R.id.list);
		MyAdapter adapter = new MyAdapter(this, subjectList);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 클릭 리스너
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				registerForContextMenu(listView);
				openContextMenu(view); // 컨텍스트 메뉴 띄우기
				unregisterForContextMenu(listView);
			}
		});
	}

	@Override
	public void onFailure(String message) { // 과목 가져오기 실패
		Toast.makeText(this, "수강 과목을 가져오는데 실패했습니다.\n" + message, Toast.LENGTH_SHORT).show();
		finish();   // 닫기
	}

	public void onClick(View view) {    // 강의계획서 검색
		Intent intent = new Intent(this, LectureActivity.class);
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {   // 컨텍스트 메뉴를 띄울 때
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ucampus_menu, menu);    // 메뉴 생성
	}

	@Override
	public boolean onContextItemSelected(@NonNull MenuItem item) {  // 컨텍스트 메뉴를 선택했을 때
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		MyItem subject = subjectList.get(info.position);

		Intent intent = new Intent(this, BoardActivity.class);

		intent.putExtra("title", subject.getName());
		intent.putExtra("ItemId", item.getItemId());
		intent.putExtra("p_subj", subject.getP_subj());
		intent.putExtra("p_year", subject.getP_year());
		intent.putExtra("p_subjseq", subject.getP_subjseq());
		intent.putExtra("p_class", subject.getP_class());

		startActivity(intent);
		return super.onContextItemSelected(item);
	}
}
