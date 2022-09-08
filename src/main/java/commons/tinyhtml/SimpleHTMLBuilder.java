package commons.tinyhtml;

import java.util.function.Consumer;

public class SimpleHTMLBuilder extends HTMLBuilder {

	public SimpleHTMLBuilder() {
		super();
		init();
	}

	private void init() {

		super.jsFile("/static/lib/uikit/uikit.min.js");
		super.jsFile("/static/lib/uikit/uikit-icons.min.js");
		super.styleFile("/static/lib/uikit/uikit.min.css");

		// autoComplete
//		super.jsFile("/static/lib/autoComplete/autoComplete.min.js");
//		super.styleFile("/static/lib/autoComplete/autoComplete.min.css");

//		super.jsFile("https://unpkg.com/react@17/umd/react.development.js");
//		super.jsFile("https://unpkg.com/react-dom@17/umd/react-dom.development.js");

		// axios
		super.jsFile("/static/lib/axios/axios.min.js");

//		super.jsFile("/static/js/editDefinition.js");

	}

	public SimpleHTMLBuilder(String requestPath, StringBuilder sb) {
		super(requestPath, sb);
		init();
	}

	public SimpleHTMLBuilder(String requestPath) {
		super(requestPath);
		init();
	}

	public void model(String id, String title, Consumer<HTMLBuilder> content) {

		mainbodyStringBuilder.append("<!-- This is the modal -->\n"
				+ "<div id=\"" + id + "\" uk-modal>\n"
				+ "    <div class=\"uk-modal-dialog uk-modal-body\">\n"
				+ "        <h2 class=\"uk-modal-title\">" + title + "</h2>\n");

		content.accept(this);

		mainbodyStringBuilder.append(""
				+ "    </div>\n"
				+ "</div>");

	}

	@Override
	public StringBuilder toStandardPage(String textTitle, Consumer<StringBuilder> content) {
		super.styleFile("/static/css/main.css");
		super.jsFile("/static/js/main.js");
		return super.toStandardPage(textTitle, content);
	}

	@Override
	public StringBuilder toStandardPage(String title) {
		return this.toStandardPage(title, null);
	}

}
