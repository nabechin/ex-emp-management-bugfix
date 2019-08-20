package jp.co.sample.emp_management.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.sample.emp_management.domain.Administrator;

/**
 * administratorsテーブルを操作するリポジトリ.
 * 
 * @author igamasayuki
 * 
 */
@Repository
public class AdministratorRepository {

	/**
	 * Administratorオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Administrator> ADMINISTRATOR_ROW_MAPPER = (rs, i) -> {
		Administrator administrator = new Administrator();
		administrator.setId(rs.getInt("id"));
		administrator.setName(rs.getString("name"));
		administrator.setMailAddress(rs.getString("mail_address"));
		administrator.setPassword(rs.getString("password"));
		return administrator;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * メールアドレスとパスワードから管理者情報を取得します.
	 * 
	 * @param mailAddress メールアドレス
	 * @param passward    パスワード
	 * @return 管理者情報 存在しない場合はnullを返します
	 */
	public Administrator findByMailAddressAndPassward(String mailAddress, String password) {
		String sql = "select id,name,mail_address,password from administrators where mail_address= :mailAddress and password= :password ";
		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress).addValue("password",password);
		List<Administrator> administratorList = template.query(sql, param, ADMINISTRATOR_ROW_MAPPER);
		if (administratorList.size() == 0) {
			return null;
		}
		return administratorList.get(0);
	}

	/**
	 * 管理者情報を挿入します.
	 * 
	 * @param administrator 管理者情報
	 */
	public void insert(Administrator administrator) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(administrator);
		String sql = "insert into administrators(name,mail_address,password)values(:name,:mailAddress,:password);";
		template.update(sql, param);
	}

	/**
	 * メールアドレスから管理者情報を取得します.
	 * 
	 * @param mailAddress メールアドレス
	 * @return 管理者情報 存在しない場合はnullを返します
	 */
	public Administrator findByMailAddress(String mailAddress) {
		String sql = "select id,name,mail_address,password from administrators where mail_address=:mailAddress";
		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress);
		List<Administrator> administratorList = template.query(sql, param, ADMINISTRATOR_ROW_MAPPER);
		if (administratorList.size() == 0) {
			return null;
		}
		return administratorList.get(0);
	}

}
