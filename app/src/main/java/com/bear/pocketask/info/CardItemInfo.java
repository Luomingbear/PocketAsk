package com.bear.pocketask.info;

/**
 * 卡片信息类
 * Created by bear on 16/10/1.
 */

public class CardItemInfo
{
	private String headPic; //头像
	private String userName; //用户名
	private String detailPic; //卡片详情图片
	private String detailUrl; //卡片详细的链接
	private String questions; //问题描述
	private int questionId; //问题id

	public CardItemInfo()
	{
	}

	public CardItemInfo(String headPic, String userName, String detailPic, String detailUrl, String questions, int questionId)
	{
		this.headPic = headPic;
		this.userName = userName;
		this.detailPic = detailPic;
		this.detailUrl = detailUrl;
		this.questions = questions;
		this.questionId = questionId;
	}

	public int getQuestionId()
	{
		return questionId;
	}

	public void setQuestionId(int questionId)
	{
		this.questionId = questionId;
	}

	public String getQuestions()
	{
		return questions;
	}

	public void setQuestions(String questions)
	{
		this.questions = questions;
	}

	public String getHeadPic()
	{
		return headPic;
	}

	public void setHeadPic(String headPic)
	{
		this.headPic = headPic;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getDetailPic()
	{
		return detailPic;
	}

	public void setDetailPic(String detailPic)
	{
		this.detailPic = detailPic;
	}

	public String getDetailUrl()
	{
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl)
	{
		this.detailUrl = detailUrl;
	}
}
