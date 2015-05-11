package com.wbread.linuxvoice.data;

public class FeedItem {
	private String title, description, link, pubDate;
	private String enclosure_url;
    private String icon_url;
    private String content;

	public FeedItem() {
	}

	public FeedItem(String title, String description, String link, String pubDate, String enclosure_url,
                    String icon_url, String content) {
		super();
		this.title = title;
		this.description = description;
		this.link = link;
		this.pubDate = pubDate;
		this.enclosure_url = enclosure_url;
        this.icon_url = icon_url;
        this.content = content;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getEnclosure_url() {
		return enclosure_url;
	}
	public void setEnclosure_url(String enclosure_url) {
		this.enclosure_url = enclosure_url;
	}

    public String getIcon_url() {
        return icon_url;
    }
    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
