package com.kanlon.cfile.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.kanlon.cfile.DemoApplication;
import com.kanlon.cfile.utli.MailUtil;

/**
 * 邮件发送的简单测试类
 *
 * @author zhangcanlong
 * @date 2018年11月27日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class MailUtilTest {

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private MailUtil mailUtil;

	@Test
	public void testSimpleMail() throws Exception {
		mailUtil.sendSimpleMail("2078697336@qq.com", "spring boot 测试邮件", "spring boot 你好，世界");
	}

	@Test
	public void testHtmlMail() throws Exception {
		String content = "<html>\n" + "<body>\n" + " <h3>hello world!这是一封Html邮件!</h3>\n" + "</body>\n" + "</html>";
		mailUtil.sendHtmlMail("2078697336@qq.com", "html测试邮件", content);
	}

	@Test
	public void sendAttachmentsMail() {
		String filePath = "C:\\Users\\hasee\\Desktop\\秋招复习计划.xlsx";
		mailUtil.sendAttachmentsMail("s19961234@126.com", "含有附件的测试邮件", "具体内容在附件", filePath);

	}

	@Test
	public void sendInlineResourceMail() {
		String rscId = "23";
		String content = "<html><body>这里是有图片的邮件:<img src=\'cid:" + rscId + "\'></body></html>";
		String imgPath = "C:\\Users\\hasee\\Desktop\\微信图片_20181125124802.jpg";
		mailUtil.sendInlineResoureceMail("2078697336@qq.com", "静态资源的测试邮件", content, imgPath, rscId);
	}

	@Test
	public void sendTemplateMail() {
		// 创建邮件正文
		Context context = new Context();
		context.setVariable("id", "06");
		String emailContent = templateEngine.process("emailTemplate", context);
		mailUtil.sendHtmlMail("2078697336@qq.com", "主题：这是模板邮件", emailContent);
	}

}
