package com.jingkai.asset.function.supervise.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.base.BaseActivity;
import com.jingkai.asset.base.BaseBean;
import com.jingkai.asset.callback.OnItemClickRecyclerListener;
import com.jingkai.asset.config.RxConstant;
import com.jingkai.asset.config.URLConstant;
import com.jingkai.asset.function.repair.adapter.AuditProcessDetailFileAdapter;
import com.jingkai.asset.function.repair.adapter.AuditStatusAdapter;
import com.jingkai.asset.function.repair.adapter.SuperviseAcceptProcessAdapter;
import com.jingkai.asset.function.repair.entity.AuditStatusBean;
import com.jingkai.asset.function.repair.entity.FileBean;
import com.jingkai.asset.function.repair.entity.ProcessCacheBean;
import com.jingkai.asset.function.supervise.entity.SuperviseDetailBean;
import com.jingkai.asset.manager.RxManager;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.FileUtil;
import com.jingkai.asset.utils.MaxTextLengthFilter;
import com.jingkai.asset.utils.PermissionHelper;
import com.jingkai.asset.utils.TimeUtils;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.MeasureRecyclerView;
import com.jingkai.asset.widget.dialog.SelectMenuDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/4/8 16:35
 * Describe: 督查督办单详情
 */
public class SuperviseHandleDetailActivity extends BaseActivity {
    //审核状态列表
    @Bind(R.id.rv_repair_detail_check_status)
    RecyclerView mRvCheckStatus;
    //审核过程列表
    @Bind(R.id.rv_repair_detail_check_process)
    RecyclerView mRvCheckProcess;
    //完成标准附件列表
    @Bind(R.id.mrv_supervise_examine_detail_file)
    MeasureRecyclerView mMrvFile;
    //验收意见父布局
    @Bind(R.id.ll_supervise_handle_acceptance_parent)
    LinearLayout mLlAcceptanceParent;
    //操作按钮父布局
    @Bind(R.id.ll_supervise_handle_button_parent)
    LinearLayout mLlButtonparent;
    //任务名称
    @Bind(R.id.tv_supervise_detail_task_name)
    TextView mTvTaskName;
    //创建时间
    @Bind(R.id.tv_supervise_detail_create_time)
    TextView mTvCreateTime;
    //完成时限
    @Bind(R.id.tv_supervise_detail_complete_time)
    TextView mTvCompleteTime;
    //工单来源
    @Bind(R.id.tv_supervise_detail_source)
    TextView mTvSource;
    //所属项目
    @Bind(R.id.tv_supervise_detail_belong_project)
    TextView mTvBelongProject;
    //经营单位
    @Bind(R.id.tv_supervise_detail_operate_unit)
    TextView mTvOperateUnit;
    //资产位置
    @Bind(R.id.tv_supervise_detail_location)
    TextView mTvLocation;
    //资产分类
    @Bind(R.id.tv_supervise_detail_classify)
    TextView mTvClassify;
    //资产名称
    @Bind(R.id.tv_supervise_detail_assets_name)
    TextView mTvAssetsName;
    //发起人
    @Bind(R.id.tv_supervise_detail_initiator)
    TextView mTvInitiator;
    //责任人
    @Bind(R.id.tv_supervise_detail_charge_person)
    TextView mTvChargePerson;
    //接受人
    @Bind(R.id.tv_supervise_detail_receive_person)
    TextView mTvReceivePerson;
    //验收人
    @Bind(R.id.tv_supervise_detail_acceptor)
    TextView mTvAcceptor;
    //完成标准内容
    @Bind(R.id.tv_supervise_detail_standard_content)
    TextView mTvStandardContent;
    //验收意见
    @Bind(R.id.et_supervise_detail_opinion)
    EditText mEtOpinion;
    //完成时限
    @Bind(R.id.tv_supervise_detail_end_time)
    TextView mTvEndTime;
    //反馈意见
    @Bind(R.id.tv_supervise_detail_feedback_content)
    TextView mTvFeedbackContent;
    //完成情况附件
    @Bind(R.id.mrv_supervise_examine_detail_feedback_file)
    MeasureRecyclerView mMrvCompleteFile;
    //附件标题
    @Bind(R.id.tv_supervise_examine_file_title)
    TextView mTvFileTitle;
    //加载完成情况
    @Bind(R.id.ll_supervise_handle_complete_parent)
    LinearLayout mLlCompleteParent;
    //资产信息父布局
    @Bind(R.id.ll_supervise_detail_assets_parent)
    LinearLayout mLlAssetsParent;
    //设备名称
    @Bind(R.id.tv_supervise_detail_device)
    TextView mTvDevice;
    //完成标准附件标题
    @Bind(R.id.tv_supervise_detail_file_title)
    TextView mTvSuperviseDetailFileTitle;

