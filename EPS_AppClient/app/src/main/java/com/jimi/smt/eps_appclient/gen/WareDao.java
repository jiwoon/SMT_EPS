package com.jimi.smt.eps_appclient.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.jimi.smt.eps_appclient.Dao.Ware;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WARE".
*/
public class WareDao extends AbstractDao<Ware, Long> {

    public static final String TABLENAME = "WARE";

    /**
     * Properties of entity Ware.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Ware_id = new Property(0, Long.class, "ware_id", true, "_id");
        public final static Property Order = new Property(1, String.class, "order", false, "ORDER");
        public final static Property Operator = new Property(2, String.class, "operator", false, "OPERATOR");
        public final static Property Board_type = new Property(3, int.class, "board_type", false, "BOARD_TYPE");
        public final static Property Line = new Property(4, String.class, "line", false, "LINE");
        public final static Property SerialNo = new Property(5, int.class, "SerialNo", false, "SERIAL_NO");
        public final static Property Alternative = new Property(6, Byte.class, "Alternative", false, "ALTERNATIVE");
        public final static Property OrgLineSeat = new Property(7, String.class, "OrgLineSeat", false, "ORG_LINE_SEAT");
        public final static Property OrgMaterial = new Property(8, String.class, "OrgMaterial", false, "ORG_MATERIAL");
        public final static Property ScanLineSeat = new Property(9, String.class, "ScanLineSeat", false, "SCAN_LINE_SEAT");
        public final static Property ScanMaterial = new Property(10, String.class, "ScanMaterial", false, "SCAN_MATERIAL");
        public final static Property Result = new Property(11, String.class, "Result", false, "RESULT");
        public final static Property Remark = new Property(12, String.class, "Remark", false, "REMARK");
    }


    public WareDao(DaoConfig config) {
        super(config);
    }
    
    public WareDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WARE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ware_id
                "\"ORDER\" TEXT," + // 1: order
                "\"OPERATOR\" TEXT," + // 2: operator
                "\"BOARD_TYPE\" INTEGER NOT NULL ," + // 3: board_type
                "\"LINE\" TEXT," + // 4: line
                "\"SERIAL_NO\" INTEGER NOT NULL ," + // 5: SerialNo
                "\"ALTERNATIVE\" INTEGER," + // 6: Alternative
                "\"ORG_LINE_SEAT\" TEXT," + // 7: OrgLineSeat
                "\"ORG_MATERIAL\" TEXT," + // 8: OrgMaterial
                "\"SCAN_LINE_SEAT\" TEXT," + // 9: ScanLineSeat
                "\"SCAN_MATERIAL\" TEXT," + // 10: ScanMaterial
                "\"RESULT\" TEXT," + // 11: Result
                "\"REMARK\" TEXT);"); // 12: Remark
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WARE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Ware entity) {
        stmt.clearBindings();
 
        Long ware_id = entity.getWare_id();
        if (ware_id != null) {
            stmt.bindLong(1, ware_id);
        }
 
        String order = entity.getOrder();
        if (order != null) {
            stmt.bindString(2, order);
        }
 
        String operator = entity.getOperator();
        if (operator != null) {
            stmt.bindString(3, operator);
        }
        stmt.bindLong(4, entity.getBoard_type());
 
        String line = entity.getLine();
        if (line != null) {
            stmt.bindString(5, line);
        }
        stmt.bindLong(6, entity.getSerialNo());
 
        Byte Alternative = entity.getAlternative();
        if (Alternative != null) {
            stmt.bindLong(7, Alternative);
        }
 
        String OrgLineSeat = entity.getOrgLineSeat();
        if (OrgLineSeat != null) {
            stmt.bindString(8, OrgLineSeat);
        }
 
        String OrgMaterial = entity.getOrgMaterial();
        if (OrgMaterial != null) {
            stmt.bindString(9, OrgMaterial);
        }
 
        String ScanLineSeat = entity.getScanLineSeat();
        if (ScanLineSeat != null) {
            stmt.bindString(10, ScanLineSeat);
        }
 
        String ScanMaterial = entity.getScanMaterial();
        if (ScanMaterial != null) {
            stmt.bindString(11, ScanMaterial);
        }
 
        String Result = entity.getResult();
        if (Result != null) {
            stmt.bindString(12, Result);
        }
 
        String Remark = entity.getRemark();
        if (Remark != null) {
            stmt.bindString(13, Remark);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Ware entity) {
        stmt.clearBindings();
 
        Long ware_id = entity.getWare_id();
        if (ware_id != null) {
            stmt.bindLong(1, ware_id);
        }
 
        String order = entity.getOrder();
        if (order != null) {
            stmt.bindString(2, order);
        }
 
        String operator = entity.getOperator();
        if (operator != null) {
            stmt.bindString(3, operator);
        }
        stmt.bindLong(4, entity.getBoard_type());
 
        String line = entity.getLine();
        if (line != null) {
            stmt.bindString(5, line);
        }
        stmt.bindLong(6, entity.getSerialNo());
 
        Byte Alternative = entity.getAlternative();
        if (Alternative != null) {
            stmt.bindLong(7, Alternative);
        }
 
        String OrgLineSeat = entity.getOrgLineSeat();
        if (OrgLineSeat != null) {
            stmt.bindString(8, OrgLineSeat);
        }
 
        String OrgMaterial = entity.getOrgMaterial();
        if (OrgMaterial != null) {
            stmt.bindString(9, OrgMaterial);
        }
 
        String ScanLineSeat = entity.getScanLineSeat();
        if (ScanLineSeat != null) {
            stmt.bindString(10, ScanLineSeat);
        }
 
        String ScanMaterial = entity.getScanMaterial();
        if (ScanMaterial != null) {
            stmt.bindString(11, ScanMaterial);
        }
 
        String Result = entity.getResult();
        if (Result != null) {
            stmt.bindString(12, Result);
        }
 
        String Remark = entity.getRemark();
        if (Remark != null) {
            stmt.bindString(13, Remark);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Ware readEntity(Cursor cursor, int offset) {
        Ware entity = new Ware( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ware_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // order
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // operator
            cursor.getInt(offset + 3), // board_type
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // line
            cursor.getInt(offset + 5), // SerialNo
            cursor.isNull(offset + 6) ? null : (byte) cursor.getShort(offset + 6), // Alternative
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // OrgLineSeat
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // OrgMaterial
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // ScanLineSeat
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // ScanMaterial
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // Result
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // Remark
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Ware entity, int offset) {
        entity.setWare_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOrder(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setOperator(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBoard_type(cursor.getInt(offset + 3));
        entity.setLine(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSerialNo(cursor.getInt(offset + 5));
        entity.setAlternative(cursor.isNull(offset + 6) ? null : (byte) cursor.getShort(offset + 6));
        entity.setOrgLineSeat(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setOrgMaterial(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setScanLineSeat(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setScanMaterial(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setResult(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setRemark(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Ware entity, long rowId) {
        entity.setWare_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Ware entity) {
        if(entity != null) {
            return entity.getWare_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Ware entity) {
        return entity.getWare_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
