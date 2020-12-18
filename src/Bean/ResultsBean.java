package Bean;

import java.sql.Date;

public class ResultsBean {

	private Date results_date;
	private String omikuji_id;
	private String birthday;
	private String changer;
	private String update_date;
	private String author;
	private String create_date;

	public Date getResults_date() {
		return results_date;
	}

	public void setResults_date(Date results_date) {
		this.results_date = results_date;
	}

	public String getOmikuji_id() {
		return omikuji_id;
	}

	public void setOmikuji_id(String omikuji_id) {
		this.omikuji_id = omikuji_id;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getChanger() {
		return changer;
	}

	public void setChanger(String changer) {
		this.changer = changer;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	/**
	 * ?
	 */
	@Override
	public String toString() {
		return "ResultsBean [results_date=" + results_date + ", omikuji_id=" + omikuji_id + ", birthday=" + birthday
				+ ", changer=" + changer + ", update_date=" + update_date + ", author=" + author + ", create_date="
				+ create_date + "]";
	}

}
