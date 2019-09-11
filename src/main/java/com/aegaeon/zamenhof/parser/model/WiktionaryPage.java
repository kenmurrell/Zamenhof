package com.aegaeon.zamenhof.parser.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WiktionaryPage
{

	private static final DateTimeFormatter informat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
	private static final DateTimeFormatter outformat = DateTimeFormatter.ofPattern("'D'yyyy-MM-dd'T'HH:mm");
	private String title;
	private int id;
	private int revision;
	private LocalDateTime timestamp;

	public String getTitle()
	{
		return this.title;
	}

	protected void setTitle(String title)
	{
		this.title = title;
	}

	public LocalDateTime getTimestamp()
	{
		return this.timestamp;
	}

	public void setTimestamp(String timestamp)
	{

		this.timestamp = LocalDateTime.parse(timestamp, informat);
	}

	public String getStringTimestamp()
	{
		return this.timestamp.format(outformat);
	}

	public int getRevision()
	{
		return this.revision;
	}

	public void setRevision(String revision)
	{
		this.revision = Integer.parseInt(revision);
	}

	public int getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = Integer.parseInt(id);
	}
}
