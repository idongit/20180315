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
 * ΢��У��
 * 
 * @author lilonglong
 * @date 2018��3��14��
 *
 */
@Controller
@RequestMapping("/wechart")
public class WeChartController {

	private final String TOKEN = "idongwecharttoken";
	/**
	 * ΢�Ž�����֤
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
	 * ժ��΢�Ź��ںſ����ĵ�
	 * 1����token��timestamp��nonce�������������ֵ�������
	 * 2�������������ַ���ƴ�ӳ�һ���ַ�������sha1����
	 * 3�������߻�ü��ܺ���ַ�������signature�Աȣ���ʶ��������Դ��΢��
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
