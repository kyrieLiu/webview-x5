package com.jingkai.asset.function.supervise.adapter;

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
import com.jingkai.asset.function.repair.adapter.AuditProcessDetailFileAdapter;
import com.jingkai.asset.function.repair.entity.FileBean;
import com.jingkai.asset.function.supervise.entity.SuperviseDetailBean;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.MeasureRecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by liuyin on 2019/4/9 16:31
 * Description:督查督办单验收过程详情适配器
 */
public class SuperviseProcessDetailAdapter<T extends SuperviseDetailBean.ActionBean> extends BaseRecyclerViewAdapter<T> {

    public SuperviseProcessDetailAdapter(List<T> list, Context context) {
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

        private ViewUtil viewUtil;


        public MyViewHolder(View itemView) {
            super(itemView);
            viewUtil = ViewUtil.getViewUtil();
        }

        @Override
        protected void bind(final T bean, int position) {

            /**
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

            boolean canShowFile = true;//是否需要展示附件

            /**
             *(1) 新建：有
             * (2) 反馈：有
             * (3) 验收：没有（验收通过）；没有（验收驳回） */

            boolean canShowRemark = true;//是否需要展示备注信息

            String statusType = bean.getStatusChange();

            if (TextUtils.isEmpty(statusType)) statusType = "报审提交";
            switch (statusType) {
                case "新建保存":
                case "新建提交":
                case "新建保存(APP)":
                case "新建提交(APP)":
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
                case "报审保存":
                case "报审提交":
                case "报审保存(APP)":
                case "报审提交(APP)":
//                    mTvRemarkTitle.setText("完成标准: ");
                    viewUtil.setDifferentColorText("完成标准: ","",mTvRemarkTitle);
                    break;
                case "反馈草稿":
                case "反馈提交":
                case "反馈草稿(APP)":
                case "反馈提交(APP)":
//                    mTvRemarkTitle.setText("反馈内容: ");
                    viewUtil.setDifferentColorText("反馈内容: ","",mTvRemarkTitle);
                    break;
                case "验收通过":
                case "验收不通过":
                case "验收通过(APP)":
                case "验收不通过(APP)":
//                    mTvRemarkTitle.setText("验收意见: ");
                    viewUtil.setDifferentColorText("验收意见: ","",mTvRemarkTitle);
                    canShowFile = false;
                    break;
                case "验收驳回":
                case "验收驳回(APP)":
//                    mTvRemarkTitle.setText("验收意见: ");
                    viewUtil.setDifferentColorText("验收意见: ","",mTvRemarkTitle);
                    canShowFile = false;
                    break;
                default:
                    canShowRemark = false;
                    break;
            }

            viewUtil.setDifferentColorText("操作人:", bean.getCreator(), mTvAuditApplyPerson);
            viewUtil.setDifferentColorText("操作时间:", bean.getGmtCreate(), mTvCommitApplyTime);
            viewUtil.setDifferentColorText("操\u3000作:", statusType, mTvStage);

            //判断是否需要展示备注信息
            if (canShowRemark) {
                mTvRemarkTitle.setVisibility(View.VISIBLE);
                mTvRemark.setVisibility(View.VISIBLE);
                String notes = TextUtils.isEmpty(bean.getNotes())?"无":bean.getNotes();
                mTvRemark.setText("\u3000\u3000" + notes);
            } else {
                mTvRemarkTitle.setVisibility(View.GONE);
                mTvRemark.setVisibility(View.GONE);
            }

            //如果需要展示附件
            if (canShowFile) {
                mTvFileTitle.setVisibility(View.VISIBLE);
                loadFileAdapter(bean);
                mMrvFile.setVisibility(View.VISIBLE);
            } else {
                mTvFileTitle.setVisibility(View.GONE);
                mMrvFile.setVisibility(View.GONE);
            }

        }

        /**
         * 加载附件列表
         *
         * @param bean
         */
        private void loadFileAdapter(SuperviseDetailBean.ActionBean bean) {
            final List<FileBean> fileList = bean.getFiles();
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
//                mTvFileTitle.setText("附件");
                viewUtil.setDifferentColorText("附件","",mTvFileTitle);
            } else {
//                mTvFileTitle.setText("附件: 无");
                viewUtil.setDifferentColorText("附件: ","无",mTvFileTitle);
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
