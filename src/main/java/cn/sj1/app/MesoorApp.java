package cn.sj1.app;

import io.jooby.Context;
import io.jooby.Jooby;
import io.jooby.Value;
import io.jooby.ValueNode;

public class MesoorApp extends Jooby {

	/**
	 * 针对forward的场合，添加上forword头
	 * 
	 * @param ctx
	 * @return
	 */
	public static String requestPathOf(Context ctx) {
		Value forwardby = ctx.header("forwardby");
		if (forwardby.isPresent()) {
			if (forwardby.value().length() == 0) {
				return ctx.getRequestPath();
			} else {
				return "/" + forwardby.value() + ctx.getRequestPath();
			}
		} else {
			ValueNode query = ctx.query("_forwardby");
			if (query.isMissing() || query.value().length() == 0) {
				return ctx.getRequestPath();
			} else {
				return "/" + query.value() + ctx.getRequestPath();
			}
		}
	}

}
