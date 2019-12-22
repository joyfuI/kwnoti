package tc.wo.joyfui.kwnoti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.List;

import tc.wo.joyfui.kwnoti.u_campus.*;

public class LectureActivity extends AppCompatActivity implements LectureSpinner.OnLectureSpinnerListener {
	private List<MyItem> lectureList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture);

		SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(this);
		final String ID = sharePref.getString("id", "");
		final String  PASSWD = sharePref.getString("passwd", "");

		LectureSpinner lectureSpinner = new LectureSpinner();
		lectureSpinner.setOnLectureSpinnerListener(LectureActivity.this);
		lectureSpinner.execute();	// 강의계획서 조회 정보 가져오기
	}

	@Override
	public void onSuccess(SparseArray<List<MyItem>> map) {	// 강의계획서 조회 정보 가져오기 성공
		Spinner spinner;
		ArrayAdapter<MyItem> arrayAdapter;

		spinner = findViewById(R.id.this_year);
		arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, map.get(R.id.this_year));
		spinner.setAdapter(arrayAdapter);	// 검색년도

		spinner = findViewById(R.id.hakgi);
		arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, map.get(R.id.hakgi));
		spinner.setAdapter(arrayAdapter);	// 검색학기

		spinner = findViewById(R.id.fsel1);
		arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, map.get(R.id.fsel1));
		spinner.setAdapter(arrayAdapter);	// 공통과목
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if (i != 0) {
					((Spinner)findViewById(R.id.fsel2)).setSelection(0);	// 공통과목 선택 시 학과/전공 초기화
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) { }
		});

		spinner = findViewById(R.id.fsel2);
		arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, map.get(R.id.fsel2));
		spinner.setAdapter(arrayAdapter);	// 학과/전공
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if (i != 0) {
					((Spinner)findViewById(R.id.fsel1)).setSelection(0);	// 학과/전공 선택 시 공통과목 초기화
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) { }
		});

		((Button)findViewById(R.id.button)).setEnabled(true);	// 로딩 후 버튼 활성화
	}

	@Override
	public void onFailure(String message) {	// 강의계획서 조회 정보 가져오기 실패
		Toast.makeText(this, "강의계획서 조회 정보를 가져오는 데 실패했습니다.\n" + message, Toast.LENGTH_SHORT).show();
		finish();	// 닫기
	}

	public void onClick(View view) {	// 검색!
		String this_year = ((MyItem)((Spinner)findViewById(R.id.this_year)).getSelectedItem()).getTime();
		String hakgi = ((MyItem)((Spinner)findViewById(R.id.hakgi)).getSelectedItem()).getTime();
		String hh = ((TextView)findViewById(R.id.hh)).getText().toString();
		String prof_name = ((TextView)findViewById(R.id.prof_name)).getText().toString();
		String fsel1 = ((MyItem)((Spinner)findViewById(R.id.fsel1)).getSelectedItem()).getTime();
		String fsel2 = ((MyItem)((Spinner)findViewById(R.id.fsel2)).getSelectedItem()).getTime();

		LectureListView lectureListView = new LectureListView();
		lectureListView.setOnLectureListViewListener(new LectureListView.OnLectureListViewListener() {
			@Override
			public void onSuccess(List<MyItem> list) {	// 강의계획서 목록 가져오기 성공
				lectureList = list;

				if (lectureList.size() == 0) {	// 검색 결과가 없으면
					Toast.makeText(LectureActivity.this, "검색된 Data가 없습니다.", Toast.LENGTH_LONG).show();
					return;
				}

				ListView listView = findViewById(R.id.list);
				ArrayAdapter<MyItem> arrayAdapter = new ArrayAdapter<>(LectureActivity.this, android.R.layout.simple_list_item_1, lectureList);

				listView.setAdapter(arrayAdapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {	// 클릭 리스너
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
						Intent intent = new Intent(LectureActivity.this, Lecture2Activity.class);

						intent.putExtra("URL", lectureList.get(i).getTime());

						startActivity(intent);
					}
				});
			}

			@Override
			public void onFailure(String message) {	// 강의계획서 목록 가져오기 실패
				Toast.makeText(LectureActivity.this, "검색 결과를 가져오는데 실패했습니다.\n" + message, Toast.LENGTH_SHORT).show();
			}
		});
		lectureListView.execute(this_year, hakgi, hh, prof_name, fsel1, fsel2);
	}
}
