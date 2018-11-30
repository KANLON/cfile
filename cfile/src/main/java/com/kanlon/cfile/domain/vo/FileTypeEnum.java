package com.kanlon.cfile.domain.vo;

/**
 * 提交的文件的类型
 *
 * @author zhangcanlong
 * @date 2018年11月30日
 */
public enum FileTypeEnum {
	/**
	 * word文档，以doc或docx结尾
	 */
	WORD,
	/**
	 * excel文件，xls或xlsx结尾
	 */
	EXCEL,
	/**
	 * PowerPoint文件，PPT文件，以ppt，pptx，pptm结尾
	 */
	POWERPOINT,
	/**
	 * image图片文件，以png，jpeg，jpg，tif，BMP，GIF
	 */
	IMAGE,
	/**
	 * pdf文件
	 */
	PDF
}
