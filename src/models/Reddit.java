package models;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "Reddit")
public class Reddit {
	// constructor
		public Reddit() {
			super();
		}

		@DatabaseField(generatedId = true)
		@JsonIgnore
		private int id;

		@DatabaseField
		@JsonProperty("url")
		private String url;

		@DatabaseField
		@JsonProperty("selftext")
		private String selftext;

		@DatabaseField
		@JsonProperty("thumbnail")
		private String thumbnail;

		@DatabaseField
		@JsonProperty("author_flair_text")
		private String author_flair_text;

		@DatabaseField
		@JsonProperty("title")
		private String title;

		public void setId(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		// getter and setters
		public String getAuthor_flair_text() {
			return author_flair_text;
		}

		public void setAuthor_flair_text(String author_flair_text) {
			this.author_flair_text = author_flair_text;
		}

		public String getThumbnail() {
			return thumbnail;
		}

		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}

		public String getSelfText() {
			return selftext;
		}

		public void setSelfText(String selftext) {
			this.selftext = selftext;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public String toString() {
			return this.url;
		}
}
