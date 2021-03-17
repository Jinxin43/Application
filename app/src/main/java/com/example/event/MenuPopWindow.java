package com.example.event;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.DingTu.Base.ICallback;
import com.DingTu.Base.PubVar;
import com.DingTu.Base.Tools;
import com.example.event.db.xEntity.FenAllTableEntity;
import com.example.event.db.xEntity.FenUploadEntity;
import com.example.event.db.xEntity.RoundExamineEntity;
import com.example.event.db.xEntity.UploadEntity;
import com.example.event.manager.PatrolManager;
import com.example.event.manager.UploadMananger;
import com.example.event.model.FenTableBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


class MenuPopWindow extends PopupWindow {
    private final Context mContetx;
    private final View conentView;
    private final View mExport;
    private final View mUpload;
    private final boolean mIsFirstPage;
    private boolean hasNext;
    private String mPhoto;
    private boolean hasPhoto;
    private String mVideo;
    private List<RoundExamineEntity> mlist;
    private boolean hasVideo;
    private List<FenAllTableEntity> mFenlist;
    private boolean hasTable;
    private String Id;

    public MenuPopWindow(Activity context, boolean isFirstPage) {
        this.mIsFirstPage = isFirstPage;
        this.mContetx = context;
        LayoutInflater inflater = (LayoutInflater) mContetx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.menu_popup_window, null);
        mExport = conentView.findViewById(R.id.ll_export);
        mExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Export();
                close();
            }
        });
        mUpload = conentView.findViewById(R.id.ll_upload);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
                close();
            }
        });
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 3 - 30);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

    }

    private void upload() {
        if(mIsFirstPage) {
            mlist = PatrolManager.getInstance().getExam();
            if (mlist != null && mlist.size() > 0) {
                uploadEventOneByOne(0);
            } else {
                Toast.makeText(mContetx, "当前没有调查记录！", Toast.LENGTH_SHORT).show();
            }
        }else{
            mFenlist = PatrolManager.getInstance().getfenEvent();
            if (mFenlist != null && mFenlist.size() > 0) {
                uploadFenOneByOne(0);
            } else {
                Toast.makeText(mContetx, "当前没有调查记录！", Toast.LENGTH_SHORT).show();
            }


        }

    }

    private void uploadFenOneByOne(final int index) {
        hasNext = index < mFenlist.size();
        if (hasNext) {
            final  FenAllTableEntity entity = mFenlist.get(index);
            UploadMananger.getInstance().uploadFenEvent(entity, new ICallback() {
                @Override
                public void OnClick(String Str, Object ExtraStr) {
                    if (Str.equals("success")) {
                        Toast.makeText(mContetx, "第" + (index + 1) + "条林分因子信息,已上传成功!", Toast.LENGTH_SHORT).show();
                        UploadSingleTable(0,entity, entity.getDetailJson(), index, ExtraStr.toString());
                    } else if (Str.equals("failed")) {
                        if (ExtraStr != null) {
                            List<FenUploadEntity> entitys = PatrolManager.getInstance().getFenUpload();
                            if (entitys != null && entitys.size() > 0) {
                                for (int i = 0; i < entitys.size(); i++) {
                                    if (entitys.get(i).getLandOrder().equals(entity.getLandOrder())) {
                                        Id = entitys.get(i).getMessAgeId();
                                    }
                                }
                                if(Id!=null){
                                    UploadSingleTable(0,entity, entity.getDetailJson(), index, Id);
                                }
                            }


                        } else {
                            Toast.makeText(mContetx, "第" + (index + 1) + "条林分因子信息，上传失败!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    uploadFenOneByOne(index + 1);
                }
            });
        }

    }

    private void UploadSingleTable(final int i, final FenAllTableEntity entity, final String detailJson, final int index, final String Id) {
     final List<FenTableBean>   mJson = new Gson().fromJson(detailJson, new TypeToken<List<FenTableBean>>() {}.getType());
     if(mJson!=null&&mJson.size()>0){
         hasTable= i < mJson.size();
        if(hasTable) {
            UploadMananger.getInstance().uploadSingleTable(mJson.get(i), Id, new ICallback() {
                @Override
                public void OnClick(String Str, Object ExtraStr) {
                    if (Str.equals("success")) {
                        FenUploadEntity upload = new FenUploadEntity();
                        Log.d("tag",entity.getLandOrder()+"----------"+ExtraStr.toString()+"------------"+mJson.get(i).getId());
                        upload.setLandOrder(entity.getLandOrder());
                        upload.setId(ExtraStr.toString());
                        upload.setMessAgeId(Id);
                        upload.setTreeId(mJson.get(i).getId());

                        if (PatrolManager.getInstance().saveFenUpload(upload)) {
                            Log.d("Tag", "保存成功");
                        }
                        Toast.makeText(mContetx, "第" + (index + 1) + "条林分因子,第" + (i + 1) + "条单株树调查,已上传成功!", Toast.LENGTH_SHORT).show();
                    }else if (Str.equals("failed")){
                        Toast.makeText(mContetx, "第" + (index + 1) + "条林分因子,第" + (i + 1) + "条单株树调查,上传失败!", Toast.LENGTH_SHORT).show();
                    }
                    UploadSingleTable(i+1, entity, detailJson,index,Id);
                }
            });
        }
     }




    }

//    private void UploadVideo(final int index) {
//        hasNext = index < mlist.size();
//        if(hasNext){
//            mVideo= mlist.get(index).getVideoList();
//            uploadVideo(0,mlist.get(index));
//            UploadVideo(index+1);
//        }
//
//    }

//    private void uploadVideo(final int i, final RoundExamineEntity entity) {
//        if (mVideo != null && mVideo.split(",").length > 0) {
//            hasVideo = i < mVideo.split(",").length;
//            if (hasVideo) {
//                UploadMananger.getInstance().uploadVideo(entity, entity.getVideoList().split(",")[i], new ICallback() {
//                    @Override
//                    public void OnClick(String Str, Object ExtraStr) {
//                        uploadVideo(i + 1, entity);
//                    }
//                });
//            }
//
//        }
//    }


    private void UploadPhoto(final int i, final RoundExamineEntity entity, final int index, final String data) {
        String mPhoto = entity.getPhotoList();
        if (mPhoto != null && mPhoto.split(",").length > 0) {
            hasPhoto = i < mPhoto.split(",").length;
            if (hasPhoto) {
                UploadMananger.getInstance().uploadPhotoes(entity, entity.getPhotoList().split(",")[i], data, new ICallback() {
                    @Override
                    public void OnClick(String Str, Object ExtraStr) {
                        if (Str.equals("success")) {
                            Log.d("Tag", (index + 1) + "*****" + (i + 1));
                            Toast.makeText(mContetx, "第" + (index + 1) + "条调查信息,第" + (i + 1) + "张照片,已上传成功!", Toast.LENGTH_SHORT).show();
                        } else if (Str.equals("failed")) {
                            Toast.makeText(mContetx, "第" + (index + 1) + "条调查信息,第" + (i + 1) + "张照片,上传失败!", Toast.LENGTH_SHORT).show();
                        }
                        UploadPhoto(i + 1, entity, index, data);

                    }
                });
            }

        }

    }

    private void uploadEventOneByOne(final int index) {
        hasNext = index < mlist.size();
        Log.d("Tag", mlist.size() + "***");
        if (hasNext) {
            final RoundExamineEntity entity = mlist.get(index);
            UploadMananger.getInstance().uploadEvent(entity, new ICallback() {
                @Override
                public void OnClick(String Str, Object ExtraStr) {
                    if (Str.equals("success")) {
                        UploadEntity upload = new UploadEntity();
                        upload.setOrderNumber(entity.getOrderNumber());
                        upload.setId(ExtraStr.toString());
                        if (PatrolManager.getInstance().saveUpload(upload)) {
                            Log.d("Tag", "保存成功");
                        }
                        UploadPhoto(0, entity, index, ExtraStr.toString());
                        Toast.makeText(mContetx, "第" + (index + 1) + "条调查信息,已上传成功!", Toast.LENGTH_SHORT).show();
                    } else if (Str.equals("failed")) {
                        if (ExtraStr != null) {

                            List<UploadEntity> bean = PatrolManager.getInstance().getUpload();
                            if (bean != null && bean.size() > 0) {
                                for (int i = 0; i < bean.size(); i++) {
                                    if (bean.get(i).getOrderNumber().equals(entity.getOrderNumber())) {
                                        Id = bean.get(i).getId();
                                    }
                                }
                                if(Id!=null) {
                                    UploadPhoto(0, entity, index,Id);
                                }
                            }
                        } else {
                            Toast.makeText(mContetx, "第" + (index + 1) + "条调查信息，上传失败!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    uploadEventOneByOne(index + 1);
                }
            });
        }
    }

    private void close() {
        this.dismiss();
    }

    private void Export() {

        if (mIsFirstPage) {
            try {
                List<RoundExamineEntity> mlist = PatrolManager.getInstance().getExam();
                if (mlist != null && mlist.size() > 0) {
                    if (!Tools.ExistFile(PubVar.m_SysAbsolutePath + "/Data/导出/单株调查/")) {
                        (new File(PubVar.m_SysAbsolutePath + "/Data/导出/")).mkdirs();
                    } else {
                        deleteDir(new File(PubVar.m_SysAbsolutePath + "/Data/导出/单株调查"));
                    }
                    for (int i = 0; i < mlist.size(); i++) {
                        if (!Tools.ExistFile(PubVar.m_SysAbsolutePath + "/Data/导出/单株调查/" + i + "/")) {
                            (new File(PubVar.m_SysAbsolutePath + "/Data/导出/单株调查/" + i + "/")).mkdirs();
                        }
                        InputStream inputStream = mContetx.getResources().openRawResource(R.raw.expot_table);// 将raw中的test.db放入输入流中
                        FileOutputStream fileOutputStream = new FileOutputStream(
                                PubVar.m_SysAbsolutePath + "/Data/导出/单株调查/" + i + "/"
                                        + mlist.get(i).getOrderNumber() + ".xls");// 将新的文件放入输出流中
                        byte[] buff = new byte[8192];
                        int len = 0;
                        while ((len = inputStream.read(buff)) > 0) {
                            fileOutputStream.write(buff, 0, len);
                        }
                        fileOutputStream.close();
                        inputStream.close();
                        getExcelContent(i, mlist.get(i));
                        exportPhoto(i, mlist.get(i).getPhotoList(), mlist.get(i).getPhotoOrderNum());
                    }
                    String ExportPath = PubVar.m_SysAbsolutePath + "/Data/导出/单株调查";
                    Tools.ShowMessageBox(mContetx, "数据成功导出！\r\n\r\n位于：【" + ExportPath + "】目录下");

                } else {
                    Toast.makeText(mContetx, "当前没有数据，无法导出!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Tools.ShowMessageBox("导出文件失败" + e.getMessage());
                return;
            }
        } else {

            try {
                List<FenAllTableEntity> mlist = PatrolManager.getInstance().getfenEvent();
                if (mlist != null && mlist.size() > 0) {
                    if (!Tools.ExistFile(PubVar.m_SysAbsolutePath + "/Data/导出/林分调查/")) {
                        (new File(PubVar.m_SysAbsolutePath + "/Data/导出/林分调查/")).mkdirs();
                    } else {
                        deleteDir(new File(PubVar.m_SysAbsolutePath + "/Data/导出/林分调查/"));
                    }
                    for (int i = 0; i < mlist.size(); i++) {
                        if (!Tools.ExistFile(PubVar.m_SysAbsolutePath + "/Data/导出/林分调查/" + i + "/")) {
                            (new File(PubVar.m_SysAbsolutePath + "/Data/导出/林分调查/" + i + "/")).mkdirs();
                        }
                        InputStream inputStream = mContetx.getResources().openRawResource(R.raw.lin_fen_table);// 将raw中的test.db放入输入流中
                        FileOutputStream fileOutputStream = new FileOutputStream(
                                PubVar.m_SysAbsolutePath + "/Data/导出/林分调查/" + i + "/"
                                        + mlist.get(i).getLandOrder() + ".xls");// 将新的文件放入输出流中
                        byte[] buff = new byte[8192];
                        int len = 0;
                        while ((len = inputStream.read(buff)) > 0) {
                            fileOutputStream.write(buff, 0, len);
                        }
                        fileOutputStream.close();
                        inputStream.close();
                        getLinFenExcelContent(i, mlist.get(i));
//                             exportPhoto(i, mlist.get(i).getPhotoList(), mlist.get(i).getPhotoOrderNum());
                    }
                    String ExportPath = PubVar.m_SysAbsolutePath + "/Data/导出/林分调查";
                    Tools.ShowMessageBox(mContetx, "数据成功导出！\r\n\r\n位于：【" + ExportPath + "】目录下");

                } else {
                    Toast.makeText(mContetx, "当前没有数据，无法导出!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Tools.ShowMessageBox("导出文件失败" + e.getMessage());
                return;
            }


        }

    }

    private void getLinFenExcelContent(int i, FenAllTableEntity entity) {
        try {
            Workbook wb = Workbook.getWorkbook(new File(PubVar.m_SysAbsolutePath + "/Data/导出/林分调查/" + i + "/" + entity.getLandOrder() + ".xls"));
            WritableWorkbook book = Workbook.createWorkbook(new File(PubVar.m_SysAbsolutePath + "/Data/导出/林分调查/" + i + "/" + entity.getLandOrder() + ".xls"), wb);
            WritableSheet sheet0 = book.getSheet(0);
            Label labelConte1 = new Label(11, 4, entity.getLandOrder(), getBodyCellStyle());
            sheet0.addCell(labelConte1);

            Label labelConte2 = new Label(1, 7, entity.getSheng(), getBodyCellStyle());
            sheet0.addCell(labelConte2);

            Label labelConte3 = new Label(4, 7, entity.getXian(), getBodyCellStyle());
            sheet0.addCell(labelConte3);

            Label labelConte4 = new Label(8, 7, entity.getAddress(), getBodyCellStyle());
            sheet0.addCell(labelConte4);

            Label labelConte5 = new Label(1, 9, entity.getExmainPerson(), getBodyCellStyle());
            sheet0.addCell(labelConte5);

            Label labelConte6 = new Label(4, 9, entity.getFillPerson(), getBodyCellStyle());
            sheet0.addCell(labelConte6);

            Label labelConte7 = new Label(8, 9, entity.getExamineDate(), getBodyCellStyle());
            sheet0.addCell(labelConte7);

            Label labelConte8 = new Label(1, 11, entity.getLongtitude() + "", getBodyCellStyle());
            sheet0.addCell(labelConte8);

            Label labelConte9 = new Label(4, 11, entity.getLatitude() + "", getBodyCellStyle());
            sheet0.addCell(labelConte9);

            Label labelConte10 = new Label(8, 11, entity.getHight() + "", getBodyCellStyle());
            sheet0.addCell(labelConte10);

            Label labelConte11 = new Label(1, 13, entity.getPoXiang(), getBodyCellStyle());
            sheet0.addCell(labelConte11);

            Label labelConte12 = new Label(4, 13, entity.getPoDu(), getBodyCellStyle());
            sheet0.addCell(labelConte12);

            Label labelConte13 = new Label(8, 13, entity.getPoWei(), getBodyCellStyle());
            sheet0.addCell(labelConte13);

            Label labelConte14 = new Label(1, 15, entity.getMyMZ(), getBodyCellStyle());
            sheet0.addCell(labelConte14);

            Label labelConte15 = new Label(4, 15, entity.getTuType(), getBodyCellStyle());
            sheet0.addCell(labelConte15);

            Label labelConte16 = new Label(8, 15, entity.getTreeType(), getBodyCellStyle());
            sheet0.addCell(labelConte16);

            Label labelConte17 = new Label(1, 17, entity.getArea(), getBodyCellStyle());
            sheet0.addCell(labelConte17);

            Label labelConte18 = new Label(4, 17, entity.getTreeName(), getBodyCellStyle());
            sheet0.addCell(labelConte18);

            Label labelConte19 = new Label(8, 17, entity.getLinAge(), getBodyCellStyle());
            sheet0.addCell(labelConte19);

            Label labelConte20 = new Label(1, 19, entity.getZhiHigh() + "", getBodyCellStyle());
            sheet0.addCell(labelConte20);

            Label labelConte21 = new Label(4, 19, entity.getAvGuanFu(), getBodyCellStyle());
            sheet0.addCell(labelConte21);

            Label labelConte22 = new Label(8, 19, entity.getAvXiongJin(), getBodyCellStyle());
            sheet0.addCell(labelConte22);

            Label labelConte23 = new Label(1, 21, entity.getAvTreeHigh(), getBodyCellStyle());
            sheet0.addCell(labelConte23);

            Label labelConte24 = new Label(4, 21, entity.getYuBiDu() + "", getBodyCellStyle());
            sheet0.addCell(labelConte24);

            Label labelConte25 = new Label(8, 21, entity.getMiDu(), getBodyCellStyle());
            sheet0.addCell(labelConte25);

            Label labelConte26 = new Label(1, 23, entity.getLinFenMainJi(), getBodyCellStyle());
            sheet0.addCell(labelConte26);

            Label labelConte27 = new Label(4, 23, entity.getAvXuji(), getBodyCellStyle());
            sheet0.addCell(labelConte27);
            Label labelConte28 = new Label(8, 23, entity.getQiYuan(), getBodyCellStyle());
            sheet0.addCell(labelConte28);


            Label labelConte29 = new Label(1, 25, entity.getLinZhongYuan(), getBodyCellStyle());
            sheet0.addCell(labelConte29);

            Label labelConte30 = new Label(7, 25, entity.getShuZhong(), getBodyCellStyle());
            sheet0.addCell(labelConte30);


            Label labelConte31 = new Label(1, 27, entity.getHealth(), getBodyCellStyle());
            sheet0.addCell(labelConte31);

            Label labelConte32 = new Label(7, 27, entity.getJieShi(), getBodyCellStyle());
            sheet0.addCell(labelConte32);

            List<FenTableBean> mJson = new Gson().fromJson(entity.getDetailJson(), new TypeToken<List<FenTableBean>>() {
            }.getType());
            if (mJson != null && mJson.size() > 0) {
                for (int j = 0; j < mJson.size(); j++) {
                    Label labelContent0 = new Label(0, 32 + j, mJson.get(j).getId(), getBodyCellStyle());
                    sheet0.addCell(labelContent0);
                    Label labelContent1 = new Label(1, 32 + j, mJson.get(j).getXiongjin(), getBodyCellStyle());
                    sheet0.addCell(labelContent1);
                    Label labelContent2 = new Label(2, 32 + j, mJson.get(j).getTreeHigh(), getBodyCellStyle());
                    sheet0.addCell(labelContent2);
                    Label labelContent3 = new Label(3, 32 + j, mJson.get(j).getGuanFu(), getBodyCellStyle());
                    sheet0.addCell(labelContent3);
                    Label labelContent4 = new Label(4, 32 + j, mJson.get(j).getShuXing(), getBodyCellStyle());
                    sheet0.addCell(labelContent4);
                    Label labelContent5 = new Label(5, 32 + j, mJson.get(j).getFlowerDate(), getBodyCellStyle());
                    sheet0.mergeCells(5, 32 + j, 6, 32 + j);
                    sheet0.addCell(labelContent5);
                    Label labelContent6 = new Label(7, 32 + j, mJson.get(j).getDaiWeiMianJi(), getBodyCellStyle());
                    sheet0.addCell(labelContent6);
                    Label labelContent7 = new Label(8, 32 + j, mJson.get(j).getStartLevel(), getBodyCellStyle());
                    sheet0.mergeCells(8, 32 + j, 11, 32 + j);
                    sheet0.addCell(labelContent7);
                }
            }


            book.write();
            book.close();
            wb.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.ShowMessageBox("导出表格失败" + ex.getMessage());
        }

    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    private void exportPhoto(int i, String photolist, String orderlist) {
        if (photolist != null && !TextUtils.isEmpty(photolist) && orderlist != null && !TextUtils.isEmpty(orderlist)) {
            String[] mPhoto = photolist.split(",");
            String[] mOderlist = orderlist.split(",");
            for (int j = 0; j < mPhoto.length; j++) {
                String oldPath = PubVar.m_SysAbsolutePath + "/Photo/" + mPhoto[j];
                String newFileName = PubVar.m_SysAbsolutePath + "/Data/导出/单株调查/" + i + "/" + mOderlist[j] + ".jpg";
                CopyFile(oldPath, newFileName);
            }
        }
    }


    private int CopyFile(String fromFile, String toFile) {
        try {
            File dest = new File(toFile);
            if (dest.exists()) {
                dest.delete();
            }
            dest.createNewFile();

            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(dest);
            int size = fosfrom.available();
            byte bt[] = new byte[size];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            Log.e("CopyFile", ex.getMessage());
            // Tools.ShowMessageBox(ex.getMessage());
        }

        return -1;
    }


    private void getExcelContent(int i, RoundExamineEntity entity) {
        try {
            Workbook wb = Workbook.getWorkbook(new File(PubVar.m_SysAbsolutePath + "/Data/导出/单株调查/" + i + "/" + entity.getOrderNumber() + ".xls"));
            WritableWorkbook book = Workbook.createWorkbook(new File(PubVar.m_SysAbsolutePath + "/Data/导出/单株调查/" + i + "/" + entity.getOrderNumber() + ".xls"), wb);
            WritableSheet sheet0 = book.getSheet(0);
            Label labelConte1 = new Label(11, 4, entity.getOrderNumber(), getBodyCellStyle());
            sheet0.addCell(labelConte1);

            Label labelConte2 = new Label(1, 5, entity.getXian(), getBodyCellStyle());
            sheet0.addCell(labelConte2);

            Label labelConte3 = new Label(5, 5, entity.getSheng(), getBodyCellStyle());
            sheet0.addCell(labelConte3);

            Label labelConte4 = new Label(9, 5, entity.getAddress(), getBodyCellStyle());
            sheet0.addCell(labelConte4);

            Label labelConte5 = new Label(1, 7, entity.getExmainPerson(), getBodyCellStyle());
            sheet0.addCell(labelConte5);

            Label labelConte6 = new Label(5, 7, entity.getFillPerson(), getBodyCellStyle());
            sheet0.addCell(labelConte6);

            Label labelConte7 = new Label(9, 7, entity.getExamineDate(), getBodyCellStyle());
            sheet0.addCell(labelConte7);

            Label labelConte8 = new Label(1, 9, entity.getZhongCName(), getBodyCellStyle());
            sheet0.addCell(labelConte8);

            Label labelConte9 = new Label(5, 9, entity.getShuCName(), getBodyCellStyle());
            sheet0.addCell(labelConte9);

            Label labelConte10 = new Label(9, 9, entity.getKeCName(), getBodyCellStyle());
            sheet0.addCell(labelConte10);

            Label labelConte11 = new Label(1, 11, entity.getZhongLaName(), getBodyCellStyle());
            sheet0.addCell(labelConte11);

            Label labelConte12 = new Label(5, 11, entity.getShuLaName(), getBodyCellStyle());
            sheet0.addCell(labelConte12);

            Label labelConte13 = new Label(9, 11, entity.getKeLaName(), getBodyCellStyle());
            sheet0.addCell(labelConte13);

            Label labelConte14 = new Label(1, 13, entity.getLatitude() + "", getBodyCellStyle());
            sheet0.addCell(labelConte14);

            Label labelConte15 = new Label(5, 13, entity.getLongtitude() + "", getBodyCellStyle());
            sheet0.addCell(labelConte15);

            Label labelConte16 = new Label(9, 13, entity.getHight() + "", getBodyCellStyle());
            sheet0.addCell(labelConte16);

            Label labelConte17 = new Label(1, 15, entity.getPoXiang(), getBodyCellStyle());
            sheet0.addCell(labelConte17);

            Label labelConte18 = new Label(5, 15, entity.getPoWei(), getBodyCellStyle());
            sheet0.addCell(labelConte18);

            Label labelConte19 = new Label(9, 15, entity.getPoDu(), getBodyCellStyle());
            sheet0.addCell(labelConte19);

            Label labelConte20 = new Label(1, 17, entity.getTreeHight() + "", getBodyCellStyle());
            sheet0.addCell(labelConte20);

            Label labelConte21 = new Label(5, 17, entity.getXiongJin() + "", getBodyCellStyle());
            sheet0.addCell(labelConte21);

            Label labelConte22 = new Label(9, 17, entity.getGuanFu() + "", getBodyCellStyle());
            sheet0.addCell(labelConte22);

            Label labelConte23 = new Label(1, 19, entity.getZhiHight() + "", getBodyCellStyle());
            sheet0.addCell(labelConte23);

            Label labelConte24 = new Label(5, 19, entity.getXuji() + "", getBodyCellStyle());
            sheet0.addCell(labelConte24);

            Label labelConte25 = new Label(9, 19, entity.getTuType(), getBodyCellStyle());
            sheet0.addCell(labelConte25);

            Label labelConte26 = new Label(1, 21, entity.getImportDescribe(), getBodyCellStyle());
            sheet0.addCell(labelConte26);

            Label labelConte27 = new Label(1, 25, entity.getPhotoOrderNum(), getBodyCellStyle());
            sheet0.addCell(labelConte27);
            Label labelConte28 = new Label(5, 25, entity.getTakePerson(), getBodyCellStyle());
            sheet0.addCell(labelConte28);
            Label labelConte29 = new Label(9, 25, entity.getTakeDate(), getBodyCellStyle());
            sheet0.addCell(labelConte29);

            Label labelConte30 = new Label(1, 27, entity.getStateDescribe(), getBodyCellStyle());
            sheet0.addCell(labelConte30);

            book.write();
            book.close();
            wb.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.ShowMessageBox("导出表格失败" + ex.getMessage());
        }

    }


    public WritableCellFormat getBodyCellStyle() {

        /*
         * WritableFont.createFont("宋体")：设置字体为宋体 10：设置字体大小
         * WritableFont.BOLD:设置字体加粗（BOLD：加粗 NO_BOLD：不加粗） false：设置非斜体
         * UnderlineStyle.NO_UNDERLINE：没有下划线
         */
        WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE);

        WritableCellFormat headerFormat = new WritableCellFormat(NumberFormats.TEXT);
        try {
            // 添加字体设置
            headerFormat.setFont(font);
            // 设置表头表格边框样式
            // 整个表格线为粗线、黑色
            headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            // 表头内容水平居中显示
            headerFormat.setAlignment(Alignment.CENTRE);
            headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        } catch (WriteException e) {
            System.out.println("内容单元格样式设置失败！");
        }
        return headerFormat;
    }


    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

}