    private int id;

    private SuperviseDetailBean detailBean;
    private int checkStatusWidth;

    private PermissionHelper mHelper;

    private int isRead;//是否已读  1是已读,0是未读

    public static void start(Context context, int id, int isRead) {
        Intent intent = new Intent(context, SuperviseHandleDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("isRead", isRead);
        context.startActivity(intent);
    }

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_supervise_examine_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("督查督办单");
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        isRead = intent.getIntExtra("isRead", 0);

        mEtOpinion.setFilters(new InputFilter[]{new MaxTextLengthFilter(255)});
        mEtOpinion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = mEtOpinion.getEditableText().toString();
                if (!TextUtils.isEmpty(text) && text.length() > 0 && !TextUtils.equals(text, "请输入您的意见，需要在255个字符内哦……")) {
                    mEtOpinion.setBackgroundResource(R.drawable.shape_fill_rectangle_grey_4);
                }
            }
        });

    }

    @Override
    protected void loadData() {
        ViewTreeObserver vto = mRvCheckStatus.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRvCheckStatus.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                checkStatusWidth = mRvCheckStatus.getWidth();

                requestData();
            }
        });


    }

    /**
     * 获取督查督办详情数据
     */
    private void requestData() {
        params.clear();
        params.put("id", id);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.SUPERVISE_DETAIL, params, new OKHttpManager.ResultCallback<BaseBean<SuperviseDetailBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<SuperviseDetailBean> response) {
                hideRDialog();
                SuperviseDetailBean detailBean = response.getBody();
                if (detailBean != null) {
                    setPageData(detailBean);
                } else {
                    ToastUtil.toastShortCenter("未查到详情数据");
                }
                //更新督查督办是否阅读
                if (isRead == 0) {
                    updateIsRead();
                }
            }
        }, this);
    }

    /**
     * 更新督查督办是否阅读
     */
    private void updateIsRead() {
        params.clear();
        params.put("id", id);
        OKHttpManager.postJsonRequest(URLConstant.UPDATE_SUPERVISE_IS_READ, params, new OKHttpManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(int code, String result, String message) {

            }

            @Override
            public void onResponse(BaseBean response) {
                RxManager rxManager = new RxManager();
                rxManager.post(RxConstant.RX_SUPERVISE_HANDLE_UPDATE, id);
            }
        }, this);
    }

    /**
     * 渲染页面
     *
     * @param detailBean
     */
    private void setPageData(SuperviseDetailBean detailBean) {
        this.detailBean = detailBean;
        ViewUtil util = ViewUtil.getViewUtil();
        util.setTextMessage(mTvTaskName, "任务名称", detailBean.getMissionName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        util.setTextMessage(mTvCreateTime, "创建时间", TimeUtils.longToDate(format, detailBean.getGmtCreate()));
        util.setTextMessage(mTvCompleteTime, "完成时限", TimeUtils.longToDate(format, detailBean.getCompleteTime()));
        util.setTextMessage(mTvSource, "工单来源", detailBean.getOrderSourceTitle());
        util.setTextMessage(mTvBelongProject, "所属项目", detailBean.getNameAcPark());
        util.setTextMessage(mTvOperateUnit, "经营单位", detailBean.getNameRbacDepartment());
        util.setTextMessage(mTvLocation, "资产位置", detailBean.getNameAcAssetPosition());
        util.setTextMessage(mTvClassify, "资产分类", detailBean.getNameAcAssetCategory());
        util.setTextMessage(mTvAssetsName, "资产名称", detailBean.getNameAcAssetsProperty());
        if (detailBean.getPrincipal() != null)
            util.setTextMessage(mTvInitiator, "发起人", detailBean.getPrincipal().getRbac_user_name());
        util.setTextMessage(mTvChargePerson, "责任人", detailBean.getChargeInfo());
        util.setTextMessage(mTvReceivePerson, "接收人", detailBean.getReceiver().getRbac_user_name());
        util.setTextMessage(mTvAcceptor, "验收人", detailBean.getAcceptor().getRbac_user_name());
        util.setTextMessage(mTvEndTime, "完成时限", TimeUtils.longToDate(format, detailBean.getCompleteTime()));
        util.setTextMessage(mTvDevice, "设备名称", detailBean.getNameAcEquipment());
        //有设备名称时不显示房产信息
        if (!TextUtils.isEmpty(detailBean.getNameAcEquipment())) {
            mLlAssetsParent.setVisibility(View.GONE);
        } else {
            mTvDevice.setVisibility(View.GONE);
        }
        mTvStandardContent.setText("\u3000\u3000" + (detailBean.getNotes() == null ? "" : detailBean.getNotes()));

        initStandardFileList();//加载完成标准附件列表

        initCompleteInfo();//加载完成情况

        loadStatusProgress();//加载验收状态

        loadCheckProcess();//加载验收过程列表

        showOperateMessage();//根据当前状态和角色进行显示操作提示

    }


    /**
     * 加载验收状态
     */
    private void loadStatusProgress() {
        String detailStatus = detailBean.getSupervisoryStatusTitle();
        List<AuditStatusBean> statusList = new ArrayList<AuditStatusBean>() {{
            add(new AuditStatusBean("待反馈"));
            add(new AuditStatusBean("待验收"));
            add(new AuditStatusBean("已验收"));
        }};
        //查询出当前状态在List中的位置
        //将该位置之前状态为已经完成,将对应节点变量
        int index = statusList.indexOf(new AuditStatusBean(detailStatus));
        for (int i = 0; i < statusList.size(); i++) {
            AuditStatusBean itemBean = statusList.get(i);
            if (i <= index) {
                if (i == index) itemBean.setLastDone(true);
                itemBean.setHaveDone(1);
            }
        }
        AuditStatusAdapter adapter = new AuditStatusAdapter(statusList, this, checkStatusWidth);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvCheckStatus.setLayoutManager(layoutManager);
        mRvCheckStatus.setAdapter(adapter);


    }

    /**
     * 处理当前状态展示的操作内容
     * <p>
     * (1)状态为待反馈的督查督办单，新建人和验收人打开的是详情页，待反馈人进去的是操作页，即详情页加需要去电脑端操作的温馨提示；
     * (2)状态为待验收的督查督办单，新建人和反馈人打开的是详情页，验收人打开的是操作页，即带验收意见和审批按钮的操作页；
     * (3)状态为已验收的督查督办单，上述三个角色打开的都是详情页。
     */
    private void showOperateMessage() {
        String status = detailBean.getSupervisoryStatusTitle();
        switch (status) {
            case "待反馈":
                if (detailBean.getMenuBack() == 1) {
                    ToastUtil.toastView("温馨提示\n\n由于您可能需要上传附件,请到电脑端操作");
                }
                break;
            case "待验收":
                if (detailBean.getMenuCheck() == 1) {
                    mLlAcceptanceParent.setVisibility(View.VISIBLE);
                    mLlButtonparent.setVisibility(View.VISIBLE);
                }
                break;
            case "已验收":
                break;
        }
    }

    /**
     * 审核过程
     */
    private void loadCheckProcess() {
        List<SuperviseDetailBean.ActionBean> processList = detailBean.getAction();
        SuperviseAcceptProcessAdapter processAdapter = new SuperviseAcceptProcessAdapter(processList, this);
        LinearLayoutManager processLayoutManager = new LinearLayoutManager(this);
        mRvCheckProcess.setLayoutManager(processLayoutManager);
        mRvCheckProcess.setAdapter(processAdapter);
    }

    /**
     * 初始化完成标准附件列表
     */
    private void initStandardFileList() {
        List<FileBean> fileList = detailBean.getSupervisoryFileList();
        if (fileList != null && fileList.size() > 0) {
            final AuditProcessDetailFileAdapter<FileBean> adapter = new AuditProcessDetailFileAdapter(fileList, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mMrvFile.setLayoutManager(layoutManager);
            mMrvFile.setAdapter(adapter);
            adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FileBean bean = adapter.getList().get(position);
                    if (TextUtils.isEmpty(bean.getAddr())) {
                        ToastUtil.toastShortCenter("暂无文件");
                    } else {
                        requestPermission(bean);
                    }
                }
            });
            mTvSuperviseDetailFileTitle.setText("附件:");
        } else {
            mTvSuperviseDetailFileTitle.setText("附件:无");
            mMrvFile.setVisibility(View.GONE);
        }
    }

    /**
     * 加载完成情况
     */
    private void initCompleteInfo() {

        List<SuperviseDetailBean.ActionBean> actionList = detailBean.getAction();
        String status = detailBean.getSupervisoryStatusTitle();
        boolean canShowComplete = false;//是否展示完成情况
        if (actionList != null && actionList.size() > 0) {
            for (SuperviseDetailBean.ActionBean actionBean : actionList) {
                if (actionBean.getSupervisoryAction() == 40 && (!"待反馈".equals(status))) {
                    canShowComplete = true;
                }
            }
        }

        if (canShowComplete) {
            mLlCompleteParent.setVisibility(View.VISIBLE);
            String feedbackContent = detailBean.getFeedbackContent();
            mTvFeedbackContent.setText("\u3000\u3000" + feedbackContent);
            //加载附件
            List<FileBean> fileList = detailBean.getFeedBackFileList();
            if (fileList != null && fileList.size() > 0) {
                final AuditProcessDetailFileAdapter<FileBean> adapter = new AuditProcessDetailFileAdapter(fileList, this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                mMrvCompleteFile.setLayoutManager(layoutManager);
                mMrvCompleteFile.setAdapter(adapter);
                adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FileBean bean = adapter.getList().get(position);
                        requestPermission(bean);
                    }
                });
            } else {
                mMrvCompleteFile.setVisibility(View.GONE);
                mTvFileTitle.setText("附件: 无");

            }
        }
    }


    /**
     * 获取权限
     */
    private void requestPermission(final FileBean bean) {
        if (mHelper == null) mHelper = new PermissionHelper(this);
        mHelper.requestPermissions(getString(R.string.voice_play_hint),
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        FileBean fileBean = new FileBean();
                        fileBean.setAddr(bean.getAddr());
                        FileUtil.getInstance().openFile(fileBean, SuperviseHandleDetailActivity.this);
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        mHelper.againWarnRequestPermission(getString(R.string.image_permission_hint), SuperviseHandleDetailActivity.this);
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    @OnClick({R.id.rl_repair_project_process_detail, R.id.bt_supervise_detail_unpass, R.id.bt_supervise_detail_pass})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_repair_project_process_detail:
                ProcessCacheBean.actionList = detailBean.getAction();
                Intent intent = new Intent(this, CheckProcessDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_supervise_detail_unpass:
                doCheckAndAccept(60);
                break;
            case R.id.bt_supervise_detail_pass:
                doCheckAndAccept(50);
                break;
        }
    }

    /**
     * 审批
     *
     * @param action 操作类型，50审批通过，60不通过
     */
    private void doCheckAndAccept(final int action) {
        //审批意见  不通过必填  通过选填
        final String opinion = mEtOpinion.getText().toString();
        if (action == 60) {
            if (TextUtils.isEmpty(opinion) || TextUtils.equals(opinion, "请输入您的意见，需要在255个字符内哦……")) {
                mEtOpinion.setBackgroundResource(R.drawable.shape_fill_rectangle_red);
                mEtOpinion.setText("请输入您的意见，需要在255个字符内哦……");
//            ToastUtil.toastShortCenter("请输入审批意见");
                return;
            }
        }


        final SelectMenuDialog dialog = new SelectMenuDialog(this);
        String title = "督查督办验收";
        String content = "";
        //60 验收驳回
        //50 验收通过
        switch (action) {
            case 60:
                content = "您确定验收驳回吗?";
                break;
            case 50:
                content = "您确定验收通过吗?";
                break;
        }

        dialog.setListener(new SelectMenuDialog.OnMenuSelectListener() {
            @Override
            public void callback() {
                dialog.dismiss();
                sendResultToServer(action, opinion);
            }
        });
        dialog.show();
        dialog.setTitle(title);
        dialog.setContent(content);

    }


    /**
     * 发送请求
     *
     * @param action
     * @param opinion
     */
    private void sendResultToServer(final int action, String opinion) {
        params.clear();
        params.put("id", id);
        params.put("action", action);
        params.put("acceptanceOpinion", opinion);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.SUPERVISE_SAVE, params, new OKHttpManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(int code, String result, String message) {
                ToastUtil.toastShortCenter(message);
                hideRDialog();
            }

            @Override
            public void onResponse(BaseBean response) {
                hideRDialog();
                //通知观察者更新数据
                new RxManager().post(RxConstant.RX_SUPERVISE_ACCEPT, 1);
                if (action == 50) {
                    ToastUtil.toastShortCenter("验收已通过");
                } else {
                    ToastUtil.toastShortCenter("验收已驳回");
                }
                finish();
            }
        }, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mHelper != null)
            mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
