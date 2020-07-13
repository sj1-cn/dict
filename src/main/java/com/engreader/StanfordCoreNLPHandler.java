package com.engreader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.eclipse.jetty.io.RuntimeIOException;

import com.alibaba.fastjson.JSONObject;
import com.engreader.StanfordNLPStemmer.WordFrequency;

public class StanfordCoreNLPHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StanfordCoreNLPHandler() {
		super();

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject ret = new JSONObject();
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
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		resp.setHeader("Access-Control-Max-Age", "3600");
		resp.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		// 是否支持cookie跨域
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		
		String codecontent = req.getParameter("codecontent");
		codecontent = codecontent.replaceAll("youjequalsign", "=").replaceAll("youjscryoujipttag", "script");

		StringBuffer awe = StanfordNLPStemmer.parseWords(codecontent);

		StringBuffer sb = new StringBuffer();
//		sb.append("<html><head>"
//				+ "<script src=\"./static/js/main.js\" type=\"text/javascript\"></script>"
//				+ "<link rel=\"stylesheet\" href=\"./css/words.css\">"
//				+ "<meta charset=\"utf-8\">"
//				+ "</head><body>");
		//	
		sb.append(awe);
//		sb.append("</body></html>");

		resp.setCharacterEncoding("utf-8");
		resp.getWriter().write(sb.toString());
	}
	
}