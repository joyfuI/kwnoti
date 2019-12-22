package tc.wo.joyfui.kwnoti.u_campus;

import org.jsoup.nodes.Document;

public interface PostStrategy {
	String getTitle(Document doc);
	String getContent(Document doc);
	String getServlet();
}
