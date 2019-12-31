package tc.wo.joyfui.kwnoti.u_campus.report

import org.jsoup.nodes.Document
import tc.wo.joyfui.kwnoti.u_campus.ListStrategy
import tc.wo.joyfui.kwnoti.u_campus.MyItem
import java.util.regex.Pattern

class ReportList : ListStrategy {
	override fun getList(doc: Document): List<MyItem> {
		val list = ArrayList<MyItem>()
		val boardTable = doc.select(".mid2 > table:nth-child(3)").select("tr")
		val p_subj = doc.select("form > input[name=p_subj]").attr("value")
		val p_year = doc.select("form > input[name=p_year]").attr("value")
		val p_subjseq = doc.select("form > input[name=p_subjseq]").attr("value")
		val p_class = doc.select("form > input[name=p_class]").attr("value")
		for (i in boardTable) {
			if (i.`is`("tr:nth-child(2)") || i.`is`("tr:nth-child(3)") || i.childNodeSize() < 10 || i.text().isEmpty()) {	// 제목, 빈 행 제외
				continue
			}
			val name = i.select("td:nth-child(2) > samp").text()
			val time = i.getElementsByIndexEquals(2).text().trim() + " | " + i.select("td:nth-child(4)").text()
			val onclick = i.select("td:nth-child(5) > table > tbody > tr > td:nth-child(2)").attr("onclick")
			val m = Pattern.compile("'(.*?)'").matcher(onclick)
			m.find()
			var p_ordseq = m.group(1)!!
			if (onclick.contains("whenInsert")) {
				p_ordseq += "."	// 약간의 꼼수;;
			}
			list.add(MyItem(name, time, p_ordseq, p_subj, p_year, p_subjseq, p_class))
		}
		return list
	}

	override fun getServlet() = "ReportStuServlet"
}
