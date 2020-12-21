package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Bean.OmikujiBean;

public class OmikujiDao {
	/**
	 * ●omikuji_idを条件におみくじを検索するメソッド
	 *（出力できるようにfortuneテーブルと結合している）
	 * @param omikuji_id
	 * @return omikujiBean
	 */
	public static OmikujiBean selectByOmikuji(String omikuji_id) {

		Connection connection = null;
		PreparedStatement ps = null;
		OmikujiBean omikujiBean = new OmikujiBean();
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// sqlにselect文を入れる
			String sql = "SELECT f.fortune_id, f.fortune_name, f.changer, f.update_date, f.author, f.create_date, o.omikuji_id, o.fortune_id, o.wish, o.business, o.study,o.changer, o.update_date, o.author, o.create_date FROM fortune f LEFT OUTER JOIN omikuji o ON f.fortune_id = o.fortune_id WHERE o.omikuji_id = ?;";
			// PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, omikuji_id);
			// resultsテーブルから値を取得
			ResultSet resultSet = preparedStatement.executeQuery();
			// ③ー２、結果ファイルを１行ずつ読む。
			while (resultSet.next()) {
				// // resultsテーブルを１行ずつ読み込んで出力する
				omikujiBean.setOmikuji_id(resultSet.getString("omikuji_id"));
				omikujiBean.setFortune_id(resultSet.getString("fortune_id"));
				omikujiBean.setWish(resultSet.getString("wish"));
				omikujiBean.setBusiness(resultSet.getString("business"));
				omikujiBean.setStudy(resultSet.getString("study"));
				omikujiBean.setChanger(resultSet.getString("changer"));
				omikujiBean.setUpdate_date(resultSet.getString("update_date"));
				omikujiBean.setAuthor(resultSet.getString("author"));
				omikujiBean.setCreate_date(resultSet.getString("create_date"));
				omikujiBean.setFortune_name(resultSet.getString("fortune_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			DBManager.close(ps, connection);

		}
		return omikujiBean;

	}

	/**
	 * omikujiテーブルの件数を検索するメソッド。
	 * （randomの引数(omikujiテーブルの登録数)をSQLのCountを使用して取得するため）
	 *
	 * @param num
	 * @return resultsBean
	 */
	public static int count() {

		Connection connection = null;
		PreparedStatement ps = null;
		int num = 0;
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// sqlにselect文を入れる（AS テーブル名（今回はnum）:１時的にnumテーブルを作成する）
			String sql = "SELECT COUNT(*) AS num FROM omikuji; ";
			// PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			// resultsテーブルから値を取得
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				num = resultSet.getInt("num");
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			DBManager.close(ps, connection);

		}
		return num;

	}

}
