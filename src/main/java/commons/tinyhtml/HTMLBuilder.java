package commons.tinyhtml;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.jooby.Context;
import io.jooby.MediaType;

public class HTMLBuilder {

	protected final StringBuilder mainbodyStringBuilder;
	List<String> styles = new ArrayList<String>();
	List<String> styleFiles = new ArrayList<>();
	List<String> jsFiles = new ArrayList<>();
	protected String requestPath;

	public HTMLBuilder() {
		this(".", new StringBuilder());
	}

	public HTMLBuilder(String requestPath) {
		this(requestPath, new StringBuilder());
	}

	public HTMLBuilder(String requestPath, StringBuilder sb) {
		this.mainbodyStringBuilder = sb;
		this.requestPath = requestPath;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void style(String style) {
		this.styles.add(style);
	}

	public void styleFile(String styleFile) {
		this.styleFiles.add(styleFile);
	}

	public void jsFile(String jsFile) {
		this.jsFiles.add(jsFile);
	}

	public StringBuilder getStringBuilder() {
		return mainbodyStringBuilder;
	}

	public final HTMLBuilder a(String href, String content) {
		mainbodyStringBuilder.append("<a href=\"").append(href).append("\">");
		mainbodyStringBuilder.append(content);
		mainbodyStringBuilder.append("</a>");
		return this;
	}

	public final void table(Consumer<HTMLBuilder> content) {
		table_();
		content.accept(this);
		_table();
	}

	public final void table_() {
		mainbodyStringBuilder.append("<table>\n");
	}

	public final void _table() {
		mainbodyStringBuilder.append("</table>\n");
	}

	public final void caption(String content) {
		caption_();
		mainbodyStringBuilder.append(content);
		_caption();
	}

	public final void caption(Consumer<HTMLBuilder> content) {
		caption_();
		content.accept(this);
		_caption();
	}

	public final void caption_() {
		mainbodyStringBuilder.append("<caption>");
	}

	public final void _caption() {
		mainbodyStringBuilder.append("</caption>");
	}

	public final void thead(Consumer<HTMLBuilder> content) {
		thead_();
		content.accept(this);
		_thead();
	}

	public final void thead_() {
		mainbodyStringBuilder.append("<thead>\n");
	}

	public final void _thead() {
		mainbodyStringBuilder.append("</head>");
	}

	public final void tbody(Consumer<HTMLBuilder> content) {
		tbody_();
		content.accept(this);
		_tbody();
	}

	public final void tbody_() {
		mainbodyStringBuilder.append("<tbody>\n");
	}

	public final void _tbody() {
		mainbodyStringBuilder.append("</tbody>\n");
	}

	public final void tr(Consumer<HTMLBuilder> content) {
		tr_();
		content.accept(this);
		_tr();
	}

	public final void tr_() {
		mainbodyStringBuilder.append("<tr>\n");
	}

	public final void _tr() {
		mainbodyStringBuilder.append("</tr>\n");
	}

	public final void th(String clazz, int minWidth, String text) {
		mainbodyStringBuilder.append('<').append("th").append(' ').append("class=\"" + clazz + "\" style=\"min-width:").append(minWidth).append("px;\"max-width:").append(minWidth).append("px;\"").append('>');
		mainbodyStringBuilder.append(text);
		mainbodyStringBuilder.append("</").append("th").append('>');
	}

	public final void th(String clazz, int minWidth, Consumer<HTMLBuilder> content) {
		mainbodyStringBuilder.append('<').append("th").append(' ').append("class=\"" + clazz + "\" style=\"min-width:").append(minWidth).append("px;\"max-width:").append(minWidth).append("px;\"").append('>');
		content.accept(this);
		mainbodyStringBuilder.append("</").append("th").append('>');
	}

	public final void th_(String clazz, int minWidth) {
		mainbodyStringBuilder.append('<').append("th").append(' ').append("class=\"" + clazz + "\" style=\"min-width:").append(minWidth).append("px;\"max-width:").append(minWidth).append("px;\"").append('>');
	}

	public final void _th() {
		mainbodyStringBuilder.append("</").append("th").append('>');
	}

	public final void td(Object value) {
		mainbodyStringBuilder.append('<').append("td").append('>');

		if (value != null) {
			mainbodyStringBuilder.append(String.valueOf(value));
		} else {
			mainbodyStringBuilder.append("-");
		}

		_td();
	}

	public void td_(String clazz) {
		mainbodyStringBuilder.append('<').append("td").append(" class=\"" + clazz + "\"").append('>');
	}

	public void td_(int colspan, String clazz) {
		mainbodyStringBuilder.append('<').append("td")
				.append(" colspan=\"").append(colspan).append("\"")
				.append(" class=\"" + clazz + "\"")
				.append('>');
	}

	public final void td(String clazz, Object value) {
		mainbodyStringBuilder.append('<').append("td").append(" class=\"" + clazz + "\"").append('>');

		if (value != null) {
			mainbodyStringBuilder.append(String.valueOf(value));
		} else {
			mainbodyStringBuilder.append("-");
		}

		_td();
	}

	public final void td(Consumer<HTMLBuilder> content) {
		mainbodyStringBuilder.append('<').append("td").append('>');
		if (content != null) {
			content.accept(this);
		} else {
			mainbodyStringBuilder.append("-");
		}
		_td();
	}

	public final void td(int colspan, int maxWidth, Object value) {
		if (colspan == 1) {
			mainbodyStringBuilder.append('<').append("td").append(" style=\"")
					.append("min-width:").append(maxWidth).append("px;")
					.append("max-width:").append(maxWidth).append("px;")
					.append("\"").append('>');
		} else {
			mainbodyStringBuilder.append('<').append("td")
					.append(" colspan=\"").append(colspan).append("\"")
					.append(" style=\"max-width:").append(maxWidth).append("px;\"")
					.append(" title=\"").append(value).append("\"")
					.append('>');
		}

		if (value != null) {
			mainbodyStringBuilder.append(String.valueOf(value));
		} else {
			mainbodyStringBuilder.append("-");
		}

		_td();
	}

	public final void td(int maxWidth, Object content) {
		if (content != null) {
			td_(1, maxWidth, String.valueOf(content));
			mainbodyStringBuilder.append(String.valueOf(content));
			_td();
		} else {
			td_(maxWidth);
			mainbodyStringBuilder.append("-");
			_td();
		}
	}

	public void td_(int maxWidth) {
		mainbodyStringBuilder.append('<').append("td").append(" style=\"max-width:").append(maxWidth).append("px;\"").append('>');
	}

	public void td_(byte colspan, int maxWidth) {
		if (colspan == 1) {
			td_(maxWidth);
		} else {
			mainbodyStringBuilder.append('<').append("td")
					.append(" colspan=\"").append(colspan).append("\"")
					.append(" style=\"max-width:").append(maxWidth).append("px;\"")
					.append('>');

		}
	}

	public void td_(int colspan, int maxWidth, String title) {
		mainbodyStringBuilder.append('<').append("td")
				.append(" style=\"max-width:").append(maxWidth).append("px;\" ")
				.append("title=\"").append(title).append("\"").append('>');
	}

	public void _td() {
		mainbodyStringBuilder.append("</").append("td").append('>');
	}

	public void div_() {
		mainbodyStringBuilder.append('<').append("div").append('>');
	}

	public void div_(String... clazz) {
		if (clazz.length > 0) {
			String strClass = String.join(" ", clazz);
			mainbodyStringBuilder.append('<').append("div").append(" class=\"").append(strClass).append('\"').append('>');
		} else {
			mainbodyStringBuilder.append('<').append("div").append('>');
		}
	}

	public void _div() {
		mainbodyStringBuilder.append("</").append("div").append('>');
	}

	public void ul_() {
		mainbodyStringBuilder.append('<').append("ul").append('>');
	}

	public void ul_(String... clazz) {
		if (clazz.length > 0) {
			String strClass = String.join(" ", clazz);
			mainbodyStringBuilder.append('<').append("ul").append(" class=\"").append(strClass).append('\"').append('>');
		} else {
			mainbodyStringBuilder.append('<').append("ul").append('>');
		}
	}

	public void _ul() {
		mainbodyStringBuilder.append("</").append("ul").append('>');
	}

	public void li_() {
		mainbodyStringBuilder.append('<').append("li").append('>');
	}

	public void li_(String... clazz) {
		if (clazz.length > 0) {
			String strClass = String.join(" ", clazz);
			mainbodyStringBuilder.append('<').append("li").append(" class=\"").append(strClass).append('\"').append('>');
		} else {
			mainbodyStringBuilder.append('<').append("li").append('>');
		}
	}

	public void _li() {
		mainbodyStringBuilder.append("</").append("li").append('>');
	}

	public StringBuilder append(String string) {
		return mainbodyStringBuilder.append(string);
	}

	public StringBuilder append(char c) {
		return mainbodyStringBuilder.append(c);
	}

	public void span(String text) {
		mainbodyStringBuilder.append('<').append("span").append('>');
		mainbodyStringBuilder.append(text);
		mainbodyStringBuilder.append("</").append("span").append('>');
	}

	public void span(String text, String... others) {
		mainbodyStringBuilder.append('<').append("span").append('>');
		mainbodyStringBuilder.append(text);
		for (String string : others) {
			mainbodyStringBuilder.append(string);
		}
		mainbodyStringBuilder.append("</").append("span").append('>');
	}

	public StringBuilder toStandardPage(String textTitle, Consumer<StringBuilder> content) {
		StringBuilder sb = new StringBuilder();

		sb.append("<!DOCTYPE html>\n"
				+ "<html lang=\"zh-cn\">\n"
				+ "<head>\n"
				+ "    <meta charset=\"utf-8\" />\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\" />");

		for (String filepath : styleFiles) {
			sb.append("<link media=\"all\" rel=\"stylesheet\" href=\"").append(filepath).append("\" />");
		}
		sb.append("</head>\n");

		for (String style : styles) {
			sb.append("    <style>").append(style).append("</style>");
		}

		sb.append("    <style>"
				+ "        html {\n"
				+ "            box-sizing: border-box;\n"
				+ "        }"
				+ "        *,\n"
				+ "        *:after,\n"
				+ "        *:before {\n"
				+ "            box-sizing: inherit;\n"
				+ "        }        "
				+ "        body {\n"
				+ "            margin: 0;\n"
				+ "            padding: 0;\n"
				// + " font: 18px 'Oxygen', Helvetica;\n"
				+ "            background: #ececec;\n"
				+ "        }"
				+ "        header {\n"
				+ "            height: 60px;\n"
				+ "            background: #512DA8;\n"
				+ "            color: #fff;\n"
				+ "            display: flex;\n"
				+ "            align-items: center;\n"
				+ "            padding: 0 40px;\n"
				+ "            box-shadow: 1px 2px 6px 0px #777;\n"
				+ "        }"
				+ "        h1 {\n"
				+ "            margin: 0;\n"
				+ "        }"
				+ "        .banner {\n"
				+ "            text-decoration: none;\n"
				+ "            color: #fff;\n"
				+ "            cursor: pointer;\n"
				+ "        }"
				+ "        main {\n"
				// + " display: flex;\n"
				+ "            justify-content: center;\n"
				// + " height: calc(100vh - 140px);\n"
				+ "            padding: 20px 40px;\n"
				// + " overflow-y: auto;\n"
				+ "        }"
				+ "\n"
				+ "        footer {\n"
				+ "            height: 40px;\n"
				+ "            background: #2d3850;\n"
				+ "            color: #fff;\n"
				+ "            display: flex;\n"
				+ "            align-items: center;\n"
				+ "            padding: 40px;\n"
				+ "        }"
				+ "    </style>\n");

		sb.append("<title>").append(textTitle).append("</title>\n");

		sb.append("<body>\n"
				+ "        <!-- Main Application Section -->\n"
				+ "        <header>\n");
		sb.append("            <h3>");
		if (content == null) {
			sb.append("<a class=\"banner\">").append(textTitle).append("</a>");
		} else {
			sb.append("<a class=\"banner\">");
			content.accept(sb);
			sb.append("</a>");
		}

		sb.append("</h3>\n");
		sb.append("        </header>\n");

		sb.append("        <main id=\"app\">");

		sb.append(this.mainbodyStringBuilder);

		sb.append("</main>\n");

//		sb.append("        <footer>\n")
//				.append("            <span>").append(footer).append("</span>\n")
//				.append("        </footer>");

		for (String filepath : jsFiles) {
			sb.append("<script src=\"").append(filepath).append("\" crossorigin></script>");
		}

		sb.append("</body>\n"
				+ "</html>");
		return sb;

	}

	public void tag(String tagName) {
		mainbodyStringBuilder.append('<').append(tagName).append("/>");
	}

	public void tag_(String tagName) {
		mainbodyStringBuilder.append('<').append(tagName).append(">");
	}

	public void _tag(String tagName) {
		mainbodyStringBuilder.append("</").append(tagName).append(">");
	}

	public void tag(String tagName, String cssClass, String content) {

		mainbodyStringBuilder.append('<').append(tagName).append(" class=\"").append(cssClass).append('\"').append('>');
		mainbodyStringBuilder.append(content);
		mainbodyStringBuilder.append("</").append(tagName).append('>');
	}

	public void tag(String tagName, String content) {
		mainbodyStringBuilder.append('<').append(tagName).append('>');
		mainbodyStringBuilder.append(content);
		mainbodyStringBuilder.append("</").append(tagName).append('>');
	}

	public void tag(String tagName, Consumer<HTMLBuilder> content) {
		mainbodyStringBuilder.append('<').append(tagName).append('>');
		content.accept(this);
		mainbodyStringBuilder.append("</").append(tagName).append('>');
	}

	public void tag(String tagName, String cssClass, Consumer<HTMLBuilder> content) {
		mainbodyStringBuilder.append('<').append(tagName).append(" class=\"").append(cssClass).append('\"').append('>');
		content.accept(this);
		mainbodyStringBuilder.append("</").append(tagName).append('>');
	}

	public HTMLBuilder space() {
		this.append("&nbsp;");
		return this;
	}

	public HTMLBuilder br() {
		tag("br");
		return this;
	}

	public StringBuilder toStandardPage(String title) {
		return toStandardPage(title, null);
	}

	public void p(String text) {
		tag("p", text);
	}

	public void p(Consumer<HTMLBuilder> content) {
		tag("p", content);
	}

	public void button(String cssClass, String name) {
		tag("button", cssClass, name);
	}

	public void p(String cssClass, Consumer<HTMLBuilder> content) {
		tag("p", cssClass, content);

	}

	public HTMLBuilder hr() {
		tag("hr");
		return this;
	}

	public Object redirect(Context ctx, String url) {
		ctx.setResponseType(MediaType.html);
		this.getStringBuilder().append("<meta http-equiv=\"refresh\" content=\"0; url=" + url + "\" />");
		return this.toStandardPage("Redirect");
	}

}