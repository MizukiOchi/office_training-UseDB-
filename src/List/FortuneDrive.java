package List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import Bean.OmikujiBean;
import Bean.ResultsBean;
import DAO.OmikujiDao;
import DAO.ResultsDao;

public class FortuneDrive {
	public static void main(String[] args) throws IOException, SQLException {
		// fortunewWithDB.fortune();


		/**
		 * ①誕生日の入力をする
		 */
		System.out.println("誕生日を入力して下さい。(例：20200709)");
		String birthday = checkBirthday();
//		System.out.println(birthday);

		/**
		 * ②今日の日付を取得する
		 */
		 Date date = new Date();
		 java.sql.Date results_date = convert(date);
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		/**
		 * ③resultsテーブルから誕生日・本日の日付を条件にデータを取得する
		 * １、JDBCで繋ぐ
		 * ２、resultsテーブルから条件に当てはまるデータを取得する
		 * ３、データがあれば④。
		 * ４、テータがなければ⑤。
		 * return resultsBean
		 */
		//javaクラスで求めた本日の日付を利用して検索するように変更する（ResultsDaoの変更もあり）
		 ResultsBean rb = ResultsDao.selectByBirthday(results_date,birthday);

		if (rb.getOmikuji_id() != null) {
			/**
			 * ④ ③で取得したデータのomikuji__idを使用して、omikujiテーブルとfortuneテーブルの結合したデータを取得→出力→終了
			 * return omikujiBean(fortune_nameもフィールドに記入すること！)
			 */
			String getOmikuji_id = rb.getOmikuji_id();
			OmikujiBean ob = OmikujiDao.selectByOmikuji(getOmikuji_id);
			showDisplay(ob);

		} else if (rb.getOmikuji_id() == null) {
			/**
			 * ⑤resultsテーブルに入力した結果がない場合→おみくじを生成する
			 * １、もしomikujiテーブルにデータがなければ、omikujiテーブルのデータを書き込むメソッドを呼ぶ
			 * ２、omikujiテーブルのレコード数(sqlのcountを使用)でrandomの数字を取得
			 * ２、⑤ー１、で出た数字をデータのomikuji_idを条件にして、SELECT文を使用してomikujiテーブル（fortuneテーブルも結合する）からデータを取得
			 * ３、resultsテーブルに⑤ー２、で取得したデータを登録する（omikuji_id, results_date, birthday）
			 * ４、コンソールに⑤−２、で取得したデータを出力する。
			 */
			//⑤ー１、もしomikujiテーブルにデータがなければ、omikujiテーブルのデータを書き込むメソッド(OmikujiDao.count())を呼ぶ
			int num = OmikujiDao.count();
			if(num == 0){
			CSVReader.readCsv();
			}
			//
			Random random = new Random();
			//TODO DBの接続して、randomの引数をSQLのCountを使用して取得する
			int randomValue = random.nextInt(num + 1);
			String omikuji_id = Integer.toString(randomValue);
			System.out.println("ランダム数の確認："+ omikuji_id);

			//TODO 取得したomikuji_id・birthday・results_dateをresultsテーブルにINSERTする
			ResultsDao.insertResults(results_date, omikuji_id, birthday);
			System.out.println("omikuji_idをランダムしてresultsテーブルに結果をINSERTしました");

			//TODO omikuji_idを条件にomikujiテーブルからデータを取得して、コンソールに出力する
			System.out.println("これから結果をコンソールに出力します。");
			OmikujiBean oi = OmikujiDao.selectByOmikuji(omikuji_id);
			showDisplay(oi);
		}
	}

