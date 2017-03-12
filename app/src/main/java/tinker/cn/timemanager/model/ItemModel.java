package tinker.cn.timemanager.model;

/**
 * Created by tiankui on 3/11/17.
 */

public abstract class ItemModel {

    protected int type;

    public abstract int getType();

    public class TitleModel extends ItemModel{

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

    public class ContentModel extends ItemModel{
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

    public class ContentDetailModel extends ItemModel{

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
}
