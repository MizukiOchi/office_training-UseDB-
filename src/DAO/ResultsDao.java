package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Bean.ResultsBean;

public class ResultsDao {

	/**
	 * ③resultsテーブルから誕生日・本日の日付を条件に一致したデータを取得するメソッド
	 *
	 * @param results_date,birthday
	 * @return resultsBean
	 */
	public static ResultsBean selectByBirthday(Date results_date, String birthday) {

		Connection connection = null; // 特定のDBとの接続
		PreparedStatement ps = null; // SQL文がプレコンパイルされ、PreparedStatementに格納される。
		ResultsBean resultsBean = new ResultsBean();
		try {
			/**
			 * ①DBManagerのgetConnectionメソッドを呼び出してDBに接続。
			 */
			connection = DBManager.getConnection();

			/**
			 * ②resultsテーブルから以下の２つを条件にデータを取得。 １、mainメソッドで取得した本日の日付（results_date）
			 * ２、FortuneDriveのcheckBirthdayメソッドで取得した誕生日（birthday）
			 */
			// ●変数sqlに条件検索できるようにSELECT文を代入
			String sql = "SELECT results_date, omikuji_id, birthday, changer, update_date, author,create_date FROM results WHERE results_date = ? AND birthday = ?;";
			// ●sqlに詰めたSELECT文をpreparedStatementに代入して動的に条件を変更できるようにする。
			PreparedStatement preparedStatement = connection.prepareStatement(sql); // MEMO:PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			preparedStatement.setDate(1, results_date); // ②ー１
			preparedStatement.setString(2, birthday); // ②ー２
			// ●executeQueryメソッドを呼び出してSELECT文を実行して、実行結果（=検索結果）をResultSet型の変数に代入
			ResultSet resultSet = preparedStatement.executeQuery();
			// ●変数resultSetに入っている実行結果をResultsBeanにsetしながら１行ずつ読み込む
			// （=条件に一致しているデータがあれば、変数resultSetに代入されている）
			while (resultSet.next()) {
				resultsBean.setResults_date(resultSet.getDate("results_date"));
				resultsBean.setOmikuji_id(resultSet.getString("omikuji_id"));
				resultsBean.setBirthday(resultSet.getString("birthday"));
				resultsBean.setChanger(resultSet.getString("changer"));
				resultsBean.setUpdate_date(resultSet.getString("update_date"));
				resultsBean.setAuthor(resultSet.getString("author"));
				resultsBean.setCreate_date(resultSet.getString("create_date"));
				// ↑※利用しないフィールドも今後利用するかもしれないためセットしていることが多い。
				// その理由から今回もセットしている。
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, connection);
		}
		return resultsBean;
	}

	/**
	 * ●result_date・omikuji_id・birthdayを動的に、その他のカラムは固定でINSERTするメソッド
	 *
	 * @param results_date,
	 *            birthday, omikuji_id
	 * @return resultsBean
	 */
	public static void insertResults(Date results_date, String omikuji_id, String birthday) {
		Connection connection = null;
		PreparedStatement ps = null;
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
