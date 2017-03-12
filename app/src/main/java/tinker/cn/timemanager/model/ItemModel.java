package tinker.cn.timemanager.model;

/**
 * Created by tiankui on 3/11/17.
 */

public abstract class ItemModel {

    protected int type;

    public abstract int getType();

    /**
     * 此为主界面的数据model；
     */
    public class TitleModel extends ItemModel {

        private String titleName;

        @Override
        public int getType() {
            return BaseConstant.TITLE_TYPE;
        }

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }
    }

    public class ContentModel extends ItemModel {
        private String contentName;
        private long time;

        @Override
        public int getType() {
            return BaseConstant.CONTENT_TYPE;
        }

        public String getContentName() {
            return contentName;
        }

        public void setContentName(String contentName) {
            this.contentName = contentName;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    public class ContentDetailModel extends ItemModel {

        private long totalTime;

        @Override
        public int getType() {
            return BaseConstant.CONTENT_DETAIL_TYPE;
        }

        public long getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(long totalTime) {
            this.totalTime = totalTime;
        }
    }

    /**
     * 以下为添加计划的数据model
     */

    /**
     * 已添加的计划概览（只有名字和删除选项），点击可以查看这个计划的具体详情
     */
    public static class PlanSummaryModel extends ItemModel {

        private String planName;

        @Override
        public int getType() {
            return BaseConstant.PLAN_SUMMARY_TYPE;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }
    }

    /**
     * 此为点击查看详情，用户可以浏览这个计划的制定的具体情况，然后可以再次修改；
     */
    public class PlanDetailModel extends ItemModel {
        private String planName;
        private int priority;
        private long planTime;
        private long planDuration;
        private String addParentPlanId;
        private String addParentPlanName;
        private String tag;

        @Override
        public int getType() {
            return BaseConstant.PLAN_DETAIL_TYPE;
        }


        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public long getPlanTime() {
            return planTime;
        }

        public void setPlanTime(long planTime) {
            this.planTime = planTime;
        }

        public long getPlanDuration() {
            return planDuration;
        }

        public void setPlanDuration(long planDuration) {
            this.planDuration = planDuration;
        }

        public String getAddParentPlanId() {
            return addParentPlanId;
        }

        public void setAddParentPlanId(String addParentPlanId) {
            this.addParentPlanId = addParentPlanId;
        }

        public String getAddParentPlanName() {
            return addParentPlanName;
        }

        public void setAddParentPlanName(String addParentPlanName) {
            this.addParentPlanName = addParentPlanName;
        }


        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    /**
     * 此为添加计划概要，一般如果用户不想自定义其他选项，只需要添加计划名称即可；
     */

    public static class AddPlanModel extends ItemModel {
        private String planName;
        private int priority;
        private long planTime;
        private long planDuration;
        private String addParentPlanId;
        private String addParentPlanName;
        private String tag;

        public AddPlanModel(){}
        @Override
        public int getType() {
            return BaseConstant.ADD_PLAN_TYPE;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public long getPlanTime() {
            return planTime;
        }

        public void setPlanTime(long planTime) {
            this.planTime = planTime;
        }

        public long getPlanDuration() {
            return planDuration;
        }

        public void setPlanDuration(long planDuration) {
            this.planDuration = planDuration;
        }

        public String getAddParentPlanId() {
            return addParentPlanId;
        }

        public void setAddParentPlanId(String addParentPlanId) {
            this.addParentPlanId = addParentPlanId;
        }

        public String getAddParentPlanName() {
            return addParentPlanName;
        }

        public void setAddParentPlanName(String addParentPlanName) {
            this.addParentPlanName = addParentPlanName;
        }


        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    public class AddPlanSummaryModel extends ItemModel {
        public String planName;

        @Override
        public int getType() {
            return BaseConstant.ADD_PLAN_SIMPLE_TYPE;
        }
    }

    public class AddPlanDetailModel extends ItemModel {

        @Override
        public int getType() {
            return BaseConstant.ADD_PLAN_DETAIL_TYPE;
        }
    }
}
