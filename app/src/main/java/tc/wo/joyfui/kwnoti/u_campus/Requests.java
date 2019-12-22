package tc.wo.joyfui.kwnoti.u_campus;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.*;

public class Requests {
	private static CookieJar cookieJar = new CookieJar() {
		/*
		강의계획서 부분에서 자꾸 로그인이 풀리는 문제가 있었는데 인코딩 문제로 추정된다.
		info2.kw.ac.kr은 utf-8, info.kw.ac.kr은 euc-kr인 듯...
		로그인페이지/강의계획서는 info.kw.ac.kr이고 나머지는 info2.kw.ac.kr
		일단 알아낸 건 여기까지다. 유캠퍼스 정말 그지같네..
		아무튼 쿠키저장소를 두 개로 나누니까 문제 해결됨. 하아...
		*/
		private List<Cookie> cookies = new ArrayList<>();	// info2.kw.ac.kr(utf-8) 용
		private List<Cookie> cookies2 = new ArrayList<>();	// info.kw.ac.kr(euc-kr) 용

		@Override
		public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
			String fileName = httpUrl.pathSegments().get(httpUrl.pathSize() - 1);
			if (fileName.equals("login_proc.php")) {	// 로그인 쿠키
				cookies = cookies2 = list;
				return;
			} else if (fileName.equals("captcha.php")) {	// 캡차 인증 쿠키
				cookies2 = new ArrayList<>(cookies2);
				cookies2.addAll(list);
				return;
			}
			if (httpUrl.host().equals("info.kw.ac.kr")) {
				cookies2 = list;
			} else {
				cookies = list;
			}
		}

		@NotNull
		@Override
		public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
			if (httpUrl.host().equals("info.kw.ac.kr")) {
				return cookies2;
			} else {
				return cookies;
			}
		}
	};

	private static final OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar)
			.connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))	// 최신 TLS로 접속 실패하면 이전 TLS로 접속 시도
			.build();

	public static OkHttpClient getInstance() {
		return client;
	}
}
