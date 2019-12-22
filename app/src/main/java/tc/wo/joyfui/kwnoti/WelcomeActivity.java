package tc.wo.joyfui.kwnoti;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class WelcomeActivity extends AppCompatActivity {
	private SharedPreferences sharePref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		sharePref = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public void onClick(View view) {
		SharedPreferences.Editor editor = sharePref.edit();
		editor.putString("id", ((TextView)findViewById(R.id.id)).getText().toString());
		editor.putString("passwd", ((TextView)findViewById(R.id.passwd)).getText().toString());
		editor.putBoolean("welcome", false);
		editor.apply();	// 설정 저장
		finish();
	}
}
