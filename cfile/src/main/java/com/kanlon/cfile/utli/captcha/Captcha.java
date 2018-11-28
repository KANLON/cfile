package com.kanlon.cfile.utli.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 验证码生成器
 *
 * @author zhangcanlong
 * @date 2018年11月28日
 */
public class Captcha {

	/**
	 * 图片的宽度
	 */
	private int width = 120;

	/**
	 * 图片的高度
	 */
	private int height = 50;

	/**
	 * 验证码字符个数
	 */
	private int codeCount = 5;

	/**
	 * 验证码干扰线数
	 */
	private int lineCount = 3;

	/**
	 * 验证码字符串
	 */
	private String code = null;

	/**
	 * 验证码图片buffer
	 */
	private BufferedImage buffImg = null;

	/**
	 * 随机数
	 */
	private Random random = new Random();

	/**
	 * 字符队列
	 */
	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };

	public Captcha() {
		this(120, 40, 5, 3);
	}

	/**
	 * @param width
	 *            图片宽
	 * @param height
	 *            图片高
	 * @param codeCount
	 *            字符个数
	 * @param lineCount
	 *            干扰线条数
	 */
	private Captcha(int width, int height, int codeCount, int lineCount) {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		this.lineCount = lineCount;
		createRandomCaptcha();
	}

	/**
	 * 创建随机验证码
	 */
	private void createRandomCaptcha() {

		// randomCode记录随机产生的验证码
		StringBuilder randomCode = new StringBuilder();
		for (int i = 0; i < codeCount; i++) {
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			randomCode.append(strRand);
		}
		// 生成随机字符
		this.code = randomCode.toString();
	}

	/**
	 * 创建验证码颜色方案
	 */
	private void createImg() {

		int x, fontHeight, codeY;
		int red, green, blue;

		x = width / (codeCount + 3);// 每个字符的宽度
		fontHeight = height - 2;// 字体的高度
		codeY = height - 4;

		// 图像buffer
		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();

		// 将图像填充为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// 创建字体
		CaptchaImgFont captchaImgFont = new CaptchaImgFont();
		Font font = captchaImgFont.getFont(fontHeight);
		g.setFont(font);

		// 随机产生codeCount个字符的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			char[] str = code.toCharArray();
			g.drawString(String.valueOf(str[i]), (i + 1) * x, codeY);
			// 将产生的四个随机数组合在一起。
		}

		// 随机生成干扰线的颜色
		for (int i = 0; i < lineCount; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width);
			int ye = ys + random.nextInt(height);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			g.drawLine(xs, ys, xe, ye);
		}
	}

	/**
	 * 创建验证码图片
	 *
	 * @param path
	 *            图片存放路径
	 * @throws IOException
	 *             输入输出异常
	 */
	public void createCaptchaImg(String path) throws IOException {
		OutputStream sos = new FileOutputStream(path);
		this.createCaptchaImg(sos);
	}

	/**
	 * 创建验证码图片
	 *
	 * @param sos
	 *            输出流
	 * @throws IOException
	 *             输入输出异常
	 */
	public void createCaptchaImg(OutputStream sos) throws IOException {
		createImg();
		ImageIO.write(buffImg, "png", sos);
	}

	/**
	 * 获得验证码字符串
	 *
	 * @return 验证码字符串
	 */
	public String getCode() {
		if (code == null || "".equals(code)) {
			createRandomCaptcha();
		}
		return code;
	}
}