package com.jingkai.asset.function.repair.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseRecyclerViewAdapter;
import com.jingkai.asset.base.BaseViewHolder;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.function.repair.entity.FileBean;
import com.jingkai.asset.function.repair.entity.RepairDetailBean;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.MeasureRecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/9 16:31
 * Description:修缮审核过程适配器
 */
public class RepairDetailProcessAdapter<T extends RepairDetailBean.ProcessBean> extends BaseRecyclerViewAdapter<T> {


    public RepairDetailProcessAdapter(List<T> list, Context context) {
        super(list, context);

    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_repair_audit_detail, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends BaseViewHolder<T> {
        //操作
        @Bind(R.id.tv_item_repair_audit_detail_stage)
        TextView mTvStage;
        //操作人
        @Bind(R.id.tv_item_repair_audit_detail_audiApplyPerson)
        TextView mTvAuditApplyPerson;
        //操作时间
        @Bind(R.id.tv_item_repair_audit_detail_commitApplyTime)
        TextView mTvCommitApplyTime;
        //详情描述或反馈意见标题
        @Bind(R.id.tv_item_repair_audit_detail_remark_title)
        TextView mTvRemarkTitle;
        //详情描述或反馈意见
        @Bind(R.id.tv_item_repair_audit_detail_remark)
        TextView mTvRemark;
        //附件标题
        @Bind(R.id.tv_item_repair_audit_detail_file_title)
        TextView mTvFileTitle;
        //附件列表
        @Bind(R.id.mrv_item_repair_audit_detail_file)
        MeasureRecyclerView mMrvFile;

        //结算资料标题
        @Bind(R.id.tv_item_repair_audit_detail_file_settle_title)
        TextView mTvItemRepairAuditDetailFileSettleTitle;
        //结算资料文件
        @Bind(R.id.mrv_item_repair_audit_detail_file_settle)
        MeasureRecyclerView mMrvItemRepairAuditDetailFileSettle;

        private ViewUtil viewUtil;


        public MyViewHolder(View itemView) {
            super(itemView);
            viewUtil = ViewUtil.getViewUtil();
        }

        @Override
        protected void bind(final T bean, int position) {
            /**
             * status
             * 工单状态
             * 10 新建保存
             * 20 新建提交
             * 25 储备转当年
             * 26 储备转废弃
             * 30  报审保存
             * 40  报审提交
             * 50  验收通过
             * 60 验收不通
             * 70  终审通过
             * 80 终审不通过
             */

            /**
             * 1. 关于附件显示问题：
             * 修缮：
             * (1) 新建：有
             * (2) 报审：有
             * (3) 预审：有（预审通过）；没有（预审驳回）
             * (4) 终审：没有（终审通过）；没有（终审驳回）
             * */
            boolean canShowFile = true;//是否需要展示附件
            boolean canShowRemark = true;//是否需要展示备注信息
            String statusType = bean.getStatusType();
            if (TextUtils.isEmpty(statusType)) statusType = "报审提交";
            switch (statusType) {
                case "新建保存(APP)":
                case "新建提交(APP)":
                case "新建保存":
                case "新建提交":
                    canShowRemark = false;
                    break;
                case "储备转当年":
                case "转当年":
                case "列入计划":
                case "储备转当年(APP)":
                case "转当年(APP)":
                case "列入计划(APP)":
                    canShowRemark = false;
                    canShowFile = false;
                    break;
                case "储备转废弃":
                case "报审保存":
                case "报审提交":
                case "储备转废弃(APP)":
                case "报审保存(APP)":
                case "报审提交(APP)":
//                    mTvRemarkTitle.setText("报审详情: ");
                    canShowRemark = false;
                    break;
                case "验收通过":
                case "预审通过":
                case "验收通过(APP)":
                case "预审通过(APP)":
//                    mTvRemarkTitle.setText("预审意见: ");
                    canShowRemark = false;
                    break;
                case "预审驳回":
                case "验收不通过":
                case "预审驳回(APP)":
                case "验收不通过(APP)":
//                    mTvRemarkTitle.setText("预审意见: ");
                    canShowFile = false;
                    canShowRemark = false;
                    break;
                case "终审通过(APP)":
                case "终审不通过(APP)":
                case "终审驳回(APP)":
                case "终审通过":
                case "终审不通过":
                case "终审驳回":
//                    mTvRemarkTitle.setText("终审意见: ");
                    viewUtil.setDifferentColorText("终审意见: ","",mTvRemarkTitle);
                    canShowFile = false;
                    break;
                default:
//                    mTvRemarkTitle.setText("报审详情: ");
                    canShowRemark = false;
                    break;
            }

            viewUtil.setDifferentColorText("操作时间:", bean.getGmtModifiedTime(), mTvCommitApplyTime);
            viewUtil.setDifferentColorText("操作人:", bean.getEditor(), mTvAuditApplyPerson);
            viewUtil.setDifferentColorText("操\u3000作:", statusType, mTvStage);

            //判断是否需要展示备注信息
            if (canShowRemark) {
                mTvRemarkTitle.setVisibility(View.VISIBLE);
                mTvRemark.setVisibility(View.VISIBLE);
                String notes = TextUtils.isEmpty(bean.getNotes()) ? "无":bean.getNotes();
                mTvRemark.setText("\u3000\u3000" + notes);
            } else {
                mTvRemarkTitle.setVisibility(View.GONE);
                mTvRemark.setVisibility(View.GONE);
            }

            //如果需要展示附件
            if (canShowFile) {
                mTvFileTitle.setVisibility(View.VISIBLE);
                mMrvFile.setVisibility(View.VISIBLE);
                if(TextUtils.equals("报审提交",statusType)){
                    mTvItemRepairAuditDetailFileSettleTitle.setVisibility(View.VISIBLE);
                    mMrvItemRepairAuditDetailFileSettle.setVisibility(View.VISIBLE);
                }else {
                    mTvItemRepairAuditDetailFileSettleTitle.setVisibility(View.GONE);
                    mMrvItemRepairAuditDetailFileSettle.setVisibility(View.GONE);
                }

                loadFileAdapter(bean);
            } else {
                mTvFileTitle.setVisibility(View.GONE);
                mTvItemRepairAuditDetailFileSettleTitle.setVisibility(View.GONE);
                mMrvFile.setVisibility(View.GONE);
                mMrvItemRepairAuditDetailFileSettle.setVisibility(View.GONE);
            }

        }

        /**
         * 加载附件列表
         *
         * @param bean
         */
        private void loadFileAdapter(RepairDetailBean.ProcessBean bean) {


            if (TextUtils.equals("报审提交", bean.getStatusType())) {
                List<FileBean> fileConstr = bean.getFileConstr();
                List<FileBean> fileSettle = bean.getFileSettle();
                loadFilesOf(fileConstr,fileSettle);
            } else {
                //非报审提交
                List<FileBean> fileList = bean.getFile();
                loadFiles(fileList);
            }
        }

        //非报审提交显示附件
        private void loadFiles(final List<FileBean> fileList) {
            if (fileList != null && fileList.size() > 0) {
                AuditProcessDetailFileAdapter adapter = new AuditProcessDetailFileAdapter(fileList, context);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                mMrvFile.setLayoutManager(layoutManager);
                mMrvFile.setAdapter(adapter);

                adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FileBean fileBean = fileList.get(position);
                        if (openFileListener != null) {
                            openFileListener.openFile(fileBean);
                        }
                    }
                });
                viewUtil.setDifferentColorText("附件:","",mTvFileTitle);
            } else {
                viewUtil.setDifferentColorText("附件: ","无",mTvFileTitle);
            }
        }

        //报审提交显示附件
        private void loadFilesOf(final  List<FileBean> fileConstr,final  List<FileBean> fileSettle) {
            //施工资料
            if (fileConstr != null && fileConstr.size() > 0) {
                AuditProcessDetailFileAdapter adapter = new AuditProcessDetailFileAdapter(fileConstr, context);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                mMrvFile.setLayoutManager(layoutManager);
                mMrvFile.setAdapter(adapter);

                adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FileBean fileBean = fileConstr.get(position);
                        if (openFileListener != null) {
                            openFileListener.openFile(fileBean);
                        }
                    }
                });
                viewUtil.setDifferentColorText("施工资料:","",mTvFileTitle);
            } else {
                viewUtil.setDifferentColorText("施工资料:","无",mTvFileTitle);
            }

            //结算资料
            if (fileSettle != null && fileSettle.size() > 0) {
                AuditProcessDetailFileAdapter adapter = new AuditProcessDetailFileAdapter(fileSettle, context);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                mMrvItemRepairAuditDetailFileSettle.setLayoutManager(layoutManager);
                mMrvItemRepairAuditDetailFileSettle.setAdapter(adapter);

                adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FileBean fileBean = fileSettle.get(position);
                        if (openFileListener != null) {
                            openFileListener.openFile(fileBean);
                        }
                    }
                });
                viewUtil.setDifferentColorText("结算资料:","",mTvItemRepairAuditDetailFileSettleTitle);
            } else {
                viewUtil.setDifferentColorText("结算资料:","无",mTvItemRepairAuditDetailFileSettleTitle);
            }
        }
    }


    private OpenFileListener openFileListener;

    public interface OpenFileListener {
        void openFile(FileBean fileBean);
    }

    public void setOpenFileListener(OpenFileListener permissionListener) {
        this.openFileListener = permissionListener;
    }

}
