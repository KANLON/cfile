package com.kanlon.cfile.utli;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * 邮箱发送工具类
 *
 * @author zhangcanlong
 * @date 2018年11月27日
 */
@Component
public class MailUtil {
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * 发送者
	 */
	@Value("${mail.fromMail.addr}")
	private String from;

	/**
	 * 简单的邮件发送，发送纯文本
	 *
	 * @param to
	 *            接受者
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	public void sendSimpleMail(String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		try {
			mailSender.send(message);
			logger.info("简单邮件已经发送");
		} catch (Exception e) {
			logger.error("发送简单简单邮件时发生异常！", e);
		}
	}

	/**
	 * 发送html邮件
	 *
	 * @param to
	 *            接受者
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	public void sendHtmlMail(String to, String subject, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			// true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			mailSender.send(message);
			logger.info("html邮件发送成功");
		} catch (MessagingException e) {
			logger.error("发送html邮件时发生异常！", e);
		}
	}

	/**
	 * 发送带附件的邮件
	 *
	 * @param to
	 *            接受者
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param filePath
	 *            文件路径
	 */
	public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content);

			FileSystemResource file = new FileSystemResource(new File(filePath));
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
			helper.addAttachment(fileName, file);
			mailSender.send(message);
			logger.info("带附件的邮件: " + filePath + "已经发送");
		} catch (MessagingException e) {
			logger.error("发送带附件的邮件时发送异常！", e);
		}
	}

	/**
	 * 发送静态资源的邮件
	 *
	 * @param to
	 *            接受者
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param rscPath
	 *            静态资源文件路劲
	 * @param rscId
	 *            静态资源id
	 */
	public void sendInlineResoureceMail(String to, String subject, String content, String rscPath, String rscId) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			FileSystemResource res = new FileSystemResource(new File(rscPath));
			helper.addInline(rscId, res);
			mailSender.send(message);
			logger.info("嵌入静态资源的邮件已经发送");
		} catch (MessagingException e) {
			logger.error("发送嵌入静态资源的邮件时发生异常！");
		}
	}

}
