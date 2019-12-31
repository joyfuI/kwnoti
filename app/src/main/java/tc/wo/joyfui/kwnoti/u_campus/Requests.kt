package tc.wo.joyfui.kwnoti.u_campus

import okhttp3.*

object Requests {
	private val cookieJar by lazy {
		object : CookieJar {
			/*
			강의계획서 부분에서 자꾸 로그인이 풀리는 문제가 있었는데 인코딩 문제로 추정된다.
			info2.kw.ac.kr은 utf-8, info.kw.ac.kr은 euc-kr인 듯...
			로그인페이지/강의계획서는 info.kw.ac.kr이고 나머지는 info2.kw.ac.kr
			일단 알아낸 건 여기까지다. 유캠퍼스 정말 그지같네..
			아무튼 쿠키저장소를 두 개로 나누니까 문제 해결됨. 하아...
			*/
			private var cookies = ArrayList<Cookie>()    // info2.kw.ac.kr(utf-8) 용
			private var cookies2 = ArrayList<Cookie>()    // info.kw.ac.kr(euc-kr) 용

			override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
				val fileName = url.pathSegments[url.pathSize - 1]
				if (fileName == "login_proc.php") {    // 로그인 쿠키
					this.cookies = ArrayList(cookies)
					cookies2 = ArrayList(cookies)
					return
				} else if (fileName == "captcha.php") {    // 캡차 인증 쿠키
					cookies2.addAll(cookies)
					return
				}
				if (url.host == "info.kw.ac.kr") {
					cookies2 = ArrayList(cookies)
				} else {
					this.cookies = ArrayList(cookies)
				}
			}

			override fun loadForRequest(url: HttpUrl): List<Cookie> {
				if (url.host == "info.kw.ac.kr") {
					return cookies2
				} else {
					return cookies
				}
			}
		}
	}

	private val client by lazy {
		OkHttpClient.Builder().cookieJar(cookieJar)
			.connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))	// 최신 TLS로 접속 실패하면 이전 TLS로 접속 시도
			.build()
	}

	fun getInstance(): OkHttpClient {
		return client
	}
}
