package eu.motogymkhana.competition.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.model.Credential;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.settings.Settings;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class GymkhanaDatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = Constants.DATABASE_NAME;
	private static final int DATABASE_VERSION = Constants.DATABASE_VERSION;
	private static final String TAG = GymkhanaDatabaseHelper.class.getSimpleName();

	private final String LOGTAG = getClass().getSimpleName();
	private final Collection<Class<?>> classList = new LinkedList<Class<?>>();

	private static GymkhanaDatabaseHelper instance;

	public GymkhanaDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		instance = this;

		try {
			classList.add(Rider.class);
			classList.add(Round.class);
			classList.add(Times.class);
			classList.add(Credential.class);
			classList.add(Settings.class);

			for (Class<?> clazz : classList) {
				DaoManager.createDao(getConnectionSource(), clazz);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		Log.i(LOGTAG, "onCreate");
		createTables();
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		Log.i(LOGTAG, "onUpgrade");
		dropTables();
		createTables();
	}

	public Dao<?, ?> getDAO(Class<?> type) throws SQLException {
		return DaoManager.createDao(connectionSource, type);
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
	}

	private void createTables() {

		try {
			for (Class<?> clazz : classList) {
				TableUtils.createTable(getConnectionSource(), clazz);
			}
		} catch (SQLException e) {
			Log.e(LOGTAG, "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	private void dropTables() {

		try {
			for (Class<?> clazz : classList) {
				TableUtils.dropTable(getConnectionSource(), clazz, true);
			}
		} catch (SQLException e) {
			Log.e(LOGTAG, "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	public static GymkhanaDatabaseHelper getInstance() {
		return instance;
	}
}