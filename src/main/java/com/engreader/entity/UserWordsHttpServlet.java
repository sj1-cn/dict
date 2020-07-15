package com.engreader.entity;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserWordsHttpServlet extends HttpServlet {

	UserWordsStore store;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserWordsHttpServlet(UserWordsStore store) {
		super();
		this.store = store;

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("utf-8");

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		try {
			String strUserId = req.getPathInfo().substring(1);
			int userId = Integer.parseInt(strUserId);
			List<UserWord> list = store.getAll(userId);
			addToJson(sb, list);
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

	private void addToJson(StringBuilder sb, List<UserWord> list) {
		if (list.size() > 0) {

			sb.append("\"data\":");
			sb.append('[');
			for (UserWord w : list) {
				sb.append('\"');
				sb.append(w.word);
				sb.append('\"');
				sb.append(',');
			}
			sb.setCharAt(sb.length() - 1, ']');
		} else {
			sb.append("\"data\":[]");
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("utf-8");

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		try {
			String strUserId = req.getPathInfo().substring(1);
			int userId = Integer.parseInt(strUserId);

			String wordsStr = req.getParameter("words");
			String[] words = wordsStr.split(",");

			String action = req.getParameter("action");
			List<UserWord> list;
			switch (action) {
			case "remember":
				list = store.remember(userId, words);
				addToJson(sb, list);
				break;
			case "forget":
				list = store.forget(userId, words);
				addToJson(sb, list);
				break;
			default:
			}
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

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if ("DELETE".equalsIgnoreCase(req.getParameter("_method"))) {
			doDelete(req, resp);
		}

		resp.setCharacterEncoding("utf-8");

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		try {
			String strUserId = req.getPathInfo().substring(1);
			int userId = Integer.parseInt(strUserId);

			String wordsStr = req.getParameter("words");
			String[] words = wordsStr.split(",");
			List<UserWord> list = store.remember(userId, words);
			addToJson(sb, list);
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

}