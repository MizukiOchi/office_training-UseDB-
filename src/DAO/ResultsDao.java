package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Bean.ResultsBean;

public class ResultsDao {

	/**
	 * ③resultsテーブルから誕生日・本日の日付を条件にデータを取得する
	 *
	 * @param results_date,birthday
	 * @return resultsBean
	 */
	public static ResultsBean selectByBirthday(Date results_date, String birthday) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultsBean resultsBean = new ResultsBean();
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// sqlにselect文を入れる
			String sql = "SELECT results_date, omikuji_id, birthday, changer, update_date, author,create_date FROM results WHERE results_date = ? AND birthday = ?;";
			// PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, results_date);
			preparedStatement.setString(2, birthday);
			// resultsテーブルから値を取得
			ResultSet resultSet = preparedStatement.executeQuery();
			// ③ー２、結果ファイルを１行ずつ読む。
			while (resultSet.next()) {
				// // resultsテーブルを１行ずつ読み込んで出力する
				resultsBean.setResults_date(resultSet.getDate("results_date"));
				resultsBean.setOmikuji_id(resultSet.getString("omikuji_id"));
				resultsBean.setBirthday(resultSet.getString("birthday"));
				resultsBean.setChanger(resultSet.getString("changer"));
				resultsBean.setUpdate_date(resultSet.getString("update_date"));
				resultsBean.setAuthor(resultSet.getString("author"));
				resultsBean.setCreate_date(resultSet.getString("create_date"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			DBManager.close(ps, connection);

		}
		return resultsBean;

	}

	/**
	 * ③resultsテーブルから誕生日・本日の日付を条件にデータを取得する
	 *
	 * @param results_date, birthday, omikuji_id
	 * @return resultsBean
	 */
	public static void insertResults(Date results_date, String omikuji_id, String birthday) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultsBean resultsBean = new ResultsBean();
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// sqlにselect文を入れる
			String sql = "INSERT INTO results(results_date, omikuji_id, birthday, changer, update_date, author, create_date) VALUES (?, ?, ?, '越智', CURRENT_TIMESTAMP, '越智', CURRENT_TIMESTAMP); ";
			// PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, results_date);
			preparedStatement.setString(2, omikuji_id);
			preparedStatement.setString(3, birthday);
			// resultsテーブルから値を取得
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			DBManager.close(ps, connection);

		}


	}

}
