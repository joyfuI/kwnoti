package tc.wo.joyfui.kwnoti.u_campus

class MyItem {
	var name = ""
		private set
	var time = ""
		private set

	var p_subj = ""
		private set
	var p_year = ""
		private set
	var p_subjseq = ""
		private set
	var p_class = ""
		private set

	var p_bdseq = ""
		private set

	var p_ordseq = ""
		private set

	constructor(name: String, time: String, p_subj: String, p_year: String, p_subjseq: String, p_class: String) {	// 과목 목록
		this.name = name
		this.time = time
		this.p_subj = p_subj
		this.p_year = p_year
		this.p_subjseq = p_subjseq
		this.p_class = p_class
	}

	constructor(name: String, time: String, p_bdseq: String) {	// 글 목록
		this.name = name
		this.time = time
		this.p_bdseq = p_bdseq
	}

	constructor(name: String, time: String, p_ordseq: String, p_subj: String, p_year: String, p_subjseq: String, p_class: String) {	// 과제 목록
		this.name = name
		this.time = time
		this.p_ordseq = p_ordseq
		this.p_subj = p_subj
		this.p_year = p_year
		this.p_subjseq = p_subjseq
		this.p_class = p_class
	}

	constructor(name: String, time: String) {	// 강의계획서 목록
		this.name = name	// name
		this.time = time	// value
	}

	override fun toString(): String {
		return name
	}
}
