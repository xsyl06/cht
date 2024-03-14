package com.cht.admin.pojo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RoutesVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 6480984596425247067L;
    private String path;
    private Meta meta;
    private String name;
    private List<RoutesVo> children;
    public static class Meta{
        private String icon;
        private String title;
        private int rank;
        private boolean keepAlive;
        private String extraIcon;
        private boolean showLink;
        private boolean showParent;
        private List<String> auths;
        private String frameSrc;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public boolean isKeepAlive() {
            return keepAlive;
        }

        public void setKeepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
        }

        public String getExtraIcon() {
            return extraIcon;
        }

        public void setExtraIcon(String extraIcon) {
            this.extraIcon = extraIcon;
        }

        public boolean isShowLink() {
            return showLink;
        }

        public void setShowLink(boolean showLink) {
            this.showLink = showLink;
        }

        public boolean isShowParent() {
            return showParent;
        }

        public void setShowParent(boolean showParent) {
            this.showParent = showParent;
        }

        public List<String> getAuths() {
            return auths;
        }

        public void setAuths(List<String> auths) {
            this.auths = auths;
        }

        public String getFrameSrc() {
            return frameSrc;
        }

        public void setFrameSrc(String frameSrc) {
            this.frameSrc = frameSrc;
        }
    }
}
