package List;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import DAO.DBManager;

/**
 * ●Fortune telling.csvをomikujiテーブルに読み込むメソッド
 *
 * @author m_ochi
 *
 */
public class CSVReader {
	public static void readCsv() throws IOException {
		/**
		 * ①Fortune telling.csvファイルを読み込む
		 * １、CSVファイルを１行ずつ読み込む
		 */
		// ①ー１、CSVファイルを１行ずつ読み込む
		File file = new File("Fortune telling.csv");
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String data = "";
			bufferedReader.readLine(); // 不要な一行目をループ前に読み込んでいる。(CSVファイルの２行目から読み込見たいため。)
			int x = 0;
			// contentsというListのString型の箱をwhile文の外で作成しておく
			//（※箱は１つで良いため。また、bufferedReaderはListのためString[]になっている。）
			String[] contents;

			/**
			 * 変数data(読み込んだ値)がnullじゃない限り読み込む処理をする
			 *（※bufferedReader.readLine()で読み込まれたデータを変数dataに代入している）
			 */

			while ((data = bufferedReader.readLine()) != null) {
				 // 値を分解する
				contents = data.split(",");
				 // omikuji_idと紐づけるために上から順に番号をつける
				System.out.println("\nomikuji_id : " + x++);

				// fortune_idにつける番号をswitch文を使用して割り当てる。
				switch (contents[0]) {
				case "大吉":
//					System.out.println(1);
					break;
				case "中吉":
//					System.out.println("fortune_id : " + 2);
					break;
				case "小吉":
//					System.out.println("fortune_id : " + 3);
					break;
				case "末吉":
//					System.out.println("fortune_id : " + 4);
					break;
				case "吉":
//					System.out.println("fortune_id : " + 5);
					break;
				case "凶":
//					System.out.println("fortune_id : " + 6);
					break;
				}
//				System.out.println("運勢 : " + contents[0]);
//				System.out.println("願い事 : " + contents[1]);
//				System.out.println("商い : " + contents[2]);
//				System.out.println("学問 : " + contents[3]);
			}
			bufferedReader.close();
		} catch (

		IOException e) {
			e.printStackTrace();
		}
		/**
		 * ①Fortune telling.csvファイルを読み込む １、CSVファイルを１行ずつ読み込む
		 */
		/**
		 * ②JDBCを使用して読み込んだデータをomikujiテーブルに投入する １、JDBCをしようしてDBへ接続
		 * ２、CSVファイルを読み込んでDBへデータを入力する
		 */
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			if (connection != null) {
			}
			// statement = connection.createStatement();

			FileReader fileReader1 = new FileReader(file);
			BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
			String data = "";
			// CSVファイルの２行目から読み込む(不要な一行目をループ前に読み込んでいる。)
			bufferedReader1.readLine();
			// ------------------------------------------------------------------------------------------------
			// ２、CSVから読み込んだデータをコンソールへ出力する（※読み込まれているかを確認するため）
			// ------------------------------------------------------------------------------------------------
			int x = 1;

			// contentsというString型の箱をwhile文の外で作成しておく（※箱は１つで良いため）
			String[] contents;
			int unsei = 0;
			String wish = "";
			String business = "";
			String study = "";
			String ochi = "越智";

			while ((data = bufferedReader1.readLine()) != null) {
				// 値を分解する
				contents = data.split(",");
				// System.out.println("aaa"+contents);
				// omikuji_idと紐づけるために上から順に番号をつける
				unsei = JudgeUnseiCode(contents[0]);
				wish = contents[1];
				business = contents[2];
				study = contents[3];
				// System.out.println(study);
				// SQLの実行
				// System.out.println("connection:"+connection);
				String sql = "INSERT INTO omikuji(omikuji_id,fortune_id, wish, business, study, changer, update_date, author, create_date) values (?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp);";
				PreparedStatement ps = connection.prepareStatement(sql);
				// System.out.println("INSERT文:"+ps);
				ps.setInt(1, x++);
				ps.setInt(2, unsei);
				ps.setString(3, wish);
				ps.setString(4, business);
				ps.setString(5, study);
				ps.setString(6, ochi);
				ps.setString(7, ochi);
				System.out.println(ps);
				ps.executeUpdate();
				// connection.commit();
				// System.out.println(sql);
			}

		} catch (SQLException e) {
			e.getMessage();
		}
	}
/**
 * csvから読み込んだ文字列をomikuji_idの数字に変換するメソッド
 * （csvとomikujiテーブルのomikuji_idとfortuneテーブルのomikuji_idとを紐づけるため。）
 * @param s
 * @return
 */
	// fortune_idを文字から数字に割り当てたメソッド（例：大吉の場合 → １）
	private static int JudgeUnseiCode(String s) {
		int unsei = 0;
		// fortune_idにつける番号をswitch文を使用して割り当てる。
		switch (s) {
		case "大吉":
			unsei = 1;
			break;
		case "中吉":
			unsei = 2;
			break;
		case "小吉":
			unsei = 3;
			break;
		case "末吉":
			unsei = 4;
			break;
		case "吉":
			unsei = 5;
			break;
		case "凶":
			unsei = 6;
			break;
		}
		return unsei;
	}

}
