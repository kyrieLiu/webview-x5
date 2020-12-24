package com.jingkai.asset.function.repair.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
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
import com.jingkai.asset.function.repair.adapter.RepairAuditProcessAdapter;
import com.jingkai.asset.function.repair.adapter.RepairEquipmnetAdapter;
import com.jingkai.asset.function.repair.entity.AuditStatusBean;
import com.jingkai.asset.function.repair.entity.FileBean;
import com.jingkai.asset.function.repair.entity.ProcessCacheBean;
import com.jingkai.asset.function.repair.entity.RepairDetailBean;
import com.jingkai.asset.function.repair.entity.RepairEquipmentBean;
import com.jingkai.asset.manager.RxManager;
import com.jingkai.asset.network.OKHttpManager;
import com.jingkai.asset.utils.FileUtil;
import com.jingkai.asset.utils.LogUtil;
import com.jingkai.asset.utils.MaxTextLengthFilter;
import com.jingkai.asset.utils.PermissionHelper;
import com.jingkai.asset.utils.TimeUtils;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.utils.view.ViewUtil;
import com.jingkai.asset.widget.MeasureRecyclerView;
import com.jingkai.asset.widget.dialog.SelectMenuDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuyin on 2019/4/8 16:35
 * Describe: 本页面为复用页面,包括待办事项列表,修缮计划单列表和修缮终审列表跳转进来的详情信息展示
 * type为0时为待办事项跳转进来,标题显示修缮项目详情,显示验收意见编辑框,显示通过未通过按钮,
 * type为1时为修缮计划单列表进来,标题显示修缮项目反馈,隐藏验收意见编辑框,隐藏通过未通过按钮
 * type为2时为修缮终审列表进来,标识根据审批未审批进行判断显示,同样页面显示逻辑根据审批未审批进行判断显示
 */
public class RepairProjectDetailActivity extends BaseActivity {
    //审核状态列表
    @Bind(R.id.rv_repair_detail_check_status)
    RecyclerView mRvCheckStatus;
    //审核过程列表
    @Bind(R.id.rv_repair_detail_check_process)
    RecyclerView mRvCheckProcess;
    //操作按钮父布局
    @Bind(R.id.ll_repair_detail_operate_parent)
    LinearLayout mLlOperateParent;
    //验收意见父布局
    @Bind(R.id.ll_repair_detail_acceptance_parent)
    LinearLayout mLlAcceptanceParent;
    //计划名称
    @Bind(R.id.tv_repair_plan_name)
    TextView mTvPlanName;
    //计划编号
    @Bind(R.id.tv_repair_number)
    TextView mTvNumber;
    //经营单位
    @Bind(R.id.tv_repair_unit)
    TextView mTvUnit;
    //所属项目
    @Bind(R.id.tv_repair_project)
    TextView mTvProject;
    //修缮类别
    @Bind(R.id.tv_repair_type)
    TextView mTvType;
    //开始时间
    @Bind(R.id.tv_repair_startTime)
    TextView mTvStartTime;
    //计划金额
    @Bind(R.id.tv_repair_money)
    TextView mTvMoney;
    //责任人
    @Bind(R.id.tv_repair_charge_person)
    TextView mTvChargePerson;
    //抄送人
    @Bind(R.id.tv_repair_copy_person)
    TextView mTvCopyPerson;
    //预审人
    @Bind(R.id.tv_repair_preliminary_person)
    TextView mTvPreliminaryPerson;
    //终审人
    @Bind(R.id.tv_repair_final_judgment_person)
    TextView mTvFinalJudgmentPerson;
    //描述信息
    @Bind(R.id.tv_repair_description_content)
    TextView mTvDescriptionContent;
    //意见反馈
    @Bind(R.id.et_repair_acceptance_opinion)
    EditText mEtRepairAcceptanceOpinion;
    //审核过程详情
    @Bind(R.id.ll_repair_detail_check_parent)
    LinearLayout mLlCheckParent;
    //审核状态父布局
    @Bind(R.id.ll_repair_detail_auditStatus_parent)
    LinearLayout mLlAuditStatusParent;
    //施工方
    @Bind(R.id.tv_repair_detail_constructionSide)
    TextView mTvConstructionSide;
    //联系人
    @Bind(R.id.tv_repair_detail_linkMan)
    TextView mTvLinkMan;
    //联系电话
    @Bind(R.id.tv_repair_detail_phone)
    TextView mTvPhone;
    //实际修缮金额
    @Bind(R.id.tv_repair_detail_money)
    TextView mTvRealMoney;
    //修缮完成时间
    @Bind(R.id.tv_repair_detail_completeTime)
    TextView mTvCompleteTime;
    //报审详情描述
    @Bind(R.id.tv_repair_detail_remark_title)
    TextView mTvRemarkTitle;
    //报审详情内容描述
    @Bind(R.id.tv_repair_detail_remark)
    TextView mTvRemark;
    //过程文件标题
    @Bind(R.id.tv_repair_detail_file_title)
    TextView mTvFileTitle;
    //过程文件列表
    @Bind(R.id.mrv_repair_detail_file)
    MeasureRecyclerView mMrvFile;
    //完成情况内容
    @Bind(R.id.ll_repair_detail_complete)
    LinearLayout mLlCompleteParent;
    //修缮审批父布局
    @Bind(R.id.ll_repair_detail_approval_parent)
    LinearLayout mLlRepairDetailApprovalParent;
    //修缮审批操作按钮父布局
    @Bind(R.id.ll_repair_detail_operate_approval_parent)
    LinearLayout mLlRepairDetailOperateApprovalParent;
    //修缮审批确定按钮
    @Bind(R.id.bt_repair_approval_pass)
    Button mBtRepairApprovalPass;
    //资产列表
    @Bind(R.id.rv_repair_equipment)
    RecyclerView mRvRepairEquipment;