	public static String checkBirthday() {
		String birthday = "";
		while (true) {
			/**
			 * 入力チェックをする。 １、入力された日付が８桁以外の場合は、エラーメッセージを出力 ２、正しい年月日かどうか入力チェックする。
			 */
			try {

				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				birthday = reader.readLine();
				// １、入力されたのが８桁かどうか
				// ８桁以外が入力された場合→"例にの通り、８桁を入力してください。"
				if (birthday.length() != 8) {
					// 次の処理に行かずに次のループに入る。（初めから）
					System.out.println("例の通り8桁を入力してください。");
					continue;
				}
				// ２、正しい年月日かどうかをチェック
				// 正しい年月日でない場合 →"正しい日付を入力してください。"→再入力を求める
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				// 日付/時刻解析を厳密に（＝存在しない日付を指定された場合、Exception を発生させること。）行うかどうかを設定する。
				format.setLenient(false);
				try {
					format.parse(birthday);
					// ループを抜ける
					break;
				} catch (Exception e) {
					System.out.println("正しい日付を入力してください。");
					continue;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		}
		return birthday;
	}

	//TODO ここに出力用のメソッドを作成する
	public static void showDisplay(OmikujiBean ob) {
		 StringBuilder sb = new StringBuilder();
			  sb.append("今日の運勢は");
			  sb.append(ob.getFortune_name());
			  sb.append("です");
			  sb.append("\n 願い事：");
			  sb.append(ob.getWish());
			  sb.append("\n 商い：");
			  sb.append(ob.getBusiness());
			  sb.append("\n 学問：");
			  sb.append(ob.getStudy());
			  System.out.println(sb.toString());

	}
	private static java.sql.Date convert(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

	// @SuppressWarnings("null")
	// public void fortune() throws IOException, SQLException {
	// /**
	// * ①誕生日の入力を求める
	// */
	// BufferedReader reader = new BufferedReader(new
	// InputStreamReader(System.in));
	// String birthday = null;
	// while (true) {
	// /**
	// * 入力チェックをする。
	// * １、入力された日付が８桁以外の場合は、エラーメッセージを出力
	// * ２、正しい年月日かどうか入力チェックする。
	// */
	// try {
	// System.out.println("誕生月を入力して下さい。(例：20200709)");
	// birthday = reader.readLine();
	// // １、入力されたのが８桁かどうか
	// // ８桁以外が入力された場合→"例にの通り、８桁を入力してください。"
	// if (birthday.length() != 8) {
	// // 次の処理に行かずに次のループに入る。（初めから）
	// System.out.println("例の通り8桁を入力してください。");
	// continue;
	// }
	// // ２、正しい年月日かどうかをチェック
	// // 正しい年月日でない場合 →"正しい日付を入力してください。"→再入力を求める
	// SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	// // 日付/時刻解析を厳密に（＝存在しない日付を指定された場合、Exception を発生させること。）行うかどうかを設定する。
	// format.setLenient(false);
	// try {
	// format.parse(birthday);
	// // ループを抜ける
	// break;
	// } catch (Exception e) {
	// System.out.println("正しい日付を入力してください。");
	// continue;
	// }
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// break;
	// }
	//
	// /**
	// * ②本日を求める
	// */
	// Date date = new Date();
	// SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	// System.out.println(df.format(date.getTime()));
	//
	// /**
	// * ③過去の結果と比較する(DBを使用する)
	// * １、JDBCで繋ぐ
	// * ２、結果ファイルを１行ずつ読む。
	// * ３、一致→同じ結果を返す→終了
	// * ４、不一致→シャッフルをして結果を出す
	// */
	//
	//
	// Connection connection1 = null;
	// Statement statement1 = null;
	// ResultSet resultSet1 = null;
	//
	// try {
	// // ③−１、JDBCに接続する。
	// connection1 = connection1 =
	// DriverManager.getConnection("jdbc:postgresql://localhost:5432/omikuji",
	// "m_ochi", "mizusugatr09");
	// statement1 = connection1.createStatement();
	// String sql = "SELECT
	// results_date,omikuji_id,changer,update_date,author,create_date,birthday
	// FROM results WHERE birthday = ? AND results_date = CURRENT_DATE;";
	// //条件を動的にしてjavaで条件を自由に変更できる
	// PreparedStatement preparedStatement = connection1.prepareStatement(sql);
	// preparedStatement.setString(1,"11111111");
	// // resultsテーブルから値を取得
	// resultSet1 = preparedStatement.executeQuery();
	// //③ー２、結果ファイルを１行ずつ読む。
	// while (resultSet1.next()) {
	// // resultsテーブルを１行ずつ読み込んで出力する
	// ResultsBean resultsBean = new ResultsBean();
	//
	//// results_date = resultSet1.getInt("results_date");
	// resultsBean.setResults_date(resultSet1.getDate("results_date"));
	// resultsBean.setOmikuji_id(resultSet1.getInt("omikuji_id"));
	// resultsBean.setBirthday(resultSet1.getInt("birthday"));
	// resultsBean.setChanger(resultSet1.getString("changer"));
	// resultsBean.setUpdate_date(resultSet1.getString("update_date"));
	// resultsBean.setAuthor(resultSet1.getString("author"));
	// resultsBean.setCreate_date(resultSet1.getString("create_date"));
	// System.out.println("確認よう出力:" + resultsBean.toString());
	// }
	//// for (int m = 0; m < resultSet1.(); m++) {
	//// System.out.println(reaultsBean.getOmikuji_id());
	//// System.out.println(fortune.get(m).getFortune_id());
	//// System.out.println(fortune.get(m).getWish());
	//// System.out.println(fortune.get(m).getBusiness());
	//// System.out.println(fortune.get(m).getStudy());
	//// System.out.println(fortune.get(m).getChanger());
	//// System.out.println(fortune.get(m).getUpdate_date());
	//// System.out.println(fortune.get(m).getAuthor());
	//// System.out.println(fortune.get(m).getCreate_date());
	//// }
	// // // カンマで分割した内容を配列に格納する
	//// String[] content = resultSet1;
	//// if (content.length != 6) {
	//// System.out.println("結果ファイルが異常です！");
	//// return;
	//// }
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }
	// /**
	// *③ select文の条件付きでresultsテーブルにデータがあるかどうかを確認しに行く
	// * １、過去結果の誕生日と日付を取り出す。
	// * ２、配列の中身の0,1番目の文字列を表示する
	// */
	//// String csvbirthday = content[0];
	//// String csvtoday = content[1];
	//
	// // // ③−２、もし①誕生日と②本日の日付が一致する結果があれば同じ結果を表示して終了する
	// // if (csvbirthday.equals(birthday) && csvtoday.equals(df.format(date)))
	// // {
	// // // 同じ結果を出す
	// // String csvunsei = content[2];
	// // String csvnegaigoto = content[3];
	// // String csvakinaii = content[4];
	// // String csvgakumon = content[5];
	// // StringBuilder sb = new StringBuilder();
	// // sb.append("今日の運勢は");
	// // sb.append(csvunsei);
	// // sb.append("です");
	// // sb.append("\n 願い事：");
	// // sb.append(csvnegaigoto);
	// // sb.append("\n 商い：");
	// // sb.append(csvakinaii);
	// // sb.append("\n 学問：");
	// // sb.append(csvgakumon);
	// // System.out.println(sb.toString());
	// // return;
	// // }
	// // }
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	//
	// // ③−３、不一致の場合
	// /**
	// * ④おみくじを作成する
	// * １、OmikujiTableToReaderクラスで書き込んだメソッドを使えるようにする
	// * ２、omikujiテーブルからおみくじの中身をとる
	// * ３、listに詰める
	// */
	// // // ④−１、OmikujiTableToReaderクラスで書き込んだメソッドを使えるようにする
	// // OmikujiTableToReader omikujitabletoreader = new
	// // OmikujiTableToReader();
	//
	// // ④−２、omikujiテーブルからおみくじの中身をとる
	// // OmikujiTableToReaderクラスで作成したomikujiテーブルを１行ずつ読み込む
	// Connection connection2 = null;
	// Statement statement2 = null;
	// ResultSet resultSet2 = null;
	//
	// // // Omikujiクラスのオブジェクトを１つずつListに追加する
	// List<OmikujiBean> fortune = new ArrayList<OmikujiBean>();
	// try {
	// // OmikujiTableToReaderクラスで作成したomikujiテーブルを１行ずつ読み込む
	// // -----------------
	// // ②ー１、JDBCを使用してDBへ接続
	// // -----------------
	// // (
	// // "jdbc:postgresql://[場所(Domain)]:[ポート番号]/[DB名]",ログインロール,パスワード);
	// connection2 =
	// DriverManager.getConnection("jdbc:postgresql://localhost:5432/omikuji",
	// "m_ochi",
	// "mizusugatr09");
	// statement2 = connection2.createStatement();
	// // DBにきちんと接続できとるかを確認
	// // -----------------
	// // SQLの発行
	// // -----------------
	// // ユーザー情報のテーブル
	//
	// // OmikujiTableToReaderクラスで作成したomikujiテーブルを１行ずつ読み込む
	//// System.out.println("DB接続確認");
	//
	// // DBから値を取得
	// resultSet2 = statement2.executeQuery(
	// "SELECT f.fortune_id, f.fortune_name, f.changer, f.update_date, f.author,
	// f.create_date, o.omikuji_id, o.fortune_id, o.wish, o.business, o.study,
	// o.changer, o.update_date, o.author, o.create_date FROM fortune f LEFT
	// OUTER JOIN omikuji o ON f.fortune_id = o.fortune_id;");
	//
	// int omikuji_id = 0;
	// int fortune_id = 0;
	// String wish = null;
	// String business = null;
	// String study = null;
	// String changer = null;
	// String update_date = null;
	// // String strUpdate_date = sdf.format(update_date);
	// // Timestamp型に変換
	// String author = null;
	// String create_date = null;
	//
	// while (resultSet2.next()) {
	//
	// OmikujiBean omikuji = new OmikujiBean();
	//
	// omikuji_id = resultSet2.getInt("omikuji_id");
	// // fortune.add(Integer.toString(resultSet.getInt("omikuji_id")));
	// omikuji.setOmikuji_id(resultSet2.getInt("omikuji_id"));
	// // System.out.println("omikuji_id : " + omikuji_id);
	//
	// fortune_id = resultSet2.getInt("fortune_id");
	// // fortune.add(Integer.toString(resultSet.getInt("fortune_id")));
	// omikuji.setFortune_id(resultSet2.getInt("fortune_id"));
	// // System.out.println("fortune_id : " + fortune_id);
	//
	// wish = resultSet2.getString("wish");
	// // fortune.add(resultSet.getString("wish"));
	// omikuji.setWish(resultSet2.getString("wish"));
	// // System.out.println("wish : " + wish);
	//
	// business = resultSet2.getString("business");
	// // fortune.add(resultSet.getString("business"));
	// omikuji.setBusiness(resultSet2.getString("business"));
	// // System.out.println("business : " + business);
	//
	// study = resultSet2.getString("study");
	// // fortune.add(resultSet.getString("study"));
	// omikuji.setStudy(resultSet2.getString("study"));
	// // System.out.println("study : " + study);
	//
	// changer = resultSet2.getString("changer");
	// // fortune.add(resultSet.getString("changer"));
	// omikuji.setChanger(resultSet2.getString("changer"));
	// // System.out.println("changer : " + changer);
	//
	// update_date = resultSet2.getString("update_date");
	// // fortune.add(resultSet.getString("update_date"));
	// omikuji.setUpdate_date(resultSet2.getString("update_date"));
	// // System.out.println("update_date : " + update_date);
	//
	// author = resultSet2.getString("author");
	// // fortune.add(resultSet.getString("author"));
	// omikuji.setAuthor(resultSet2.getString("author"));
	// // System.out.println("authoru : " + author);
	//
	// create_date = resultSet2.getString("create_date");
	// // fortune.add(resultSet.getString("create_date"));
	// omikuji.setCreate_date(resultSet2.getString("create_date"));
	// // System.out.println("create_date" + create_date);
	//
	// // fortune.add(omikuji_id);
	// // System.out.println(fortune);
	// // fortune.add(Integer.toString(fortune_id));
	// // fortune.add(wish);
	// // fortune.add(business);
	// // fortune.add(study);
	// // fortune.add(changer);
	// // fortune.add(update_date);
	// // fortune.add(author);
	// // fortune.add(create_date);
	//
	// fortune.add(omikuji);
	//
	// }
	// // Listに詰めたデータを全て取得する。
	//// for (int m = 0; m < fortune.size(); m++) {
	//// System.out.println(fortune.get(m).getOmikuji_id());
	//// System.out.println(fortune.get(m).getFortune_id());
	//// System.out.println(fortune.get(m).getWish());
	//// System.out.println(fortune.get(m).getBusiness());
	//// System.out.println(fortune.get(m).getStudy());
	//// System.out.println(fortune.get(m).getChanger());
	//// System.out.println(fortune.get(m).getUpdate_date());
	//// System.out.println(fortune.get(m).getAuthor());
	//// System.out.println(fortune.get(m).getCreate_date());
	//// }
	// // // ↓これだと参照先データしか出てこないため、上のようにして出力すること
	// // System.out.println(fortune);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// // /**
	// // * ⑤おみくじを１枚引く
	// // */
	// // // Listに入れた子クラスノオブフェクトをシャッフルする
	// // // 1,shuffleで箱をシャッフルする方法
	// // Collections.shuffle(fortune);
	// // StringBuilder sb = new StringBuilder();
	// //
	// // System.out.println(fortune.get(0));
	// // /**
	// // * ⑥引いたおみくじの結果を結果ファイルに書き込む
	// // */
	// // // ※結果が出た時点で出力結果を格納している
	// // // ファイルに書き出す
	// // try {
	// // // 出力先を作成する
	// // FileWriter fw = new FileWriter("fortuneget.csv", true);
	// // PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
	// // // 内容を指定する pw.print("");←この中にfortuneget.csvへ書き込みしたい情報を格納する。
	// // pw.print(birthday);
	// // pw.print(",");
	// // pw.print(df.format(date));
	// // pw.print(",");
	// // pw.print(fortune.get(0).getUnsei());
	// // pw.print(",");
	// // pw.print(fortune.get(0).getNegaigoto());
	// // pw.print(",");
	// // pw.print(fortune.get(0).getAkinai());
	// // pw.print(",");
	// // pw.print(fortune.get(0).getGakumon());
	// // pw.print("\n");
	// // pw.close();
	// // } catch (FileNotFoundException e) {
	// // e.printStackTrace();
	// // } catch (IOException ex) {
	// // // 例外時処理
	// // ex.printStackTrace();
	// // }
	// }

}
