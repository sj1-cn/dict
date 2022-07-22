package cn.sj1.aireader.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StanfordCoreNLPFrequencyHttpServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public StanfordCoreNLPFrequencyHttpServlet() {
		super();
	

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 ObjectMapper mapper = new ObjectMapper();
		ObjectNode ret = mapper.createObjectNode();
		try {
			String ttsTxt = req.getParameter("text");

//			String outFile = System.nanoTime() + ".mp4";
			String url = "xx.xxx(ttsTxt,...) 11" + ttsTxt;
			ret.put("ret", "0");
			ret.put("url", url);
		} catch (Exception ex) {
			ret.put("ret", "-1");
			ret.put("error", ex.getMessage());
		}
		if (req.getParameter("callback") != null) {
			resp.getWriter().write(req.getParameter("callback") + "(" + ret.toString() + ")");
		} else {
			resp.getWriter().write(ret.toString());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// StemmerTest2.parse(srcText, pipeline)
		String codecontent = req.getParameter("codecontent");
		codecontent = codecontent.replaceAll("youjequalsign", "=").replaceAll("youjscryoujipttag", "script");

//		ArrayList<WordFrequency> awe = StanfordNLPStemmer.parseWordFrequency(codecontent);
//
		StringBuffer sb = new StringBuffer();
//
//		for (int i = 0; i < awe.size(); i++) {
//			sb.append(awe.get(i).toString());
//			sb.append('\n');
//		}

		resp.setCharacterEncoding("utf-8");
		resp.getWriter().write(sb.toString());
	}

}