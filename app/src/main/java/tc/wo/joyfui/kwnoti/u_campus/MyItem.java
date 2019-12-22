package tc.wo.joyfui.kwnoti.u_campus;

import androidx.annotation.NonNull;

public class MyItem {
	private String name = "";
	private String time = "";

	private String p_subj = "";
	private String p_year = "";
	private String p_subjseq = "";
	private String p_class = "";

	private String p_bdseq = "";

	private String p_ordseq = "";

	public MyItem(String name, String time, String p_subj, String p_year, String p_subjseq, String p_class) {	// 과목 목록
		this.name = name;
		this.time = time;
		this.p_subj = p_subj;
		this.p_year = p_year;
		this.p_subjseq = p_subjseq;
		this.p_class = p_class;
	}

	public MyItem(String name, String time, String p_bdseq) {	// 글 목록
		this.name = name;
		this.time = time;
		this.p_bdseq = p_bdseq;
	}

	public MyItem(String name, String time, String p_ordseq, String p_subj, String p_year, String p_subjseq, String p_class) {	// 과제 목록
		this.name = name;
		this.time = time;
		this.p_ordseq = p_ordseq;
		this.p_subj = p_subj;
		this.p_year = p_year;
		this.p_subjseq = p_subjseq;
		this.p_class = p_class;
	}

	public MyItem(String name, String time) {	// 강의계획서 목록
		this.name = name;	// name
		this.time = time;	// value
	}

	@NonNull
	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}

	public String getP_subj() {
		return p_subj;
	}

	public String getP_year() {
		return p_year;
	}

	public String getP_subjseq() {
		return p_subjseq;
	}

	public String getP_class() {
		return p_class;
	}

	public String getP_bdseq() {
		return p_bdseq;
	}

	public String getP_ordseq() {
		return p_ordseq;
	}
}
