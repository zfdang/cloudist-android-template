package cc.cloudist.app.android.template.data.local.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.app.android.template.data.local.database.DaoSession;
import cc.cloudist.app.android.template.data.local.database.entity.Image;
import cc.cloudist.app.android.template.data.local.database.entity.Story;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "IMAGES".
*/
public class ImageDao extends AbstractDao<Image, Long> {

    public static final String TABLENAME = "IMAGES";

    /**
     * Properties of entity Image.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Path = new Property(1, String.class, "path", false, "PATH");
        public final static Property StoryId = new Property(2, long.class, "storyId", false, "STORY_ID");
    };

    private DaoSession daoSession;

    private Query<Image> story_ImagesQuery;

    public ImageDao(DaoConfig config) {
        super(config);
    }
    
    public ImageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"IMAGES\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"PATH\" TEXT," + // 1: path
                "\"STORY_ID\" INTEGER NOT NULL );"); // 2: storyId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"IMAGES\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Image entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(2, path);
        }
        stmt.bindLong(3, entity.getStoryId());
    }

    @Override
    protected void attachEntity(Image entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Image readEntity(Cursor cursor, int offset) {
        Image entity = new Image( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // path
            cursor.getLong(offset + 2) // storyId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Image entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPath(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setStoryId(cursor.getLong(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Image entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Image entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "images" to-many relationship of Story. */
    public List<Image> _queryStory_Images(long storyId) {
        synchronized (this) {
            if (story_ImagesQuery == null) {
                QueryBuilder<Image> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.StoryId.eq(null));
                story_ImagesQuery = queryBuilder.build();
            }
        }
        Query<Image> query = story_ImagesQuery.forCurrentThread();
        query.setParameter(0, storyId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getStoryDao().getAllColumns());
            builder.append(" FROM IMAGES T");
            builder.append(" LEFT JOIN STORY T0 ON T.\"STORY_ID\"=T0.\"ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Image loadCurrentDeep(Cursor cursor, boolean lock) {
        Image entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Story story = loadCurrentOther(daoSession.getStoryDao(), cursor, offset);
         if(story != null) {
            entity.setStory(story);
        }

        return entity;    
    }

    public Image loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Image> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Image> list = new ArrayList<Image>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Image> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Image> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}