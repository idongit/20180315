package com.idong.ctl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.idong.commom.utils.SHA1;

/**
 * 微信校验
 * 
 * @author lilonglong
 * @date 2018年3月14日
 *
 */
@Controller
@RequestMapping("/wechart")
public class WeChartController {

	private final String TOKEN = "idongwecharttoken";
	/**
	 * 微信接入验证
	 * @param request
	 * @param response
	 */
	@RequestMapping("/check")
	public void weChartCheck(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			String echostr = request.getParameter("echostr");
			PrintWriter out = response.getWriter();
			if (echostr != null && !"".equals(echostr)) {
				String check = this.check(request, response);
				if (check != null && !"".equals(check)) {
					out.print(echostr);
				}else{
					out.print("error");
				}
				out.flush();
				out.close();
				return;
			}
			out.print("123");
			request.setCharacterEncoding("UTF-8");
			// out.print(processRequest(request));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 摘自微信公众号开发文档
	 * 1）将token、timestamp、nonce三个参数进行字典序排序
	 * 2）将三个参数字符串拼接成一个字符串进行sha1加密
	 * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private String check(HttpServletRequest request, HttpServletResponse response) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		String reSignature = null;
		try {
			String[] strArray = { TOKEN, timestamp, nonce };
			Arrays.sort(strArray);
			String sortStr = strArray[0] + strArray[1] + strArray[2];
			reSignature = new SHA1().getDigestOfString(sortStr.getBytes()).toLowerCase();
		} catch (Exception e) {
		}
		if (reSignature != null && reSignature.equals(signature)) {
			return echostr;
		}
		return null;
	}
}
