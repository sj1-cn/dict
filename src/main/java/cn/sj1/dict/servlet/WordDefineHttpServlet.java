package cn.sj1.dict.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.sj1.dict.WordDefine;
import cn.sj1.dict.WordDefineStore;

public class WordDefineHttpServlet extends HttpServlet {

	WordDefineStore store;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WordDefineHttpServlet(WordDefineStore store) {
		super();
		this.store = store;

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("utf-8");
		resp.setHeader("content-type", "application/json;chartset=uft-8");
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		try {
			String wordStr = req.getPathInfo().substring(1);
			
			
			WordDefine wordDefine = store.get(wordStr);
			addToJson(sb, wordDefine);
			sb.append(',');
			sb.append("\"ret\":0");
		} catch (Exception ex) {
			sb.append("\"ret\":-1,");
			sb.append("\"error\":\"");
			sb.append(ex.getMessage());
			sb.append("\"");
		}
		sb.append('}');
		if (req.getParameter("callback") != null) {
			resp.getWriter().write(req.getParameter("callback") + "(" + sb.toString() + ")");
		} else {
			resp.getWriter().write(sb.toString());
		}
	}

	private void addToJson(StringBuilder sb, WordDefine w) {
		sb.append("\"data\":");
		sb.append('{');
		{
			sb.append("\"id\":").append(w.getId()).append(',');
			sb.append("\"word\":").append('\"').append(w.getWord()).append('\"').append(',');
			sb.append("\"cocaLevel\":").append('\"').append(w.getCocaLevel()).append('\"').append(',');
			sb.append("\"tense\":").append('\"').append(w.getTense()).append('\"').append(',');
			sb.append("\"accentEn\":").append('\"').append(w.getAccentEn()).append('\"').append(',');
			sb.append("\"accentUs\":").append('\"').append(w.getAccentUs()).append('\"').append(',');
			sb.append("\"meanZh\":").append('\"').append(w.getMeanZh()).append('\"').append(',');
			sb.append("\"meanBriefZh\":").append('\"').append(w.getMeanBriefZh()).append('\"').append(',');
			sb.append("\"meanEn\":").append('\"').append(w.getMeanEn()).append('\"').append(',');
			sb.append("\"pos\":").append('\"').append(w.getPos()).append('\"').append(',');
			sb.append("\"freq\":").append(w.getFreq()).append(',');
		}
		sb.setCharAt(sb.length() - 1, '}');
	}
//
//	@Override
//	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		resp.setCharacterEncoding("utf-8");
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("{");
//		try {
//			String strUserId = req.getPathInfo().substring(1);
//			int userId = Integer.parseInt(strUserId);
//
//			String wordsStr = req.getParameter("words");
//			String[] words = wordsStr.split(",");
//
//			String action = req.getParameter("action");
//			List<WordDefine> list;
//			switch (action) {
//			case "remember":
//				list = store.remember(userId, words);
//				addToJson(sb, list);
//				break;
//			case "forget":
//				list = store.forget(userId, words);
//				addToJson(sb, list);
//				break;
//			default:
//			}
//			sb.append(',');
//			sb.append("\"ret\":0");
//		} catch (Exception ex) {
//			sb.append("\"ret\":-1,");
//			sb.append("\"error\":\"");
//			sb.append(ex.getMessage());
//			sb.append("\"");
//		}
//		sb.append('}');
//
//		if (req.getParameter("callback") != null) {
//			resp.getWriter().write(req.getParameter("callback") + "(" + sb.toString() + ")");
//		} else {
//			resp.getWriter().write(sb.toString());
//		}
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		if ("DELETE".equalsIgnoreCase(req.getParameter("_method"))) {
//			doDelete(req, resp);
//		}
//
//		resp.setCharacterEncoding("utf-8");
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("{");
//		try {
//			String strUserId = req.getPathInfo().substring(1);
//			int userId = Integer.parseInt(strUserId);
//
//			String wordsStr = req.getParameter("words");
//			String[] words = wordsStr.split(",");
//			List<WordDefine> list = store.remember(userId, words);
//			addToJson(sb, list);
//			sb.append(',');
//			sb.append("\"ret\":0");
//		} catch (Exception ex) {
//			sb.append("\"ret\":-1,");
//			sb.append("\"error\":\"");
//			sb.append(ex.getMessage());
//			sb.append("\"");
//		}
//		sb.append('}');
//
//		if (req.getParameter("callback") != null) {
//			resp.getWriter().write(req.getParameter("callback") + "(" + sb.toString() + ")");
//		} else {
//			resp.getWriter().write(sb.toString());
//		}
//	}

}