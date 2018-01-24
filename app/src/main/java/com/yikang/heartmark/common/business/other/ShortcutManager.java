//package com.yikang.heartmark.common.business.other;
//
//import android.database.Cursor;
//
//import com.kanebay.lepu.askdr.common.db.DbQueryRunner;
//import com.kanebay.lepu.askdr.common.model.ShortCut;
//import com.kanebay.lepu.askdr.common.model.ShortCutCategory;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//
///**
// * Created by guolchen on 2015/1/30.
// */
//public class ShortcutManager {
//    private static Logger logger = LoggerFactory.getLogger(ShortcutManager.class);
//
//    static class ShortcutManagerHolder {
//        static ShortcutManager shortcutManager = new ShortcutManager();
//    }
//
//    public static ShortcutManager getInstance() {
//        return ShortcutManagerHolder.shortcutManager;
//    }
//
//    public List<ShortCut> getShortcuts(){
//        try {
//            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
//            /*String sql = "SELECT sessionId, userProfilePictureId,hostUserId, userId,userNickName,gender,\n" +
//                    "newMsgCount,lastMsgContent,lastMsgTime from chat_Session WHERE hostUserId=?";*/
//            String sql = "SELECT sc.ShortcutCode, sc.ShortcutCategoryCode, sc.Sentence, scc.CategoryName from Shortcut sc, ShortcutCategory scc where sc.ShortcutCategoryCode = scc.CategoryCode";
//
//            String[] params = new String[]{};
//            List<ShortCut> shortCuts = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
//                @Override
//                public Object handle(Cursor c) {
//                    ShortCut shortCut = new ShortCut();
//
//                    shortCut.setShortcutCode(c.getString(c.getColumnIndex("ShortcutCode")));
//                    shortCut.setShortcutCategoryCode(c.getString(c.getColumnIndex("ShortcutCategoryCode")));
//                    shortCut.setCategoryName(c.getString(c.getColumnIndex("CategoryName")));
//                    shortCut.setSentence(c.getString(c.getColumnIndex("Sentence")));
//                    return shortCut;
//                }
//            }, params);
//            if(null != shortCuts && shortCuts.size() > 0){
//                return shortCuts;
//            }
//            return null;
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//        return null;
//    }
//
//    public List<ShortCutCategory> getShortcutCategories(){
//        try {
//            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
//            String sql = "SELECT * from ShortcutCategory";
//
//            String[] params = new String[]{};
//            List<ShortCutCategory> shortCutCategories = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
//                @Override
//                public Object handle(Cursor c) {
//                    ShortCutCategory shortCutCategory = new ShortCutCategory();
//                    shortCutCategory.setShortcutCategoryCode(c.getString(c.getColumnIndex("CategoryCode")));
//                    shortCutCategory.setCategoryName(c.getString(c.getColumnIndex("CategoryName")));
//                    return shortCutCategory;
//                }
//            }, params);
//
//            return shortCutCategories;
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//        return null;
//    }
//
//    public List<ShortCut> getShortcutsByCategory(String categoryCode){
//        try {
//            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
//            String sql = "SELECT * from Shortcut where ShortcutCategoryCode = ?";
//
//            String[] params = new String[]{categoryCode};
//            List<ShortCut> shortCuts = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
//                @Override
//                public Object handle(Cursor c) {
//                    ShortCut shortCut = new ShortCut();
//                    shortCut.setShortcutCode(c.getString(c.getColumnIndex("ShortcutCode")));
//                    shortCut.setShortcutCategoryCode(c.getString(c.getColumnIndex("ShortcutCategoryCode")));
//                    shortCut.setSentence(c.getString(c.getColumnIndex("Sentence")));
//                    return shortCut;
//                }
//            }, params);
//            if(null != shortCuts && shortCuts.size() > 0){
//                return shortCuts;
//            }
//            return null;
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//        return null;
//    }
//}