    //结算资料标题
    @Bind(R.id.tv_repair_detail_settle_file_title)
    TextView mTvRepairDetailSettleFileTitle;
    //结算资料附件
    @Bind(R.id.mrv_repair_detail_file_settle)
    MeasureRecyclerView mMrvRepairDetailFileSettle;

    /**
     * type为0时为待办事项跳转进来
     * type为1时为修缮计划单列表进来
     * type为2时为修缮终审列表进来
     * type为3时为修缮审批列表进来
     */
    private int type;

    private int id;
    private RepairDetailBean detailBean;

    //申请权限
    private PermissionHelper mHelper;


    public static void start(Context context, int id, int intentType, int isRead, String statusName) {
        Intent intent = new Intent(context, RepairProjectDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("intentType", intentType);
        intent.putExtra("isRead", isRead);
        intent.putExtra("statusName", statusName);
        context.startActivity(intent);
    }

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_repair_project_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        /**
         * type为0时为待办事项跳转进来
         * type为1时为修缮计划单列表进来
         * type为2时为修缮终审列表进来
         * type为3时为修缮审批列表进来
         */
        type = intent.getIntExtra("intentType", 0);
        String statusName = intent.getStringExtra("statusName");

        LogUtil.e("statusName: " + statusName);

        String title = "";
        if (TextUtils.equals(statusName, "储备中")) {
            title = "修缮审批";
        } else if (TextUtils.equals(statusName, "待报审")) {
            title = "修缮报审";
        } else if (TextUtils.equals(statusName, "待终审")) {
            title = "修缮终审";
        } else if (TextUtils.equals(statusName, "已终审")) {
            title = "修缮终审";
        }

        setTitle(title);

        switch (type) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                setTitle("修缮终审");
                //如果工单是未读状态,将状态更新为已读
                int isRead = intent.getIntExtra("isRead", 0);
                if (isRead == 0) {
                    updateIsRead();
                }
                break;
            case 3:
                //修缮审批
                int isApprovalRead = intent.getIntExtra("isRead", 0);
                if (isApprovalRead == 0) {
                    updateIsRead();
                }
                break;
        }
        mEtRepairAcceptanceOpinion.setFilters(new InputFilter[]{new MaxTextLengthFilter(255)});
        mEtRepairAcceptanceOpinion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = mEtRepairAcceptanceOpinion.getEditableText().toString();
                if (!TextUtils.isEmpty(text) && text.length() > 0 && !TextUtils.equals(text,"请输入您的意见，需要在255个字符内哦……")) {
                    mEtRepairAcceptanceOpinion.setBackgroundResource(R.drawable.shape_fill_rectangle_grey_4);
                }
            }
        });
    }

    /**
     * 更新工单为已读
     */
    private void updateIsRead() {
        params.clear();
        params.put("id", id);
        OKHttpManager.postJsonRequest(URLConstant.UPDATE_REPAIR_IS_READ, params, new OKHttpManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(int code, String result, String message) {

            }

            @Override
            public void onResponse(BaseBean response) {
                RxManager rxManager = new RxManager();
                rxManager.post(RxConstant.RX_REPAIR_ORDER_UPDATE, id);
            }
        }, this);
    }


    @Override
    protected void loadData() {
        //加载审核状态数据
        ViewTreeObserver vto = mRvCheckStatus.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRvCheckStatus.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                requestData();
            }
        });
    }

    /**
     * 请求数据
     */
    private void requestData() {
        params.put("id", id);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.REPAIR_DETAIL, params, new OKHttpManager.ResultCallback<BaseBean<RepairDetailBean>>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean<RepairDetailBean> response) {
                hideRDialog();
                detailBean = response.getBody();
                if (detailBean == null) {
                    ToastUtil.toastShortCenter("暂未查到详情数据");
                } else {
                    setPageData(detailBean);
                }
            }
        }, this);
    }

    /**
     * 渲染页面
     *
     * @param bean
     */
    private void setPageData(RepairDetailBean bean) {
        ViewUtil util = ViewUtil.getViewUtil();
        util.setTextMessage(mTvPlanName, "计划名称", bean.getName());
        util.setTextMessage(mTvNumber, "计划编号", bean.getPlanCode());
        util.setTextMessage(mTvUnit, "经营单位", bean.getNameRbacDepartment());
        util.setTextMessage(mTvProject, "所属项目", bean.getIdAcParkTitle());
        util.setTextMessage(mTvType, "修缮类别", bean.getRepairProductTypeTitle());
        util.setTextMessage(mTvStartTime, "开始时间", TimeUtils.changeTimeFormat(new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"), bean.getGmtStart()));
        String money = bean.getRepairPlanAmount();
        if (!TextUtils.isEmpty(money)) {
            DecimalFormat df = new DecimalFormat("#,###.00");
            util.setTextMessage(mTvMoney, "计划金额", df.format(Double.parseDouble(money)) + " 元");
        }
        util.setTextMessage(mTvChargePerson, "责任人", bean.getPrincipalName());
        util.setTextMessage(mTvCopyPerson, "抄送人", bean.getReceiverName());
        util.setTextMessage(mTvFinalJudgmentPerson, "终审人", bean.getFinalperName());

        mTvDescriptionContent.setText("\u3000\u3000" + (bean.getNotes() == null ? "" : bean.getNotes()));

        initMaintenanceComplete(bean.getRepairReport());//加载维修完成情况

        initAuditStatus(bean);//初始化审核状态

        showOperateMessage(bean);//根据工单状态和身份展示应有的操作

        initAuditProcess(bean.getProcess());//加载审核过程流程

        initRepairEquipmentList(bean.getRepairEquipmentList());//加载资产列表
    }

    /**
     * 加载资产列表
     *
     * @param repairEquipmentBeans
     */
    private void initRepairEquipmentList(List<RepairEquipmentBean> repairEquipmentBeans) {
        RepairEquipmnetAdapter repairEquipmnetAdapter = new RepairEquipmnetAdapter(repairEquipmentBeans, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvRepairEquipment.setLayoutManager(linearLayoutManager);
        mRvRepairEquipment.setAdapter(repairEquipmnetAdapter);
    }

    /**
     * 渲染维修完成情况
     *
     * @param bean
     */
    private void initMaintenanceComplete(RepairDetailBean.RepairReportBean bean) {
        //待报审不显示维修完成情况
        if (bean == null || "待报审".equals(detailBean.getRepairStatusTitle())) return;
        mLlCompleteParent.setVisibility(View.VISIBLE);
        ViewUtil util = ViewUtil.getViewUtil();
        util.setDifferentSizeText("施工方:", bean.getConstructionSide(), mTvConstructionSide);
        //隐藏联系人 联系电话
//        util.setDifferentSizeText("联系人:", bean.getContact(), mTvLinkMan);
//        util.setDifferentSizeText("联系电话:", bean.getContactNumber(), mTvPhone);
        DecimalFormat df = new DecimalFormat("#,###.00");
        util.setDifferentSizeText("结算金额:", df.format(bean.getActualRepairAmount()) + "元", mTvRealMoney);
        util.setDifferentSizeText("竣工时间:", TimeUtils.getLineDayTime(bean.getGmtRepairCompletionTime()), mTvCompleteTime);
        //隐藏 报审详情描述
//        mTvRemark.setText("\u3000\u3000" + (bean.getNotes() == null ? "" : bean.getNotes()));

//        List<RepairDetailBean.ProcessBean> processList = detailBean.getProcess();
//        if (processList == null || processList.size() == 0) {
//            return;
//        }

        final List<FileBean> fileList = detailBean.getAttachmentReport4construction(); //工程资料
        if (fileList != null && fileList.size() > 0) {
            AuditProcessDetailFileAdapter adapter = new AuditProcessDetailFileAdapter(fileList, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mMrvFile.setLayoutManager(layoutManager);
            mMrvFile.setAdapter(adapter);
            adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FileBean fileBean = fileList.get(position);
                    requestFilePermission(fileBean);
                }
            });
            util.setDifferentSizeText("施工资料:","",mTvFileTitle);
        } else {
            util.setDifferentSizeText("施工资料:","无",mTvFileTitle);
        }

        final List<FileBean> settleFileList = detailBean.getAttachmentReport4settlement(); //结算资料
        if (settleFileList != null && settleFileList.size() > 0) {
            AuditProcessDetailFileAdapter adapter = new AuditProcessDetailFileAdapter(settleFileList, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mMrvRepairDetailFileSettle.setLayoutManager(layoutManager);
            mMrvRepairDetailFileSettle.setAdapter(adapter);
            adapter.setOnItemClickRecyclerAdapter(new OnItemClickRecyclerListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FileBean fileBean = settleFileList.get(position);
                    requestFilePermission(fileBean);
                }
            });
            util.setDifferentSizeText("结算资料:","",mTvRepairDetailSettleFileTitle);
        } else {
            util.setDifferentSizeText("结算资料:","无",mTvRepairDetailSettleFileTitle);
        }
    }


    /**
     * 获取权限
     *
     * @param fileBean
     */
    private void requestFilePermission(final FileBean fileBean) {
        if (mHelper == null) mHelper = new PermissionHelper(this);
        mHelper.requestPermissions(RepairProjectDetailActivity.this.getString(R.string.voice_play_hint),
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        FileUtil.getInstance().openFile(fileBean, RepairProjectDetailActivity.this);
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        mHelper.againWarnRequestPermission(RepairProjectDetailActivity.this.getString(R.string.image_permission_hint), RepairProjectDetailActivity.this);
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 初始化审核状态
     */
    private void initAuditStatus(RepairDetailBean detailBean) {

        int width = mRvCheckStatus.getWidth();

        String detailStatus = detailBean.getRepairStatusTitle();

        List<AuditStatusBean> statusList = new ArrayList<AuditStatusBean>() {{
            add(new AuditStatusBean("储备中"));
            add(new AuditStatusBean("待报审"));
//            add(new AuditStatusBean("待预审"));
            add(new AuditStatusBean("待终审"));
            add(new AuditStatusBean("已终审"));
        }};
        //查出当前状态是在List中的位置
        int statusIndex = statusList.indexOf(new AuditStatusBean(detailStatus));
        for (int i = 0; i < statusList.size(); i++) {
            AuditStatusBean itemBean = statusList.get(i);
            //下标在当前状态之前的都是已经做过了
            if (i <= statusIndex) {
                if (i == statusIndex) {
                    itemBean.setLastDone(true);
                }
                itemBean.setHaveDone(1);
            } else {
                itemBean.setHaveDone(0);
            }
        }
        AuditStatusAdapter adapter = new AuditStatusAdapter(statusList, this, width);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvCheckStatus.setLayoutManager(layoutManager);
        mRvCheckStatus.setAdapter(adapter);

    }

    /**
     * 处理当前状态展示的操作内容
     * (1)状态为储备中的修缮计划单，新建人、报审人、预审人、终审人打开是详情页，不需要操作和温馨提示，（非）应急审批人进去的是操作页，即详情页加需要去电脑端操作的温馨提示；
     * (2)状态为待报审的修缮计划单，新建人、（非）应急审批人、预审人、终审人看到都是详情页，报审人进去的是操作页，即详情页加需要去电脑端操作的温馨提示；
     * (3)状态为待预审的修缮计划单，新建人、（非）应急审批人、报审人、终审人打开是详情页，预审人进去的是操作页，即详情页加需要去电脑端操作的温馨提示；
     * (4)状态为待终审的修缮计划单，新建人、（非）应急审批人、报审人、预审人打开是详情页，终审人进去的是操作页，即带验收意见和审批按钮的操作页；
     * (5)状态为已终审的修缮计划单，上述5种种角色打开都是详情页；
     */
    private void showOperateMessage(RepairDetailBean detailBean) {
        String status = detailBean.getRepairStatusTitle();
        switch (status) {
            case "储备中":
                if (detailBean.getPersonTypeUrgenter() == 0
                        && TextUtils.equals(detailBean.getRepairProductTypeTitle(), "应急")) {
                    //应急显示转当年  只有应急审批人可以操作
                    mLlRepairDetailApprovalParent.setVisibility(View.VISIBLE);
                    mLlRepairDetailOperateApprovalParent.setVisibility(View.VISIBLE);
                    if (TextUtils.equals(detailBean.getRepairProductTypeTitle(), "应急")) {
                        mBtRepairApprovalPass.setText("转当年");
                    }
                } else if (detailBean.getPersonTypeNoUrgenter() == 0
                        && !TextUtils.equals(detailBean.getRepairProductTypeTitle(), "应急")) {
                    //非应急显示列入计划  只有非应急人可以操作
                    mLlRepairDetailApprovalParent.setVisibility(View.VISIBLE);
                    mLlRepairDetailOperateApprovalParent.setVisibility(View.VISIBLE);
                }

                break;
            case "待报审":
                if (detailBean.getPersonTypePrincipal() == 0) {
                    ToastUtil.toastView("温馨提示\n\n由于您可能需要上传附件,请到电脑端操作");
                }
                break;
            case "待预审":
                // 无此状态
//                if (detailBean.getPersonTypePretrial() == 0) {
//                    ToastUtil.toastView("温馨提示\n\n由于您可能需要上传附件,请到电脑端操作");
//                }
                break;
            case "待终审":
                if (detailBean.getPersonTypeFinalper() == 0) {
                    mLlOperateParent.setVisibility(View.VISIBLE);
                    mLlAcceptanceParent.setVisibility(View.VISIBLE);
                }
                break;
            case "已终审":
                break;
        }
    }

    /**
     * 初始化审核过程
     */
    private void initAuditProcess(List<RepairDetailBean.ProcessBean> processList) {
        if (processList != null && processList.size() > 0) {
            mLlCheckParent.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvCheckProcess.getLayoutParams();
            params.topMargin = ViewUtil.dp2px(15);
            RepairAuditProcessAdapter processAdapter = new RepairAuditProcessAdapter(processList, this);
            LinearLayoutManager processLayoutManager = new LinearLayoutManager(this);
            mRvCheckProcess.setLayoutManager(processLayoutManager);
            mRvCheckProcess.setAdapter(processAdapter);
        }
    }

    @OnClick({R.id.ctv_repair_project_process_detail, R.id.bt_repair_acceptance_not_pass,
            R.id.bt_repair_acceptance_pass, R.id.bt_repair_approval_not_pass, R.id.bt_repair_approval_pass})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ctv_repair_project_process_detail:
                ProcessCacheBean.processList = detailBean.getProcess();
                Intent intent = new Intent(this, RepairProcessDetailActivity.class);
                startActivity(intent);
                break;
            //验收通过
            case R.id.bt_repair_acceptance_pass:
                doCheckAndAccept(70);
                break;
            //验收未通过
            case R.id.bt_repair_acceptance_not_pass:
                doCheckAndAccept(80);
                break;
            //修缮审批作废
            case R.id.bt_repair_approval_not_pass:
                //24 转当年
                //25 列入计划
                //26 作废
                checkRepairApproval(26);
                break;
            //修缮审批列入计划或者转当年
            case R.id.bt_repair_approval_pass:
                //24 转当年
                //25 列入计划
                //26 作废
                if (TextUtils.equals(detailBean.getRepairProductTypeTitle(), "应急")) {
                    checkRepairApproval(24);
                } else {
                    checkRepairApproval(25);
                }
                break;

        }
    }

    /**
     * 执行验收
     *
     * @param action 操作类型，70-审核通过   80-审核驳回
     */
    private void doCheckAndAccept(final int action) {
        //审核意见  不通过必填  通过选填
        final String opinion = mEtRepairAcceptanceOpinion.getText().toString();
        if (action == 80){
            if(TextUtils.isEmpty(opinion) || TextUtils.equals(opinion,"请输入您的意见，需要在255个字符内哦……")){
                mEtRepairAcceptanceOpinion.setBackgroundResource(R.drawable.shape_fill_rectangle_red);
                mEtRepairAcceptanceOpinion.setText("请输入您的意见，需要在255个字符内哦……");
//            ToastUtil.toastShortCenter("请输入审核意见");
                return;
            }
        }

        final SelectMenuDialog dialog = new SelectMenuDialog(this);
        String title = "修缮终审";
        String content = "";
        //80 终审驳回
        //70 终审通过
        switch (action) {
            case 80:
                content = "您确定终审驳回吗?";
                break;
            case 70:
                content = "您确定终审通过吗?";
                break;
        }

        dialog.setListener(new SelectMenuDialog.OnMenuSelectListener() {
            @Override
            public void callback() {
                dialog.dismiss();
                sendCheckOrAcceptToServer(opinion,action);
            }
        });
        dialog.show();
        dialog.setTitle(title);
        dialog.setContent(content);

    }


    /**
     * 发送终审请求
     *
     * @param action
     */
    private void sendCheckOrAcceptToServer(String opinion, final int action) {

        params.clear();
        params.put("id", id);
        params.put("status", action);
        params.put("finalSuggest", opinion);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.REPAIR_PLAN_FINISH, params, new OKHttpManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(int code, String result, String message) {
                ToastUtil.toastShortCenter(message);
                hideRDialog();
            }

            @Override
            public void onResponse(BaseBean response) {
                hideRDialog();
                //通知观察者更新数据
                new RxManager().post(RxConstant.RX_REPAIR_CHECK, 1);
                if (action == 70) {
                    ToastUtil.toastShortCenter("终审已通过");
                } else {
                    ToastUtil.toastShortCenter("终审已驳回");
                }

                finish();
            }
        }, this);
    }


    /**
     * 修缮审批操作  作废  列入计划  转当年
     *
     * @param action
     */
    private void checkRepairApproval(final int action) {

        final SelectMenuDialog dialog = new SelectMenuDialog(RepairProjectDetailActivity.this);
        String title = "修缮审批";
        String content = "";
        //24 转当年
        //25 列入计划
        //26 作废
        switch (action) {
            case 24:
                content = "您确定转当年吗?";
                break;
            case 25:
                content = "您确定列入计划吗?";
                break;
            case 26:
                content = "您确定作废吗?";
                break;
        }

        dialog.setListener(new SelectMenuDialog.OnMenuSelectListener() {
            @Override
            public void callback() {
                dialog.dismiss();
                sendCheckToServer(action);
            }
        });
        dialog.show();
        dialog.setTitle(title);
        dialog.setContent(content);
    }


    /**
     * 发送请求
     */
    private void sendCheckToServer(final int action) {
        params.clear();
        params.put("id", id);
        params.put("status", action);
        showRDialog();
        OKHttpManager.postJsonRequest(URLConstant.REPAIR_APPROVAL_SAVE, params, new OKHttpManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(int code, String result, String message) {
                hideRDialog();
                ToastUtil.toastShortCenter(message);
            }

            @Override
            public void onResponse(BaseBean response) {
                hideRDialog();
                new RxManager().post(RxConstant.RX_REPAIR_CHECK, 1);
                if (action == 26) {
                    ToastUtil.toastShortCenter("作废处理成功");
                } else if (action == 24) {
                    ToastUtil.toastShortCenter("转当年处理成功");
                } else if (action == 25) {
                    ToastUtil.toastShortCenter("列入计划处理成功");
                }

                finish();
            }
        }, this);
    }

    //授权回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mHelper != null)
            mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
