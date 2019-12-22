package tc.wo.joyfui.kwnoti;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.settings, new SettingsFragment())
				.commit();
	}

	public static class SettingsFragment extends PreferenceFragmentCompat {
		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			setPreferencesFromResource(R.xml.root_preferences, rootKey);
			EditTextPreference preference;

			preference = findPreference("id");
			if (preference != null) {
				preference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
					@Override
					public void onBindEditText(@NonNull EditText editText) {
						editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);	// android:inputType="number"
					}
				});
			}

			preference = findPreference("passwd");
			if (preference != null) {
				preference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
					@Override
					public void onBindEditText(@NonNull EditText editText) {
						editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);	// android:inputType="textPassword"
					}
				});
			}
		}
	}
}
